package net.theevilreaper.aves.util.functional;

import net.theevilreaper.aves.item.IItem;
import net.theevilreaper.aves.item.TranslatedItem;
import net.minestom.server.entity.EquipmentSlot;
import net.minestom.server.entity.Player;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * With the interface a developer can manipulate how an item would set into the inventory of a player.
 *
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
                case 0 ->
                        player.getInventory().setEquipment(EquipmentSlot.HELMET, ((byte) EquipmentSlot.HELMET.armorSlot()), itemStack);
                case 1 ->
                        player.getInventory().setEquipment(EquipmentSlot.CHESTPLATE, ((byte) EquipmentSlot.CHESTPLATE.armorSlot()), itemStack);
                case 2 ->
                        player.getInventory().setEquipment(EquipmentSlot.LEGGINGS, ((byte) EquipmentSlot.LEGGINGS.armorSlot()), itemStack);
                case 3 ->
                        player.getInventory().setEquipment(EquipmentSlot.BOOTS, ((byte) EquipmentSlot.BOOTS.armorSlot()), itemStack);
                default -> throw new IllegalArgumentException("The slotID is greater than four");
            }
        } else {
            player.getInventory().setItemStack(slotID, itemStack);
        }
    };

    /**
     * Set's a given {@link ItemStack} to the inventory from a player
     *
     * @param player the player who receives the itemStack
     * @param slotID he slotId for the itemStack
     * @param item   the itemStack itself
     * @param locale the locale for the itemStack, if the itemStack is a {@link TranslatedItem} it will be translated to the given locale
     */
    default void setItem(Player player, int slotID, IItem item, Locale locale) {
        this.setItem(player, slotID, item, locale, false);
    }

    /**
     * Set's a given {@link ItemStack} to the inventory from a player
     *
     * @param player the player who receives the itemStack
     * @param slotID the slotId for the itemStack
     * @param item   the itemStack itself
     * @param locale the locale for the itemStack, if the itemStack is a {@link TranslatedItem} it will be translated to the given locale
     * @param armor  if the item is an armor item or not
     */
    void setItem(Player player, int slotID, IItem item, @Nullable Locale locale, boolean armor);
}
