package de.icevizion.aves.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    /**
     * Checks if two items are completely equal.
     * @param firstItem The first item to check
     * @param secondItem The second item to check
     * @return True when the items are equal otherwise false
     */

    public static boolean isSameItem(ItemStack firstItem, ItemStack secondItem) {
        return firstItem != null && secondItem != null
                && firstItem.getType() == secondItem.getType()
                && firstItem.getItemMeta() != null && secondItem.getItemMeta() != null
                && firstItem.getItemMeta().getDisplayName().equals(secondItem.getItemMeta().getDisplayName());
    }

    /**
     * Returns the number of a specific item that is in a player's inventory
     * @param player The player to be appointed by
     * @param item The item to check
     * @return The amount of the given item but returns with zero when the item does not occur in the inventory
     */

    public static int getAmountFromItem(Player player, ItemStack item) {
        int amount = 0;
        if (player.getInventory().getContents().length != 0) {
            int i = 0;
            while (i < player.getInventory().getContents().length) {
                if (player.getInventory().getContents()[i].getType() == item.getType()) {
                    amount += player.getInventory().getContents()[i].getAmount();
                }
                i++;
            }
        }

        return amount;
    }

    /**
     * Returns the remaining free space in the inventory of a given player.
     * @param player The player from which the remaining place should be determined
     * @param item For what?
     * @return The amount of free space
     */

    public static int getFreeSpace(Player player, ItemStack item) {
        int spaceCount = 0;

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack currentStack = player.getInventory().getItem(i);
            if (currentStack == null) {
                spaceCount += 64;
            }

            if (isSameItem(currentStack, item)) {
                spaceCount += currentStack.getMaxStackSize() - item.getAmount();
            }
        }

        return spaceCount;
    }
}