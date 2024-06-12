package de.icevizion.aves.inventory.holder;

import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a wrapper objects which holds an instance from an inventory
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
public interface InventoryHolder {

    /**
     * Returns the inventory from the holder
     * @return the underling inventory
     */
    @NotNull Inventory getInventory();
}
