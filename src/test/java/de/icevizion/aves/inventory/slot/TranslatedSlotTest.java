package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.item.TranslatedItem;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static de.icevizion.aves.inventory.util.InventoryConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class TranslatedSlotTest {

    @Test
    void testCopyConstructor() {
        var translatedSlot = new TranslatedSlot(TranslatedItem.empty(), CANCEL_CLICK);
        var copiedSlot = TranslatedSlot.of(translatedSlot);
        assertNotEquals(translatedSlot, copiedSlot);
    }

    @Test
    void testGetItemStack() {
        var translatedSlot = new TranslatedSlot(TranslatedItem.empty(), CANCEL_CLICK);
        assertThrowsExactly(
                UnsupportedOperationException.class,
                translatedSlot::getItem,
                "This TranslatedSlot needs a locale to retrieve the item"
        );
    }

    @Test
    void testSetItemStack() {
        var translatedSlot = new TranslatedSlot(TranslatedItem.empty(), CANCEL_CLICK);
        assertThrowsExactly(
                UnsupportedOperationException.class,
                () -> translatedSlot.setItemStack(ItemStack.AIR),
                "This TranslatedSlot needs a translated item"
        );
    }

    @Test
    void testGetItemStackWithLocale() {
        var translatedSlot = new TranslatedSlot(TranslatedItem.empty(), CANCEL_CLICK);
        var item = translatedSlot.getItem(Locale.CANADA);
        assertNotSame(Material.ACACIA_BOAT, item.material());
    }

    @Test
    void testSetTranslatedItem() {
        var translatedSlot = new TranslatedSlot(TranslatedItem.empty(), CANCEL_CLICK);
        translatedSlot.setTranslatedItem(TranslatedItem.of(Material.ITEM_FRAME));
        assertSame(Material.ITEM_FRAME, translatedSlot.getItem(Locale.CANADA).material());
    }

    @Test
    void testGetTranslatedItem() {
        var translatedSlot = new TranslatedSlot(TranslatedItem.empty(), CANCEL_CLICK);
        assertNotNull(translatedSlot.getTranslatedItem());
    }

    @Test
    void testToString() {
        assertNotNull(new TranslatedSlot(TranslatedItem.empty(), CANCEL_CLICK).toString());
    }
}