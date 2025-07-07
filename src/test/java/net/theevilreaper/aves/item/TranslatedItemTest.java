package net.theevilreaper.aves.item;

import net.minestom.server.component.DataComponents;
import net.theevilreaper.aves.i18n.AvesTranslationRegistry;
import net.theevilreaper.aves.inventory.util.InventoryConstants;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.TranslationRegistry;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class TranslatedItemTest {

    @Test
    void testRendererAves() {
        var item = TranslatedItem.of(ItemStack.of(Material.ACACIA_BUTTON));
        item.setDisplayName("key", "Argument 1", "Argument 2");
        TranslationRegistry translationRegistry = TranslationRegistry.create(Key.key("test", "test"));
        translationRegistry.register("key", Locale.ENGLISH, new MessageFormat("<arg:0> <arg:1>"));
        GlobalTranslator.translator().addSource(new AvesTranslationRegistry(translationRegistry));
        Component displayName = item.get(Locale.ENGLISH).get(DataComponents.CUSTOM_NAME);
        assertNotNull(displayName);
        assertTrue(PlainTextComponentSerializer.plainText().serialize(displayName).equalsIgnoreCase("Argument 1 Argument 2"));
    }

    @Test
    void testRenderer() {
        var item = TranslatedItem.of(ItemStack.of(Material.ACACIA_BUTTON));
        item.setDisplayName("key", "Argument 1", "Argument 2");
        TranslationRegistry translationRegistry = TranslationRegistry.create(Key.key("test", "test"));
        translationRegistry.register("key", Locale.ENGLISH, new MessageFormat("{0} {1}"));
        GlobalTranslator.translator().addSource(translationRegistry);
        Component displayName = item.get(Locale.ENGLISH).get(DataComponents.CUSTOM_NAME);
        assertNotNull(displayName);
        assertTrue(PlainTextComponentSerializer.plainText().serialize(displayName).equalsIgnoreCase("Argument 1 Argument 2"));
    }

    @Test
    void testRendererLore() {
        var item = TranslatedItem.of(ItemStack.of(Material.ACACIA_BUTTON));
        item.setDisplayName("key", "Argument 1", "Argument 2");
        item.setLore("key", "Argument 1", "Argument 2");
        TranslationRegistry translationRegistry = TranslationRegistry.create(Key.key("test", "test"));
        translationRegistry.register("key", Locale.ENGLISH, new MessageFormat("{0} {1}"));
        GlobalTranslator.translator().addSource(translationRegistry);
        List<Component> lore = item.get(Locale.ENGLISH).get(DataComponents.LORE);
        assertNotNull(lore);
        assertFalse(lore.isEmpty());
        assertLinesMatch(lore.stream().map(PlainTextComponentSerializer.plainText()::serialize).toList(), List.of("Argument 1 Argument 2"));
    }

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
        var slot = TranslatedItem.of(Material.ACACIA_BOAT).toSlot((player, iSlot, click) -> null);
        assertNotSame(InventoryConstants.CANCEL_CLICK, slot.getClick());
    }

    @Test
    void testEquals() {
        var firstSlot = TranslatedItem.of(ItemStack.of(Material.ACACIA_SLAB));
        var secondSlot = TranslatedItem.of(ItemStack.of(Material.GLOW_SQUID_SPAWN_EGG));
        assertThrows(NullPointerException.class, () -> firstSlot.equals(secondSlot));
    }
}