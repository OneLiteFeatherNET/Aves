package net.theevilreaper.aves.inventory.holder;

import net.theevilreaper.aves.inventory.InventoryBuilder;
import net.minestom.server.inventory.Inventory;
import net.theevilreaper.aves.inventory.function.InventoryClick;
import org.jetbrains.annotations.NotNull;

/**
 * This class is the implementation for the {@link InventoryHolder} interface.
 * With an inventory can be checked if a holder from an {@link Inventory} is similar to another holder instance.
 * That's a better way to check if a {@link InventoryClick} is for that inventory or for another one.
 *
 * @param inventoryBuilder the {@link InventoryBuilder} which is bound to the holder
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
public record InventoryHolderImpl(@NotNull InventoryBuilder inventoryBuilder) implements InventoryHolder {

    /**
     * Returns the {@link Inventory} which is bound a holder reference.
     *
     * @return the given {@link Inventory}
     */
    @Override
    public @NotNull Inventory getInventory() {
        return inventoryBuilder.getInventory();
    }
}
