package net.theevilreaper.aves.inventory.function;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.inventory.condition.InventoryConditionResult;
import net.theevilreaper.aves.inventory.slot.ISlot;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a click function which can be applied to an implementation from the {@link ISlot} interface.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
@FunctionalInterface
public interface InventoryClick {

    /**
     * Handles what happen when a player clicks on an item in a inventory.
     *
     * @param player    the {@link Player} who is involved
     * @param clickType the given {@link ClickType}
     * @param slot      the clicked slot as int
     * @param condition the given {@link net.minestom.server.inventory.condition.InventoryCondition}
     */
    void onClick(@NotNull Player player, int slot, @NotNull ClickType clickType, @NotNull InventoryConditionResult condition);
}
