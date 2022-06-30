package de.icevizion.aves.inventory.holder;

import de.icevizion.aves.inventory.InventoryBuilder;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
public record InventoryHolderImpl(@NotNull InventoryBuilder inventoryBuilder) implements InventoryHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return inventoryBuilder.getInventory();
    }
}
