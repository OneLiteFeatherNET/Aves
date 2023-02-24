package de.icevizion.aves.inventory.pageable;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The interface contains various implementations to open an inventory to a player.
 * What a methods do depends on the given use case. In some cases the method won't do anything
 * @version 1.0.0
 * @since 1.2.0
 */
public sealed interface OpenableInventory permits PageableInventory {

    int FIRST_PAGE = 1;

    /**
     * Opens the inventory for a specific player which is handled internally.
     */
    void open();

    /**
     * Opens the inventory at a given page index.
     * @param page the page index
     */
    void open(int page);

    /**
     * Opens the inventory for a given player.
     * This method opens the inventory at the first page
     * @param player the player who receives the inventory
     */
    default void open(@NotNull Player player) {
        this.open(player, FIRST_PAGE);
    }

    /**
     * Opens the inventory for a player at a given page
     * @param player the player who receives the inventory
     * @param page the page number
     */
    void open(@NotNull Player player, int page);
}
