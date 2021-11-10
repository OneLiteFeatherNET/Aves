package de.icevizion.aves.util.functional;

import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

@FunctionalInterface
public interface ItemPlacer {

    ItemPlacer DEFAULT = (player, slotID, itemStack, armor) -> {
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

    default void setItem(@NotNull Player player, int slotId, @NotNull ItemStack itemStack) {
        this.setItem(player, slotId, itemStack, false);
    }

    void setItem(@NotNull Player player, int slotID, @NotNull ItemStack itemStack, boolean armor);

}
