package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import de.icevizion.aves.inventory.function.CloseFunction;
import de.icevizion.aves.inventory.function.OpenFunction;
import de.icevizion.aves.util.functional.ThrowingFunction;
import de.icevizion.aves.inventory.slot.ISlot;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryCondition;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.timer.ExecutionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 * @version 1.2.0
 * @since 1.0.12
 */
@SuppressWarnings("java:S3252")
public abstract class InventoryBuilder implements SizeChecker {

    private static final int INVALID_SLOT_ID = -999;
    protected static final Logger LOGGER = LoggerFactory.getLogger(InventoryBuilder.class);
    protected final InventoryType type;
    private InventoryLayout inventoryLayout;
    private InventoryLayout dataLayout;
    protected boolean inventoryLayoutValid = true;
    protected boolean dataLayoutValid = false;
    protected OpenFunction openFunction;
    protected CloseFunction closeFunction;
    protected ThrowingFunction<InventoryLayout, InventoryLayout> dataLayoutFunction;
    protected InventoryCondition inventoryCondition;

    /**
     * Creates a new instance from the inventory builder with the given size.
     * @param type The type from the inventory to get the size from it
     */
    protected InventoryBuilder(@NotNull InventoryType type) {
        checkInventorySize(type.getSize());
        this.type = type;

        this.inventoryCondition = (player, slot, clickType, inventoryConditionResult) -> {
            if (slot == INVALID_SLOT_ID) return;

            if (this.dataLayout != null) {
                var clickedSlot = this.dataLayout.getSlot(slot);
                acceptClick(clickedSlot, player, clickType, slot, inventoryConditionResult);
            }

            if (this.inventoryLayout != null) {
                var clickedSlot = this.inventoryLayout.getSlot(slot);
                acceptClick(clickedSlot, player, clickType, slot, inventoryConditionResult);
            }
        };
    }

    /**
     * Handles the click request on a given slot.
     * @param slot the {@link ISlot} which is clicked
     * @param player the {@link Player} who is involved
     * @param clickType the given {@link ClickType}
     * @param result the given {@link InventoryConditionResult}
     */
    private void acceptClick(@Nullable ISlot slot, @NotNull Player player, @NotNull ClickType clickType, int slotID, @NotNull InventoryConditionResult result) {
        if (slot != null && slot.getClick() != null) {
            slot.getClick().onClick(player, clickType, slotID, result);
        }
    }

    /**
     * Set a new reference to the data layout
     * @param dataLayout The {@link InventoryLayout} to set
     */
    public void setDataLayoutFunction(ThrowingFunction<InventoryLayout, InventoryLayout> dataLayout) {
        this.dataLayoutFunction = dataLayout;
    }

    /**
     * Registers the underlying listeners into the event graph.
     */
    public abstract void register();

    /**
     * Removes the underlying listeners from the event graph.
     */
    public abstract void unregister();

    //Abstract methods
    public abstract Inventory getInventory(@Nullable Locale locale);

    /**
     * Returns if the inventory is currently opened by a player.
     * @return True if a player does the inventory currently opened otherwise false
     */
    protected abstract boolean isOpen();

    /**
     * Updates the underlying inventory.
     */
    protected abstract void updateInventory();

    /**
     * Apply the data layout to the inventory
     */
    protected abstract void applyDataLayout();

    /**
     * Marks the inventory layout as not valid an updates the inventory.
     */
    public void invalidateLayout() {
        inventoryLayoutValid = false;
        if (isOpen()) {
            updateInventory();
        }
    }

    /**
     * Invalidates the boolean for the data layout.
     */
    public void invalidateDataLayout() {
        if (isOpen()) {
            retrieveDataLayout();
            LOGGER.info("DataLayout invalidated on open inv, requested data...");
        } else {
            LOGGER.info("DataLayout invalidated on closed inv");
            dataLayoutValid = false;
        }
    }

    /**
     * Handles the open function when a inventory will be opened.
     * @param event The instance from the inventory event
     */
    protected void handleOpen(@NotNull InventoryOpenEvent event) {
        if (openFunction != null) {
            openFunction.onOpen(event);
        }
    }

    /**
     * Handles the close function if a close event is called from the server
     * @param event The instance from the close event
     */
    protected void handleClose(@NotNull InventoryCloseEvent event) {
        if (closeFunction != null) {
            closeFunction.onClose(event);
        }
    }

    /**
     * Updates the given inventory with the content.
     * @param inventory the inventory which should receive the update
     * @param title the title for the inventory
     * @param locale the locale for the inventory
     * @param messageProvider the instance to a {@link MessageProvider}
     * @param applyLayout if the layout should be applied
     */
    protected void updateInventory(@NotNull Inventory inventory,
                                   Component title,
                                   Locale locale,
                                   MessageProvider messageProvider,
                                   boolean applyLayout) {
        if (getLayout() == null) {
            LOGGER.info("Can't update content because the main layout is null");
            return;
        }

        if (!Component.EQUALS.test(inventory.getTitle(), title)) {
            LOGGER.info("UpdateInventory is updating the title");
            inventory.setTitle(title);
        }

        // Design
        if (applyLayout) {
            var contents = inventory.getItemStacks();
            inventory.clear();
            getLayout().applyLayout(contents, locale, messageProvider);
            this.setItemsInternal(inventory, contents);
            LOGGER.info("UpdateInventory applied the InventoryLayout!");
            this.inventoryLayoutValid = true;
        }

        // Values
        synchronized (this) {
            if (!dataLayoutValid) {
                retrieveDataLayout();
            } else {
                if (getDataLayout() != null) {
                    var contents = inventory.getItemStacks();
                    getDataLayout().applyLayout(contents, locale, messageProvider);
                    this.setItemsInternal(inventory, contents);
                }
            }
        }
    }

    private void setItemsInternal(@NotNull Inventory inventory, @NotNull ItemStack[] contents) {
        for (int i = 0; i < contents.length; i++) {
            var contentSlot = contents[i];
            if (contentSlot == null || contentSlot.material() == Material.AIR) continue;
            inventory.setItemStack(i, contents[i]);
        }
    }

    protected void retrieveDataLayout() {
        synchronized (this) {
            if (this.dataLayoutFunction == null) return;
            MinecraftServer.getSchedulerManager().scheduleNextTick(() -> {
                try {
                    this.dataLayout = this.dataLayoutFunction.acceptThrows(this.dataLayout);
                    applyDataLayout();
                } catch (Exception exception) {
                    MinecraftServer.getExceptionManager().handleException(exception);
                }
            }, ExecutionType.ASYNC);
        }
    }

    /**
     * Updates the inventory for all current viewers.
     * @param inventory The inventory to update
     */
    protected void updateViewer(@NotNull Inventory inventory) {
        if (inventory.getViewers().isEmpty()) return;
        inventory.update();
    }

    /**
     * Returns the underlying inventory.
     * Please note this method ignores the translation context
     * @return the given inventory
     */
    public Inventory getInventory() {
        return getInventory(null);
    }

    /**
     * Returns the given {@link InventoryType} for the inventory.
     * @return the given type
     */
    public InventoryType getType() {
        return type;
    }

    /**
     * Set's the open function to the builder
     * @param openFunction The function to set
     */
    public InventoryBuilder setOpenFunction(OpenFunction openFunction) {
        this.openFunction = openFunction;
        return this;
    }

    /**
     * Set's the close function to the builder
     * @param closeFunction The function to set
     */
    public InventoryBuilder setCloseFunction(CloseFunction closeFunction) {
        this.closeFunction = closeFunction;
        return this;
    }

    /**
     * Set a new instance of the {@link InventoryLayout} to the builder
     * @param inventoryLayout The layout to set
     */
    public InventoryBuilder setLayout(@NotNull InventoryLayout inventoryLayout) {
        this.inventoryLayout = inventoryLayout;
        return this;
    }

    /**
     * Returns the underlying {@link InventoryLayout}.
     * @return the given layout
     */
    @Nullable
    public InventoryLayout getLayout() {
        return inventoryLayout;
    }

    /**
     * Get underlying data {@link InventoryLayout}.
     * @return the given layout
     */
    @Nullable
    public InventoryLayout getDataLayout() {
        return dataLayout;
    }
}
