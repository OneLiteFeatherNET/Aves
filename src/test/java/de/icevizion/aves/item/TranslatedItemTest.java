package de.icevizion.aves.item;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.message.CompoundMessageCache;
import at.rxcki.strigiformes.text.TextData;
import de.icevizion.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
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
    void testCreateOfMethodWithBuilder() {
        var item = TranslatedItem.of(ItemStack.builder(Material.ACACIA_BOAT));
        assertNotNull(item);
        assertSame(Material.ACACIA_BOAT, item.get(Locale.ENGLISH).material());
    }

    @Test
    void testCreateOfMethodWithBuilderAndMessageProvider() {
        var item = TranslatedItem.of(ItemStack.builder(Material.ALLIUM), Mockito.mock(MessageProvider.class));
        assertNotNull(item);
    }

    @Test
    void testTranslatedMethods() {
        var item = TranslatedItem.of(ItemStack.builder(Material.ALLIUM), Mockito.mock(MessageProvider.class));
        var mockedCache = Mockito.mock(CompoundMessageCache.class);
        item.setDisplayName(new TextData("displayName"));
        item.setLore(new TextData("lore"));
        assertThrows(IllegalStateException.class, () -> item.setLore(mockedCache));
    }

    @Test
    void testEmptySlotCreation() {
        var item = TranslatedItem.empty();
        assertNotNull(item);
    }

    @Test
    void testSetItemStackMethod() {
        var item = TranslatedItem.empty();
        item.setItemStack(ItemStack.builder(Material.ANDESITE_SLAB).build());
        assertNotSame(Material.AIR, item.get(Locale.ENGLISH).material());
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

    @Test
    void testEquals() {
        var firstSlot = TranslatedItem.of(ItemStack.AIR);
        var secondSlot = TranslatedItem.of(ItemStack.AIR);
        assertThrows(NullPointerException.class, () -> firstSlot.equals(secondSlot));
    }
}