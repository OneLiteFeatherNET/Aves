package de.icevizion.aves.util.functional;

import de.icevizion.aves.item.IItem;
import de.icevizion.aves.util.Players;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * With the interface a developer can manipulate how an item would set into the inventory of a player.
 * One example for this ist in {@link Players#updateEquipment(Player, IItem[], IItem[], int...)}
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@FunctionalInterface
public interface ItemPlacer {

    /**
     * Default implementation for the interface.
     */
    ItemPlacer FALLBACK = (player, slotID, itemStack, armor) -> {
        if (armor) {
            switch (slotID) {
                case 0 -> player.getInventory().setHelmet(itemStack);
                case 1 -> player.getInventory().setChestplate(itemStack);
                case 2 -> player.getInventory().setLeggings(itemStack);
                case 3 -> player.getInventory().setBoots(itemStack);
            }
        } else {
            player.getInventory().setItemStack(slotID, itemStack);
        }
    };

    /**
     * Set's a given {@link ItemStack} to the inventory from a player
     * @param player The player who receives the itemStack
     * @param slotID The slotId for the itemStack
     * @param itemStack The itemStack itself
     */
    default void setItem(@NotNull Player player, int slotID, @NotNull ItemStack itemStack) {
        this.setItem(player, slotID, itemStack, false);
    }

    /**
     * Set's a given {@link ItemStack} to the inventory from a player
     * @param player The player who receives the itemStack
     * @param slotID The slotId for the itemStack
     * @param itemStack The itemStack itself
     * @param armor If the item is an armor item or not
     */
    void setItem(@NotNull Player player, int slotID, @NotNull ItemStack itemStack, boolean armor);
}
