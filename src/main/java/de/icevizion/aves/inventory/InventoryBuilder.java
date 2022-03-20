package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import de.icevizion.aves.inventory.function.CloseFunction;
import de.icevizion.aves.inventory.function.OpenFunction;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.EventNode;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.event.trait.InventoryEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.utils.time.TimeUnit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 * @version 1.1.0
 * @since 1.0.12
 */
public abstract class InventoryBuilder implements SizeChecker {

    protected static final SchedulerManager SCHEDULER_MANAGER = MinecraftServer.getSchedulerManager();
    protected static final Logger LOGGER = LoggerFactory.getLogger(MinecraftServer.class);

    protected final EventNode<InventoryEvent> inventoryNode = EventNode.type("inventories", EventFilter.INVENTORY);

    private InventoryType type;

    private InventoryLayout inventoryLayout;
    private InventoryLayout dataLayout;

    protected boolean inventoryLayoutValid = true;
    protected boolean dataLayoutValid = false;

    protected OpenFunction openFunction;
    protected CloseFunction closeFunction;

    protected EventListener<InventoryOpenEvent> openEventListener;
    protected EventListener<InventoryPreClickEvent> clickEventListener;
    protected EventListener<InventoryCloseEvent> closeEventListener;

    public InventoryBuilder(@NotNull InventoryType type) {
        checkInventorySize(type.getSize());
        this.type = type;
    }

    public void registerInNode() {
        this.clickEventListener = EventListener.of(InventoryPreClickEvent.class, this::handleClick);

        if (openFunction != null) {
            this.inventoryNode.addListener(openEventListener);
        }

        if (closeFunction != null) {
            this.inventoryNode.addListener(closeEventListener);
        }

        this.inventoryNode.addListener(clickEventListener);
        MinecraftServer.getGlobalEventHandler().addChild(this.inventoryNode);
    }

    public void unregister() {
        MinecraftServer.getGlobalEventHandler().removeChild(this.inventoryNode);
    }

    public void registerGlobally() {
        MinecraftServer.getGlobalEventHandler().addListener(InventoryPreClickEvent.class, this::handleClick);
    }

    //Abstract methods
    public abstract Inventory getInventory(@Nullable Locale locale);

    protected abstract boolean isInventoryOpen();

    protected abstract void updateInventory();

    protected abstract void applyDataLayout();

    public void invalidateInventoryLayout() {
        inventoryLayoutValid = false;

        if (isInventoryOpen()) {
            updateInventory();
        }
    }

    public void invalidateDataLayout() {
        if (isInventoryOpen()) {
            retrieveDataLayout();
            LOGGER.info("DataLayout invalidated on open inv, requested data...");
        } else {
            LOGGER.info("DataLayout invalidated on closed inv");
            dataLayoutValid = false;
        }
    }

    protected void handleClick(InventoryPreClickEvent event) {
        if (event.getSlot() < 0 || event.getSlot() > type.getSize() - 1)
            return; //Not within this inventory

        if (getDataLayout() != null) {
            var dataSlot = getDataLayout().getContents()[event.getSlot()];
            if (dataSlot != null && dataSlot.getClickListener() != null) {
                dataSlot.getClickListener().accept(event);
                return;
            }
        }

        var layoutSlot = getInventoryLayout().getContents()[event.getSlot()];
        if (layoutSlot != null && layoutSlot.getClickListener() != null) {
            layoutSlot.getClickListener().accept(event);
        }
    }

    protected void handleOpen(@NotNull InventoryOpenEvent event) {
        if (openFunction != null) {
            openFunction.onOpen(event);
        }
    }

    protected void handleClose(InventoryCloseEvent event) {
        if (closeFunction != null) {
            if (!closeFunction.onClose(event)) {
                SCHEDULER_MANAGER.buildTask(()
                        -> event.setNewInventory(getInventory())).delay(150, TimeUnit.MILLISECOND).schedule();
            }
        }
    }

    protected void updateInventory(Inventory inventory, Component title, Locale locale, MessageProvider messageProvider, boolean applyLayout) {
        applyLayout |= !inventoryLayoutValid;

        if (!Component.EQUALS.test(inventory.getTitle(), title)) {
            LOGGER.info("UpdateInventory is updating the title");
            inventory.setTitle(title);
        }

        if (applyLayout) {
            var contents = inventory.getItemStacks();
            getInventoryLayout().applyLayout(contents, locale, messageProvider);
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].getMaterial() == Material.AIR) continue;
                inventory.setItemStack(i, contents[i]);
            }
           LOGGER.info("UpdateInventory applied the InventoryLayout!");
        }

        synchronized (this) {
            if (!dataLayoutValid /*&& dataLayoutChain == null*/) {
                retrieveDataLayout();
            } else {
                if (getDataLayout() != null) {
                    var contents = inventory.getItemStacks();
                    getDataLayout().applyLayout(contents, locale, messageProvider);
                    for (int i = 0; i < contents.length; i++) {
                        if (contents[i].getMaterial() == Material.AIR) continue;
                        inventory.setItemStack(i, contents[i]);
                    }
                }
            }
        }
    }

    protected void retrieveDataLayout() {
       /*synchronized (this) {
            if (dataLayoutChainFunction == null)
                return;

            if (dataLayoutChain != null) {
                dataLayoutChain.abortChain();
            }

            TaskChainFactory factory = ServiceRegistry.getService("TaskChainFactory");
            dataLayoutChain = factory.newChain()
                    .current(() -> TaskChain.getCurrentChain().setTaskData("dataLayout", getDataLayout()))
                    .returnData("dataLayout");

            dataLayoutChain = dataLayoutChainFunction.apply(dataLayoutChain);

            dataLayoutChain.current(input -> {
                dataLayout = input;
                dataLayoutValid = true;

                //System.out.println("Received DataLayout "+input);
                applyDataLayout();
                dataLayoutChain = null;
                return null;
            });

            dataLayoutChain.execute();
        }*/
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

        if (openEventListener != null) {
            LOGGER.info("Overwriting open event listener");
        }

        this.openEventListener = EventListener.of(InventoryOpenEvent.class, this::handleOpen);
        return this;
    }

    /**
     * Set's the close function to the builder
     * @param closeFunction The function to set
     */
    public InventoryBuilder setCloseFunction(CloseFunction closeFunction) {
        this.closeFunction = closeFunction;

        if (closeEventListener != null) {
            LOGGER.info("Overwriting close event listener");
        }

        this.closeEventListener = EventListener.of(InventoryCloseEvent.class, this::handleClose);
        return this;
    }

    /**
     * Set a new instance of the {@link InventoryLayout} to the builder
     * @param inventoryLayout The layout to set
     */
    public InventoryBuilder setInventoryLayout(@NotNull InventoryLayout inventoryLayout) {
        this.inventoryLayout = inventoryLayout;
        return this;
    }

    /**
     * Returns the underlying {@link InventoryLayout}.
     * @return the given layout
     */
    @NotNull
    public InventoryLayout getInventoryLayout() {
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
