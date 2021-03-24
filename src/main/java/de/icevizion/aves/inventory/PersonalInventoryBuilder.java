package de.icevizion.aves.inventory;

import org.bukkit.entity.Player;

import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class PersonalInventoryBuilder extends GlobalInventoryBuilder {

    private final Player player;

    public PersonalInventoryBuilder(String title, InventoryRows rows, Player player) {
        super(title, rows);
        this.player = player;
    }

    public PersonalInventoryBuilder(String title, int slots, Player player) {
        super(title, slots);
        this.player = player;
    }

    public void openInventory() {
        player.openInventory(getInventory());
    }
}
