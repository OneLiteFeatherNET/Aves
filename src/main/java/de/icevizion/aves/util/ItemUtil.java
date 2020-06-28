package de.icevizion.aves.util;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtil {

    public boolean isSameItem(ItemStack firstItem, ItemStack secondItem) {
        return firstItem != null && secondItem != null
                && firstItem.getType() == secondItem.getType()
                && firstItem.getItemMeta() != null && secondItem.getItemMeta() != null
                && firstItem.getItemMeta().getDisplayName().equals(secondItem.getItemMeta().getDisplayName());
    }

    public static int getAmountFromItem(Player player, ItemStack itemStack) {
        int amount = 0;
        if (player.getInventory().getContents().length != 0) {
            int i = 0;
            while (i < player.getInventory().getContents().length) {
                if (player.getInventory().getContents()[i].getType() == itemStack.getType()) {
                    amount += player.getInventory().getContents()[i].getAmount();
                }
                i++;
            }
        }

        return 0;
    }
}
