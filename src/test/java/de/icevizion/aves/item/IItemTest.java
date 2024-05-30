package de.icevizion.aves.item;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class IItemTest {

    @Test
    void testNonTranslatedItem() {
        var item = new Item(ItemStack.AIR);
        assertSame(ItemStack.AIR, item.get());
        assertSame(ItemStack.AIR, item.get(Locale.ENGLISH));
    }

    @Test
    void testNonTranslatedItemWithMaterial() {
        var item = Item.of(Material.AIR);
        assertNotSame(IItem.AIR, item);
        assertNotSame(ItemStack.AIR, item.get());
    }

    @Test
    void testNonTranslatedItemWithBuilder() {
        var item = Item.of(ItemStack.builder(Material.AIR));
        assertNotSame(ItemStack.AIR, item.get());
    }
}