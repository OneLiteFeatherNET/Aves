package de.icevizion.aves.inventory;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
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
     * @param type The type for the inventory
     * @param player The player who owns the inventory
     */
    public PersonalTranslatedInventoryBuilder(@NotNull InventoryType type, @NotNull Player player) {
        super(type);
        this.player = player;
    }

    /**
     * Opens the inventory for the given {@link Player} based on the {@link Locale}.
     * The locale can not be null in this context because the class is a builder for translated inventories.
     * @param locale The locale to open the right inventory with the right language
     */
    public void open(@NotNull Locale locale) {
        player.openInventory(getInventory(locale));
    }
}
