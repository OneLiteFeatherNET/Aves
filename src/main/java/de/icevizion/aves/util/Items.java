package de.icevizion.aves.util;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

/**
 * The class contains some methods to work with {@link ItemStack}.
 * @author theEvilReaper
 * @since 1.0.6
 * @version 1.0.0
 */
public class Items {

    public static int MAX_STACK_SIZE = 64;

    /**
     * Returns the number of a specific item that is in a player's inventory
     * @param player The player to be appointed by
     * @param item The item to check
     * @return The amount of the given item but returns with zero when the item does not occur in the inventory
     */
    public static int getAmountFromItem(Player player, ItemStack item) {
        int amount = 0;
        if (player.getInventory().getItemStacks().length != 0) {
            int i = 0;
            while (i < player.getInventory().getItemStacks().length) {
                if (player.getInventory().getItemStacks()[i].isSimilar(item)) {
                    amount += player.getInventory().getItemStacks()[i].getAmount();
                }
                i++;
            }
        }
        return amount;
    }

    /**
     * Returns the remaining free space in the inventory of a given player.
     * @param player The player from which the remaining place should be determined
     * @param item The item to check
     * @return The amount of free space
     */
    public static int getFreeSpace(Player player, ItemStack item) {
        int spaceCount = 0;

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack currentStack = player.getInventory().getItemStacks()[i];
            if (currentStack == null) {
                spaceCount += MAX_STACK_SIZE;
                continue;
            }
            if (currentStack.isSimilar(item)) {
                spaceCount += currentStack.getAmount() - item.getAmount();
            }
        }
        return spaceCount;
    }
}