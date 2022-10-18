package de.icevizion.aves.inventory.holder;

import de.icevizion.aves.inventory.InventoryBuilder;
import net.minestom.server.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * This class is the implementation for the {@link InventoryHolder} interface.
 * With an inventory can be checked if a holder from an {@link Inventory} is similar to another holder instance.
 * That's a better way to check if a {@link de.icevizion.aves.inventory.function.InventoryClick} is for that inventory or for another one.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
public record InventoryHolderImpl(@NotNull InventoryBuilder inventoryBuilder) implements InventoryHolder {

    /**
     * Returns the {@link Inventory} which is bound a holder reference.
     * @return the given {@link Inventory}
     */
    @Override
    public @NotNull Inventory getInventory() {
        return inventoryBuilder.getInventory();
    }
}
