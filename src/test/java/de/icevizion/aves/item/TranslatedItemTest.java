package de.icevizion.aves.item;

import de.icevizion.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class TranslatedItemTest {

    @Test
    void testTranslatedItemWithStackReference() {
        var item = TranslatedItem.of(ItemStack.AIR);

        assertSame(ItemStack.AIR, item.get(Locale.ENGLISH));
        assertNull(item.getMessageProvider());
    }

    @Test
    void tesTranslatedItemGetWithoutLocale() {
        var item = TranslatedItem.of(Material.AIR);
        var exception = assertThrows(UnsupportedOperationException.class, item::get);
        assertEquals("Can not get item without a locale in a translated context", exception.getMessage());
    }

    @Test
    void testHashCode() {
        assertNotSame(12, TranslatedItem.of(Material.AIR).hashCode());
    }

    @Test
    void testToSlot() {
        var slot = TranslatedItem.of(ItemStack.AIR).toSlot();
        assertNull(slot.getClick());
    }

    @Test
    void testToNonClickSLot() {
        var slot = TranslatedItem.of(Material.ITEM_FRAME).toNonClickSlot();
        assertSame(InventoryConstants.CANCEL_CLICK, slot.getClick());
    }

    @Test
    void testToSlotWithClickListener() {
        var slot = TranslatedItem.of(Material.ACACIA_BOAT).toSlot((player, clickType, slot1, condition) -> {});
        assertNotSame(InventoryConstants.CANCEL_CLICK, slot.getClick());
    }
}