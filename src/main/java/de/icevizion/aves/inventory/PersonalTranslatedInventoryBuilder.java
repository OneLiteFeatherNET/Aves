package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class PersonalTranslatedInventoryBuilder extends GlobalTranslatedInventoryBuilder {

    private final Player player;

    public PersonalTranslatedInventoryBuilder(InventoryRows rows, MessageProvider messageProvider, Function<InventoryLayout, InventoryLayout> dataLayoutProvider, Player player) {
        super(rows, messageProvider, dataLayoutProvider);
        this.player = player;
    }

    public PersonalTranslatedInventoryBuilder(int slots, MessageProvider messageProvider, Function<InventoryLayout, InventoryLayout> dataLayoutProvider, Player player) {
        super(slots, messageProvider, dataLayoutProvider);
        this.player = player;
    }

    public void openInventory(Locale locale) {
        player.openInventory(getInventory(locale));
    }
}
