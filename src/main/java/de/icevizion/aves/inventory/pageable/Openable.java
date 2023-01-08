package de.icevizion.aves.inventory.pageable;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 *
 * @version 1.0.0
 * @since 1.2.0
 */
public sealed interface Openable permits PageableInventory {

    void open();

    /**
     * Opens the inventory for a given player.
     * This method opens the inventory at the first page
     * @param player the player who receives the inventory
     */
    default void open(@NotNull Player player) {
        this.open(player, 1);
    }

    /**
     * Opens the inventory for a player at a given page
     * @param player the player who receives the inventory
     * @param page the page number
     */
    void open(@NotNull Player player, int page);
}
