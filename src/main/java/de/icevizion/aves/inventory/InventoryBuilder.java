package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import de.icevizion.aves.inventory.function.CloseFunction;
import de.icevizion.aves.inventory.function.OpenFunction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;
import net.minestom.server.timer.SchedulerManager;
import net.minestom.server.utils.time.TimeUnit;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class InventoryBuilder implements SizeChecker{

    private static final SchedulerManager schedulerManager = MinecraftServer.getSchedulerManager();
    private final InventoryRows rows;

    private InventoryLayout inventoryLayout;
    protected boolean inventoryLayoutValid = true;

    private InventoryLayout dataLayout;
    protected boolean dataLayoutValid = false;

    protected OpenFunction openFunction;
    protected CloseFunction closeFunction;

    public InventoryBuilder(InventoryRows rows) {
        checkSize(rows.getSize());
        this.rows = rows;
        this.inventoryLayout = new InventoryLayout(rows.getSize());
    }

    public InventoryBuilder(int slots) {
        checkSize(slots);

        this.rows = InventoryRows.getRows(slots);
        this.inventoryLayout = new InventoryLayout(slots);
    }

    public void registerGlobally() {
        MinecraftServer.getGlobalEventHandler().addListener(InventoryPreClickEvent.class, this::handleClick);
    }

    //Abstract methods

    public Inventory getInventory() {
        return getInventory(null);
    }

    public abstract Inventory getInventory(Locale locale);

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
      /*  if (dataLayoutChainFunction == null) {
            throw new IllegalStateException("Tried to invalidate DataLayout with no TaskChain configured");
        }*/

        if (isInventoryOpen()) {
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
        if (closeFunction != null) {
            if (!closeFunction.onClose(event)) {
                schedulerManager.buildTask(()
                        -> event.setNewInventory(getInventory())).delay(150, TimeUnit.MILLISECOND).schedule();
            }
        }
    }

    protected void updateInventory(Inventory inventory, String title, Locale locale, MessageProvider messageProvider, boolean applyLayout) {
        applyLayout |= !inventoryLayoutValid;

        var titleComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(title);

        if (!Component.EQUALS.test(inventory.getTitle(), titleComponent)) {
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

    public InventoryBuilder setInventoryLayout(InventoryLayout inventoryLayout) {
        this.inventoryLayout = inventoryLayout;
        return this;
    }

    public InventoryLayout getInventoryLayout() {
        return inventoryLayout;
    }

    public InventoryLayout getDataLayout() {
        return dataLayout;
    }
}
