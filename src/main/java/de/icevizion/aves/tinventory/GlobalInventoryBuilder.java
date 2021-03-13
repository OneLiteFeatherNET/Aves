package de.icevizion.aves.tinventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GlobalInventoryBuilder extends InventoryBuilder {

    private String title;
    private Inventory inventory;
    private Holder holder;

    public GlobalInventoryBuilder(String title, InventoryRows rows, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        super(rows, dataLayoutProvider);
        this.title = title;

        holder = new Holder(this);
    }

    public GlobalInventoryBuilder(String title, int slots, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        super(slots, dataLayoutProvider);
        this.title = title;

        holder = new Holder(this);
    }

    @Override
    public Inventory getInventory(Locale ignored) {
        if (dataLayoutValid && inventoryLayoutValid && inventory != null)
            return inventory;
        updateInventory();

        return inventory;
    }

    @Override
    protected boolean isInventoryOpened() {
        return !inventory.getViewers().isEmpty();
    }

    @Override
    protected void updateInventory() {
        boolean applyLayout = !inventoryLayoutValid;
        if (inventory == null) {
            inventory = Bukkit.createInventory((holder = new Holder(this)), getRows().getSize(), title);
            holder.setInventory(inventory);
            holder.setInventoryTitle(title);
            applyLayout = true;
        }

        updateInventory(inventory, title, null, applyLayout);
    }

    @Override
    protected void applyDataLayout() {
        synchronized (getDataLayoutFuture()) {
            getDataLayout().applyLayout(inventory.getContents(), null);
        }
    }
}
