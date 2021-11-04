package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.utils.time.TimeUnit;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class InventoryBuilder {

    private static final SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
    private final InventoryRows rows;

    private InventoryLayout inventoryLayout;
    protected boolean inventoryLayoutValid = true;

    private InventoryLayout dataLayout;
    protected boolean dataLayoutValid = false;

    protected Function<InventoryCloseEvent, Boolean> closeListener;
    protected Function<InventoryOpenEvent, Boolean> openFunction;


    public InventoryBuilder(InventoryRows rows) {
        this.rows = rows;
        this.inventoryLayout = new InventoryLayout(rows.getSize());
    }

    public InventoryBuilder(int slots) {
        if (slots > InventoryRows.SIX.getSize()) {
            throw new IllegalArgumentException("Maximum amount of slots for an inventory is 54!");
        }

        this.rows = InventoryRows.getRows(slots);
        this.inventoryLayout = new InventoryLayout(slots);
    }

    //Abstract methods

    public Inventory getInventory() {
        return getInventory(null);
    }

    public abstract Inventory getInventory(Locale locale);

    protected abstract boolean isInventoryOpened();

    protected abstract void updateInventory();

    protected abstract void applyDataLayout();

    public void invalidateInventoryLayout() {
        inventoryLayoutValid = false;

        if (isInventoryOpened()) {
            updateInventory();
        }
    }

    public void invalidateDataLayout() {
      /*  if (dataLayoutChainFunction == null) {
            throw new IllegalStateException("Tried to invalidate DataLayout with no TaskChain configured");
        }*/

        if (isInventoryOpened()) {
            retrieveDataLayout();
            System.out.println("DataLayout invalidated on open inv, requested data...");
        } else {
            System.out.println("DataLayout invalidated on closed inv");
            dataLayoutValid = false;
        }
    }

    protected void handleClick(InventoryPreClickEvent event) {
        if (event.getSlot() < 0 || event.getSlot() > getRows().getSize()-1)
            return; //Not within this inventory

        if (getDataLayout() != null) {
            var dataSlot = getDataLayout().getContents()[event.getSlot()];
            if (dataSlot != null && dataSlot.getClickListener() != null) {
                dataSlot.getClickListener().accept(event);
                return;
            }
        }

        if (getInventoryLayout() != null) {
            var layoutSlot = getInventoryLayout().getContents()[event.getSlot()];
            if (layoutSlot != null && layoutSlot.getClickListener() != null) {
                layoutSlot.getClickListener().accept(event);
            }
        }
    }

    protected void handleClose(InventoryCloseEvent event) {
        if (closeListener != null) {
            if (!closeListener.apply(event)) {
                schedulerManager.buildTask(()
                        -> event.setNewInventory(getInventory())).delay(150, TimeUnit.MILLISECOND).schedule();
            }
        }
    }

    protected void updateInventory(Inventory inventory, String title, Locale locale, MessageProvider messageProvider, boolean applyLayout) {
        applyLayout |= !inventoryLayoutValid;

        var titleComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(title);

        if (!inventory.getTitle().contains(titleComponent)) {
            System.out.println("UpdateInventory is updating the title");
            inventory.setTitle(titleComponent);
        }

        if (applyLayout) {
            var contents = inventory.getItemStacks();
            getInventoryLayout().applyLayout(contents, locale, messageProvider);
            for (int i = 0; i < contents.length; i++) {
                if (contents[i].getMaterial() == Material.AIR) continue;
                inventory.setItemStack(i, contents[i]);
            }
            System.out.println("UpdateInventory applied the InventoryLayout!");
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

    //Getters and Setters
    public InventoryRows getRows() {
        return rows;
    }

    public InventoryLayout getInventoryLayout() {
        return inventoryLayout;
    }

    public InventoryBuilder setInventoryLayout(InventoryLayout inventoryLayout) {
        this.inventoryLayout = inventoryLayout;
        return this;
    }

    public InventoryLayout getDataLayout() {
        return dataLayout;
    }

    public InventoryBuilder setCloseListener(Function<InventoryCloseEvent, Boolean> closeListener) {
        this.closeListener = closeListener;

        return this;
    }

    public InventoryBuilder setOpenFunction(Function<InventoryOpenEvent, Boolean> openFunction) {
        this.openFunction = openFunction;
        return this;
    }
}
