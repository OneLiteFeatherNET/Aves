package net.theevilreaper.aves.inventory;

import net.minestom.server.inventory.click.Click;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.function.CloseFunction;
import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.theevilreaper.aves.inventory.function.OpenFunction;
import net.theevilreaper.aves.inventory.slot.EmptySlot;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.inventory.util.InventoryConstants;
import net.theevilreaper.aves.util.functional.ThrowingFunction;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
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
public abstract class InventoryBuilder {

    protected static final Logger LOGGER = LoggerFactory.getLogger(InventoryBuilder.class);
    protected final InventoryType type;
    private InventoryLayout inventoryLayout;
    private InventoryLayout dataLayout;
    protected boolean inventoryLayoutValid = true;
    protected boolean dataLayoutValid = false;
    protected OpenFunction openFunction;
    protected CloseFunction closeFunction;
    protected ThrowingFunction<InventoryLayout, InventoryLayout> dataLayoutFunction;
    protected InventoryClick inventoryClick;

    /**
     * Creates a new instance from the inventory builder with the given size.
     *
     * @param type The type from the inventory to get the size from it
     */
    protected InventoryBuilder(@NotNull InventoryType type) {
        this.type = type;

        this.inventoryClick = (player, slot, clickType) -> {
            LOGGER.info("Clicked slot: " + slot);
            if (slot == InventoryConstants.INVALID_SLOT_ID) return ClickHolder.noClick();

            if (this.dataLayout != null) {
                var clickedSlot = this.dataLayout.getSlot(slot);
                return acceptClick(clickedSlot, player, clickType, slot);
            }

            if (this.inventoryLayout != null) {
                var clickedSlot = this.inventoryLayout.getSlot(slot);
                return acceptClick(clickedSlot, player, clickType, slot);
            }

            return ClickHolder.noClick();
        };
    }

    /**
     * Handles the click request on a given slot.
     *
     * @param slot      the {@link ISlot} which is clicked
     * @param player    the {@link Player} who is involved
     * @param clickType the given {@link ClickType}
     */
    private @NotNull ClickHolder acceptClick(@Nullable ISlot slot, @NotNull Player player, @NotNull Click clickType, int slotID) {
        if (slot == null) return ClickHolder.noClick();
        if (slot instanceof EmptySlot) return ClickHolder.noClick();
        return slot.getClick().onClick(player, slotID, clickType);
    }

    /**
     * Set a new reference to the data layout
     *
     * @param dataLayout The {@link InventoryLayoutImpl} to set
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
     *
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
        if (!inventoryLayoutValid) return;
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
     * Handles the open function when an inventory will be opened.
     *
     * @param event The instance from the inventory event
     */
    protected void handleOpen(@NotNull InventoryOpenEvent event) {
        if (this.openFunction == null) return;
        openFunction.onOpen(event);
    }

    /**
     * Handles the close function if a close event is called from the server
     *
     * @param event The instance from the close event
     */
    protected void handleClose(@NotNull InventoryCloseEvent event) {
        if (this.closeFunction == null) return;
        closeFunction.onClose(event);
    }

    /**
     * Updates the given inventory with the content.
     *
     * @param inventory   the inventory which should receive the update
     * @param title       the title for the inventory
     * @param locale      the locale for the inventory
     * @param applyLayout if the layout should be applied
     */
    protected void updateInventory(@NotNull Inventory inventory,
                                   Component title,
                                   Locale locale,
                                   boolean applyLayout) {
        if (this.inventoryLayout == null) {
            throw new IllegalStateException("Can't update content because the layout is null");
        }

        // Design
        if (applyLayout) {
            var contents = inventory.getItemStacks();
            inventory.clear();
            getLayout().applyLayout(contents, locale);
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
                    getDataLayout().applyLayout(contents, locale);
                    this.setItemsInternal(inventory, contents);
                }
            }
        }
    }

    /**
     * Set's the given array with the {@link ItemStack}'s into an inventory.
     *
     * @param inventory the inventory for the items
     * @param contents  the array itself which contains all items
     */
    private void setItemsInternal(@NotNull Inventory inventory, @NotNull ItemStack[] contents) {
        for (int i = 0; i < contents.length; i++) {
            var contentSlot = contents[i];
            if (contentSlot == null || contentSlot.material() == Material.AIR) continue;
            inventory.setItemStack(i, contents[i]);
        }
    }

    /**
     * Executes the logic to retrieve the {@link InventoryLayoutImpl} which comes from the {@link ThrowingFunction}.
     */
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
            });
        }
    }

    /**
     * Updates the title of an inventory.
     * Note that this method must be called from the developer
     *
     * @param inventory the {@link Inventory} to update the title
     * @param newTitle  the new title as {@link Component} to set
     */
    protected void updateTitle(@NotNull Inventory inventory, @NotNull Component newTitle) {
        inventory.setTitle(newTitle);
    }

    /**
     * Updates the inventory for all current viewers.
     *
     * @param inventory The inventory to update
     */
    protected void updateViewer(@NotNull Inventory inventory) {
        if (inventory.getViewers().isEmpty()) return;
        inventory.update();
    }

    /**
     * Returns the underlying inventory.
     * Please note this method ignores the translation context
     *
     * @return the given inventory
     */
    public Inventory getInventory() {
        return getInventory(null);
    }

    /**
     * Returns the given {@link InventoryType} for the inventory.
     *
     * @return the given type
     */
    public @NotNull InventoryType getType() {
        return type;
    }

    /**
     * Set's the open function to the builder
     *
     * @param openFunction The function to set
     */
    public InventoryBuilder setOpenFunction(OpenFunction openFunction) {
        this.openFunction = openFunction;
        return this;
    }

    /**
     * Set's the close function to the builder
     *
     * @param closeFunction The function to set
     */
    public InventoryBuilder setCloseFunction(CloseFunction closeFunction) {
        this.closeFunction = closeFunction;
        return this;
    }

    /**
     * Set a new instance of the {@link InventoryLayoutImpl} to the builder
     *
     * @param inventoryLayoutImpl The layout to set
     */
    public InventoryBuilder setLayout(@NotNull InventoryLayout inventoryLayoutImpl) {
        this.inventoryLayout = inventoryLayoutImpl;
        return this;
    }

    /**
     * Returns the underlying {@link InventoryLayoutImpl}.
     *
     * @return the given layout
     */
    public @Nullable InventoryLayout getLayout() {
        return inventoryLayout;
    }

    /**
     * Get underlying data {@link InventoryLayoutImpl}.
     *
     * @return the given layout
     */
    public @Nullable InventoryLayout getDataLayout() {
        return dataLayout;
    }
}
