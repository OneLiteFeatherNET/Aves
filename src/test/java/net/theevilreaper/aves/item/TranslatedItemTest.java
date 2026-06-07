package net.theevilreaper.aves.item;

import net.theevilreaper.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class TranslatedItemTest {

    @Test
    void testSetterMethods() {
        var item = TranslatedItem.of(ItemStack.of(Material.ACACIA_BUTTON));
        item.setDisplayName("key", "Argument 1", "Argument 2");
        item.setLore("loreKey", "Argument 1", "Argument 2");
        assertNotNull(item);

    }

    @Test
    void tesTranslatedItemGetWithoutLocale() {
        var item = TranslatedItem.of(Material.ACACIA_BOAT);
        var exception = assertThrows(UnsupportedOperationException.class, item::get);
        assertEquals("Can not get item without a locale in a translated context", exception.getMessage());
    }

    @Test
    void testCreateOfMethodWithBuilder() {
        var item = TranslatedItem.of(ItemStack.builder(Material.ACACIA_BOAT));
        assertNotNull(item);
        assertSame(Material.ACACIA_BOAT, item.get(Locale.ENGLISH).material());
    }

    @Test
    void testHashCode() {
        assertNotSame(12, TranslatedItem.of(Material.GOLDEN_APPLE).hashCode());
    }

    @Test
    void testToSlot() {
        var slot = TranslatedItem.of(ItemStack.of(Material.ACACIA_LEAVES)).toSlot();
        assertNull(slot.getClick());
    }

    @Test
    void testToNonClickSLot() {
        var slot = TranslatedItem.of(Material.ITEM_FRAME).toNonClickSlot();
        assertSame(InventoryConstants.CANCEL_CLICK, slot.getClick());
    }

    @Test
    void testToSlotWithClickListener() {
        var slot = TranslatedItem.of(Material.ACACIA_BOAT).toSlot((player, iSlot, click, stack,result) -> {});
        assertNotSame(InventoryConstants.CANCEL_CLICK, slot.getClick());
    }

    @Test
    void testEquals() {
        var firstSlot = TranslatedItem.of(ItemStack.of(Material.ACACIA_SLAB));
        var secondSlot = TranslatedItem.of(ItemStack.of(Material.GLOW_SQUID_SPAWN_EGG));
        assertThrows(NullPointerException.class, () -> firstSlot.equals(secondSlot));
    }
}