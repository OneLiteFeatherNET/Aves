package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import de.icevizion.aves.ServiceRegistry;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class InventoryBuilder implements Listener {

    private final InventoryRows rows;

    private InventoryLayout inventoryLayout;
    protected boolean inventoryLayoutValid = true;

    private InventoryLayout dataLayout;
    private TaskChain<InventoryLayout> dataLayoutChain;
    private Function<TaskChain<InventoryLayout>, TaskChain<InventoryLayout>> dataLayoutChainFunction;
    protected boolean dataLayoutValid = false;

    protected Function<InventoryCloseEvent, Boolean> closeListener;

    protected JavaPlugin plugin;

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

    // =========

    public void registerListener(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void invalidateInventoryLayout() {
        inventoryLayoutValid = false;

        if (isInventoryOpened()) {
            updateInventory();
        }
    }

    public void invalidateDataLayout() {
        if (dataLayoutChainFunction == null) {
            throw new IllegalStateException("Tried to invalidate DataLayout with no TaskChain configured");
        }

        if (isInventoryOpened()) {
            retrieveDataLayout();
            System.out.println("DataLayout invalidated on open inv, requested data...");
        } else {
            System.out.println("DataLayout invalidated on closed inv");
            dataLayoutValid = false;
        }
    }

    protected void handleClick(InventoryClickEvent event) {
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
            var closeInv = closeListener.apply(event);
            var holder = (Holder) event.getView().getTopInventory().getHolder();

            if (!closeInv) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> event.getPlayer().openInventory(holder.getInventory()), 3);
            }
        }
    }

    protected void updateInventory(Inventory inventory, String title, Locale locale, MessageProvider messageProvider, boolean applyLayout) {
        applyLayout |= !inventoryLayoutValid;

        if (!((Holder) inventory.getHolder()).getInventoryTitle().equals(title)) {
            plugin.getLogger().info("UpdateInventory is updating the title!");
            var contents = inventory.getContents();
            var viewers = inventory.getViewers();
            var holder = (Holder) inventory.getHolder();

            inventory = Bukkit.createInventory(holder, getRows().getSize(), title);
            holder.setInventory(inventory);
            holder.setInventoryTitle(title);
            inventory.setContents(contents);

            for (var viewer : viewers) {
                viewer.openInventory(inventory);
            }
        }

        if (applyLayout) {
            var contents = inventory.getContents();
            getInventoryLayout().applyLayout(contents, locale, messageProvider);
            inventory.setContents(contents);
            plugin.getLogger().info("UpdateInventory applied the InventoryLayout!");
        }

        synchronized (this) {
            if (!dataLayoutValid && dataLayoutChain == null) {
                retrieveDataLayout();
            } else {
                if (getDataLayout() != null) {
                    var contents = inventory.getContents();
                    getDataLayout().applyLayout(contents, locale, messageProvider);
                    inventory.setContents(contents);
                }
            }
        }
    }

    protected void retrieveDataLayout() {
        synchronized (this) {
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

                System.out.println("Received DataLayout "+input);
                applyDataLayout();
                dataLayoutChain = null;
                return null;
            });

            dataLayoutChain.execute();
        }
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

    public InventoryBuilder setDataLayoutChainFunction(Function<TaskChain<InventoryLayout>, TaskChain<InventoryLayout>> setupFunction) {
        dataLayoutChainFunction = setupFunction;

        return this;
    }
}
