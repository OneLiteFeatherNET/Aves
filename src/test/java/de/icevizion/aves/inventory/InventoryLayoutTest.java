package de.icevizion.aves.inventory;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryLayoutTest {

    final InventoryLayout layout = new InventoryLayout(InventoryType.CHEST_4_ROW);

    @BeforeAll
    void init() {
        layout.setItem(0, ItemStack.AIR);
    }

    @Test
    void testLayoutConstructor() {
        var layout = new InventoryLayout(InventoryType.CHEST_5_ROW);
        assertNotNull(layout);
        assertSame(InventoryType.CHEST_5_ROW.getSize(), layout.getContents().length);
    }

    @Test
    void testClearSlot() {
        layout.clear(0);
        assertNull(this.layout.getSlot(0));
    }

    @Test
    void testClearSlots() {
        var slots = new int[]{1,2,3};
        this.layout.clear(slots);
        for (int i = 0; i < slots.length; i++) {
            assertNull(this.layout.getSlot(i));
        }
    }

    @Test
    void testGetSlot() {
        var slot = layout.getSlot(0);
        assertNotNull(slot);
        assertNotNull(slot.getItem());
    }

    @Test
    void testGetSlotWithException() {
        assertThrows(IllegalArgumentException.class, () -> layout.getSlot(-1));
        assertThrows(IllegalArgumentException.class, () -> layout.getSlot(9999));
    }

    @Test
    void testToString() {
        var invLayout = new InventoryLayout(InventoryType.CHEST_1_ROW);
        assertEquals(
                "InventoryLayout{contents=[null, null, null, null, null, null, null, null, null]}",
                invLayout.toString());
    }

    @Test
    void testEqualsWithSameObjects() {
        assertEquals(this.layout, this.layout);
    }

    @Test
    void testEqualsWithDifferentReferences() {
        var otherLayout = new InventoryLayout(InventoryType.CHEST_4_ROW);
        assertNotEquals(this.layout, otherLayout);
    }

    @Test
    void testHashCode() {
        assertNotSame(1254, this.layout.hashCode());
    }

}