package net.theevilreaper.aves.util;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;

/**
 * Utility class providing helper methods to work with {@link ItemStack} and player inventories.
 *
 * @author theEvilReaper
 * @version 1.2.0
 * @since 1.0.6
 */
public final class Items {

    public static final int MAX_STACK_SIZE = 64;

    private Items() {
    }

    /**
     * Returns the number of a specific item in a player's inventory.
     * <p>
     * Item similarity is checked via {@link ItemStack#isSimilar(ItemStack)}.
     *
     * @param player whose inventory to inspect
     * @param item   to count
     * @return the total amount of matching items found, or 0 if none exist
     */
    public static int getAmountFromItem(Player player, ItemStack item) {
        int amount = 0;
        ItemStack[] itemStacks = player.getInventory().getItemStacks();
        for (ItemStack currentStack : itemStacks) {
            if (currentStack.isSimilar(item)) {
                amount += currentStack.amount();
            }
        }
        return amount;
    }

    /**
     * Returns the remaining free space in a player's inventory.
     * <p>
     * Empty slots count as having 64 free slots. Non-empty slots calculate free space based on
     * the item's specific {@link ItemStack#maxStackSize()}.
     *
     * @param player whose inventory capacity to determine
     * @return the total remaining item space available across all slots
     */
    public static int getFreeSpace(Player player) {
        int spaceCount = 0;
        ItemStack[] itemStacks = player.getInventory().getItemStacks();
        int inventorySize = player.getInventory().getSize();

        for (int i = 0; i < inventorySize && i < itemStacks.length; i++) {
            ItemStack currentStack = itemStacks[i];
            if (currentStack.isAir()) {
                spaceCount += MAX_STACK_SIZE;
                continue;
            }

            int maxStackSize = currentStack.maxStackSize();
            spaceCount += Math.max(0, maxStackSize - currentStack.amount());
        }
        return spaceCount;
    }
}