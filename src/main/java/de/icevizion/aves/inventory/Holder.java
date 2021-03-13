package de.icevizion.aves.inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class Holder implements InventoryHolder {
    private final InventoryBuilder inventoryBuilder;
    private String inventoryTitle;
    private Inventory inventory;

    public Holder(InventoryBuilder inventoryBuilder) {
        this.inventoryBuilder = inventoryBuilder;
    }

    public Holder(InventoryBuilder inventoryBuilder, Inventory inventory) {
        this.inventoryBuilder = inventoryBuilder;
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public InventoryBuilder getInventoryBuilder() {
        return inventoryBuilder;
    }

    Holder setInventory(Inventory inventory) {
        this.inventory = inventory;
        return this;
    }

    public String getInventoryTitle() {
        return inventoryTitle;
    }

    public void setInventoryTitle(String inventoryTitle) {
        this.inventoryTitle = inventoryTitle;
    }
}
