package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public abstract class InventoryBuilder implements Listener {

    private final InventoryRows rows;

    private InventoryLayout inventoryLayout;
    protected boolean inventoryLayoutValid = true;

    private InventoryLayout dataLayout;
    private final CompletableFuture<InventoryLayout> dataLayoutFuture;
    protected boolean dataLayoutValid = false, dataLayoutProcessing = false;

    protected JavaPlugin plugin;

    public InventoryBuilder(InventoryRows rows, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        this.rows = rows;
        this.inventoryLayout = new InventoryLayout(rows.getSize());

        this.dataLayoutFuture = new CompletableFuture<>();
        this.dataLayoutFuture.thenApplyAsync(dataLayoutProvider);
        this.dataLayoutFuture.thenAcceptAsync(inventoryLayout1 -> {
            synchronized (dataLayoutFuture) {
                dataLayout = inventoryLayout1;
                dataLayoutProcessing = false;
                dataLayoutValid = true;

                applyDataLayout();
            }
        });
    }

    public InventoryBuilder(int slots, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        if (slots > InventoryRows.SIX.getSize()) {
            throw new IllegalArgumentException("Maximum amount of slots for an inventory is 54!");
        }

        this.rows = InventoryRows.getRows(slots);
        this.inventoryLayout = new InventoryLayout(slots);

        this.dataLayoutFuture = new CompletableFuture<>();
        this.dataLayoutFuture.thenApplyAsync(dataLayoutProvider);
        this.dataLayoutFuture.thenAcceptAsync(inventoryLayout1 -> {
            synchronized (dataLayoutFuture) {
                dataLayout = inventoryLayout1;
                dataLayoutProcessing = false;
                dataLayoutValid = true;

                applyDataLayout();
            }
        });
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
        if (isInventoryOpened()) {
            synchronized (dataLayoutFuture) {
                if (dataLayoutProcessing) {
                    dataLayoutFuture.cancel(true);
                }
                dataLayoutFuture.complete(dataLayout);
                dataLayoutProcessing = true;
            }
        } else {
            dataLayoutValid = false;
        }
    }

    protected void updateInventory(Inventory inventory, String title, Locale locale, MessageProvider messageProvider, boolean applyLayout) {
        applyLayout |= !inventoryLayoutValid;

        if (((Holder) inventory.getHolder()).getInventoryTitle().equals(title)) {
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
        }

        synchronized (getDataLayoutFuture()) {
            if (!dataLayoutValid && !dataLayoutProcessing || getDataLayout() == null) {
                getDataLayoutFuture().complete(getDataLayout());
            } else {
                var contents = inventory.getContents();
                getDataLayout().applyLayout(contents, locale, messageProvider);
                inventory.setContents(contents);
            }
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

    public CompletableFuture<InventoryLayout> getDataLayoutFuture() {
        return dataLayoutFuture;
    }
}
