package net.theevilreaper.aves.util.functional;

import net.theevilreaper.aves.item.IItem;
import net.theevilreaper.aves.item.TranslatedItem;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * With the interface a developer can manipulate how an item would set into the inventory of a player.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@FunctionalInterface
public interface ItemPlacer {

    /**
     * Default implementation for the interface.
     */
    ItemPlacer FALLBACK = (player, slotID, iitem, locale, armor) -> {
        var itemStack = iitem instanceof TranslatedItem && locale != null ? iitem.get(locale) : iitem.get();
        if (armor) {
            switch (slotID) {
                case 0 -> player.getInventory().setHelmet(itemStack);
                case 1 -> player.getInventory().setChestplate(itemStack);
                case 2 -> player.getInventory().setLeggings(itemStack);
                case 3 -> player.getInventory().setBoots(itemStack);
                default -> throw new IllegalArgumentException("The slotID is greater than four");
            }
        } else {
            player.getInventory().setItemStack(slotID, itemStack);
        }
    };

    /**
     * Set's a given {@link ItemStack} to the inventory from a player
     * @param player The player who receives the itemStack
     * @param slotID The slotId for the itemStack
     * @param item The itemStack itself
     */
    default void setItem(@NotNull Player player, int slotID, @NotNull IItem item, Locale locale) {
        this.setItem(player, slotID, item, locale, false);
    }

    /**
     * Set's a given {@link ItemStack} to the inventory from a player
     * @param player The player who receives the itemStack
     * @param slotID The slotId for the itemStack
     * @param item The itemStack itself
     * @param armor If the item is an armor item or not
     */
    void setItem(@NotNull Player player, int slotID, @NotNull IItem item, Locale locale, boolean armor);
}
