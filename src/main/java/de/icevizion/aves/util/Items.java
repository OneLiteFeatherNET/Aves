package de.icevizion.aves.util;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The class contains some methods to work with {@link ItemStack}.
 * @author theEvilReaper
 * @since 1.0.6
 * @version 1.0.0
 */
@ApiStatus.NonExtendable
public final class Items {

    public static final int MAX_STACK_SIZE = 64;

    private Items() {}

    /**
     * Returns the number of a specific item that is in a player's inventory
     * @param player The player to be appointed by
     * @param item The item to check
     * @return The amount of the given item but returns with zero when the item does not occur in the inventory
     */
    public static int getAmountFromItem(@NotNull Player player, @NotNull ItemStack item) {
        int amount = 0;
        if (player.getInventory().getItemStacks().length != 0) {
            for (int i = 0; i < player.getInventory().getItemStacks().length; i++) {
                if (player.getInventory().getItemStacks()[i].isSimilar(item)) {
                    amount += player.getInventory().getItemStacks()[i].amount();
                }
            }
        }
        return amount;
    }

    /**
     * Returns the remaining free space in the inventory of a given player.
     * @param player The player from which the remaining place should be determined
     * @return The amount of free space
     */
    public static int getFreeSpace(@NotNull Player player) {
        int spaceCount = 0;
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            var currentStack = player.getInventory().getItemStacks()[i];
            if (currentStack == null || currentStack.material() == ItemStack.AIR.material()) {
                spaceCount += MAX_STACK_SIZE;
                continue;
            }

            spaceCount += MAX_STACK_SIZE - currentStack.amount();
        }
        return spaceCount;
    }
}