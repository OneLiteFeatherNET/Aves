package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

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
     * @param component The title for the inventory as {@link Component}
     * @param type The type for the inventory
     * @param player The player who owns the inventory
     */
    public PersonalInventoryBuilder(@NotNull Component component, @NotNull InventoryType type, @NotNull Player player) {
        super(component, type);
        this.player = player;
    }

    /**
     * Opens the inventory for the given {@link Player}.
     * The locale can not be null in this context because the class is a builder for translated inventories.
     */
    public void open() {
        player.openInventory(getInventory());
    }
}
