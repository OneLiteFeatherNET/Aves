package de.icevizion.aves.inventory;

import net.minestom.server.entity.Player;

/**
 * The builder can be used to build a {@link net.minestom.server.inventory.Inventory} for a single {@link Player}.
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0.1
 * @since 1.0.12
 */
public class PersonalInventoryBuilder extends GlobalInventoryBuilder {

    private final Player player;

    /**
     * Creates a new instance from the {@link PersonalTranslatedInventoryBuilder} with the given values.
     * @param title The title for the {@link net.minestom.server.inventory.Inventory}
     * @param rows The amount of rows for the {@link net.minestom.server.inventory.Inventory}
     * @param player The player who owns the {@link net.minestom.server.inventory.Inventory}
     */
    public PersonalInventoryBuilder(String title, InventoryRow rows, Player player) {
        super(title, rows);
        this.player = player;
    }

    /**
     * Creates a new instance from the {@link PersonalTranslatedInventoryBuilder} with the given values.
     * @param title The title for the {@link net.minestom.server.inventory.Inventory}
     * @param slots The amount of slots of the {@link net.minestom.server.inventory.Inventory}
     * @param player The player who owns the {@link net.minestom.server.inventory.Inventory}
     */
    public PersonalInventoryBuilder(String title, int slots, Player player) {
        super(title, slots);
        this.player = player;
    }

    /**
     * Opens the inventory for the given {@link Player}.
     * The locale can not be null in this context because the class is a builder for translated inventories.
     */
    public void openInventory() {
        player.openInventory(getInventory());
    }
}
