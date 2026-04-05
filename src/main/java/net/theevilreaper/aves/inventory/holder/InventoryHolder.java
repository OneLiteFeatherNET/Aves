package net.theevilreaper.aves.inventory.holder;

import net.minestom.server.inventory.Inventory;

/**
 * Represents a wrapper objects which holds an instance from an inventory
 *
 * @author theEvilReaper
 * @version 1.0.1
 * @since 1.2.0
 */
public interface InventoryHolder {

    /**
     * Returns the inventory from the holder
     *
     * @return the underling inventory
     */
    Inventory getInventory();
}
