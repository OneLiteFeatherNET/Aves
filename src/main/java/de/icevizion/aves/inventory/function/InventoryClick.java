package de.icevizion.aves.inventory.function;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a click function which can be applied to an implementation from the {@link de.icevizion.aves.inventory.slot.ISlot} interface.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
@FunctionalInterface
public interface InventoryClick {

    /**
     * Handles what happen when a player clicks on an item in a inventory.
     * @param player the {@link Player} who is involved
     * @param clickType the given {@link ClickType}
     * @param slot the clicked slot as int
     * @param condition the given {@link net.minestom.server.inventory.condition.InventoryCondition}
     */
    void onClick(@NotNull Player player, @NotNull ClickType clickType, int slot, @NotNull InventoryConditionResult condition);
}
