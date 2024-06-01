package de.icevizion.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText;
import static org.junit.jupiter.api.Assertions.*;

@EnvTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryBuilderTest {

    final Component title = Component.text("Title");

    final InventoryType type = InventoryType.CHEST_3_ROW;

    @Test
    void testInventoryUpdateWhichRaisesAnException(Env env) {
        var builder = new GlobalInventoryBuilder(title, type);
        var exception = assertThrows(IllegalStateException.class, builder::updateInventory);
        assertEquals("Can't update content because the layout and datalayout is null", exception.getMessage());
    }

    @Test
    void testTitleUpdate(Env env) {
        var builder = new GlobalInventoryBuilder(title, type);
        builder.setLayout(InventoryLayout.fromType(type));
        var currentTitle = plainText().serialize(builder.getInventory().getTitle());
        builder.setTitleComponent(Component.text("Test 2"));

        var newTitle = plainText().serialize(builder.getInventory().getTitle());

        assertNotEquals(currentTitle, newTitle);
    }
}
