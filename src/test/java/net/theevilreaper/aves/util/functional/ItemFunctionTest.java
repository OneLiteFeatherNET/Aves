package net.theevilreaper.aves.util.functional;

import net.minestom.server.component.DataComponents;
import net.theevilreaper.aves.item.IItem;
import net.theevilreaper.aves.item.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemFunctionTest {

    @Test
    void testItemFunctionalInterface() {
        ItemFunction<IItem, Component> function  = item -> item == null ? null : item.get().get(DataComponents.CUSTOM_NAME);
        assertNull(function.apply(null));
        final IItem item = Item.of(ItemStack.builder(Material.ACACIA_FENCE).customName(Component.text("Test")).build());
        final Component displayName = function.apply(item);
        assertNotNull(displayName);
        assertEquals("Test", PlainTextComponentSerializer.plainText().serialize(displayName));
    }
}
