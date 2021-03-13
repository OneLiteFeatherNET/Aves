package de.icevizion.aves.tinventory;

import org.bukkit.entity.Player;

import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class PersonalInventoryBuilder extends GlobalInventoryBuilder {

    private final Player player;

    public PersonalInventoryBuilder(String title, InventoryRows rows, Player player, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        super(title, rows, dataLayoutProvider);
        this.player = player;
    }

    public PersonalInventoryBuilder(String title, int slots, Player player, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        super(title, slots, dataLayoutProvider);
        this.player = player;
    }

    public void openInventory() {
        player.openInventory(getInventory());
    }
}
