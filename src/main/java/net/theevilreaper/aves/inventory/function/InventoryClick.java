package net.theevilreaper.aves.inventory.function;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.inventory.click.ClickType;
import net.minestom.server.item.ItemStack;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.slot.ISlot;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a click function which can be applied to an implementation from the {@link ISlot} interface.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.3.0
 **/
@FunctionalInterface
public interface InventoryClick {

    /**
     * Handles what happen when a player clicks on an item in an inventory.
     *
     * @param player the {@link Player} who is involved
     * @param slot   the clicked slot as int
     * @param click  the given {@link ClickType}
     * @param stack  the {@link ItemStack} which was clicked
     * @param result a {@link Consumer} which will be called with a {@link ClickHolder} to handle the result of the click
     */
    void onClick(
            @NotNull Player player,
            int slot, @NotNull
            Click click,
            @NotNull ItemStack stack,
            @NotNull Consumer<@NotNull ClickHolder> result
    );
}
