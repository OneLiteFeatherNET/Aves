package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The builder can be used to build a translated {@link net.minestom.server.inventory.Inventory} for a single {@link Player}.
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0.1
 * @since 1.0.12
 */
public class PersonalTranslatedInventoryBuilder extends GlobalTranslatedInventoryBuilder {

    private final Player player;

    /**
     * Creates a new instance from the {@link PersonalTranslatedInventoryBuilder} with the given values.
     * @param rows The amount of rows for the {@link net.minestom.server.inventory.Inventory}
     * @param messageProvider A valid instance to a {@link MessageProvider}
     * @param player The player who owns the {@link net.minestom.server.inventory.Inventory}
     */
    public PersonalTranslatedInventoryBuilder(InventoryRow rows, MessageProvider messageProvider, @NotNull Player player) {
        super(rows, messageProvider);
        this.player = player;
    }

    /**
     * Creates a new instance from the {@link PersonalTranslatedInventoryBuilder} with the given values.
     * @param slots The amount of slots of the {@link net.minestom.server.inventory.Inventory}
     * @param messageProvider A valid instance to a {@link MessageProvider}
     * @param player The player who owns the {@link net.minestom.server.inventory.Inventory}
     */
    public PersonalTranslatedInventoryBuilder(int slots, MessageProvider messageProvider, @NotNull Player player) {
        super(slots, messageProvider);
        this.player = player;
    }

    /**
     * Opens the inventory for the given {@link Player} based on the {@link Locale}.
     * The locale can not be null in this context because the class is a builder for translated inventories.
     * @param locale The locale to open the right inventory with the right language
     */
    public void openInventory(@NotNull Locale locale) {
        player.openInventory(getInventory(locale));
    }
}
