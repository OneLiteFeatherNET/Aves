package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomInventoryTest {

    @Test
    void testConstructorWithComponent() {
        var customInventory = new CustomInventory(null, InventoryType.CHEST_4_ROW, Component.empty());
        assertNotNull(customInventory);
        assertNull(customInventory.getHolder());
    }

    @Test
    void testConstructorWithTitle() {
        var customInventory = new CustomInventory(null, InventoryType.CHEST_4_ROW, Component.text("Title"));
        assertNotNull(customInventory);
        assertNull(customInventory.getHolder());
        assertEquals("Title", PlainTextComponentSerializer.plainText().serialize(customInventory.getTitle()));
    }

}