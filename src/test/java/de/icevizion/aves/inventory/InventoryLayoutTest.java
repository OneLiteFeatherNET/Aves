package de.icevizion.aves.inventory;

import de.icevizion.aves.item.TranslatedItem;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryLayoutTest {

    final InventoryLayout layout = new InventoryLayout(InventoryType.CHEST_4_ROW);

    @BeforeAll
    void init() {
        layout.setItem(0, ItemStack.AIR);
    }

    @Order(1)
    @Test
    void testLayoutConstructor() {
        var layout = new InventoryLayout(InventoryType.CHEST_5_ROW);
        assertNotNull(layout);
        assertSame(InventoryType.CHEST_5_ROW.getSize(), layout.getContents().length);
    }

    @Order(2)
    @Test
    void testConstructorWithLayout() {
        var otherLayout = InventoryLayout.of(this.layout);
        assertNotNull(otherLayout);
        assertSame(this.layout.getContents().length, otherLayout.getContents().length);
        assertNotNull(otherLayout.getSlot(0));
    }

    @Order(3)
    @Test
    void testApplyLayoutWithEmptyAndZeroArray() {
        var testLayout = new InventoryLayout(InventoryType.CHEST_1_ROW);
        testLayout.applyLayout(new ItemStack[]{});
        assertNotNull(testLayout.getContents());
        testLayout.applyLayout(null);
        assertNull(testLayout.getSlot(0));
    }

    @Order(4)
    @Test
    void testApplyLayoutWithTranslatedSlotWithoutLocale() {
        var testLayout = new InventoryLayout(InventoryType.CHEST_1_ROW);
        testLayout.setItem(0, TranslatedItem.of(Material.AIR).toSlot());
        assertThrowsExactly(IllegalArgumentException.class,
                () ->testLayout.applyLayout(new ItemStack[]{ItemStack.AIR}, null, null)
                , "Tried to apply the InventoryLayout with an Translated slot and provided no locale!");
    }

    @Order(4)
    @Test
    void testSetItemWithBuilder() {
        this.layout.setItem(1, ItemStack.builder(Material.ACACIA_BOAT), InventoryLayout.CANCEL_CLICK);
        assertNotNull(this.layout.getSlot(1));
    }

    @Order(5)
    @Test
    void testSetItemWithSlot() {
        this.layout.setItem(2, new InventorySlot(ItemStack.AIR), InventoryLayout.CANCEL_CLICK);
        var slot = this.layout.getSlot(2);
        assertNotNull(slot);
        assertTrue(slot instanceof InventorySlot);
    }

    @Order(6)
    @Test
    void testSetItemWithoutAClick() {
        this.layout.setItem(3, ItemStack.AIR);
        var slot = this.layout.getSlot(3);
        assertNotNull(slot);
        assertNull(slot.getClick());
    }

    @Order(7)
    @Test
    void testBlankSlot() {
        this.layout.blank(23);
        assertNotNull(this.layout.getSlot(23));
    }

    @Order(8)
    @Test
    void testBlankSlots() {
        var blankSlots = new int[]{24,25,26,30};
        this.layout.blank(blankSlots);

        for (int i = 0; i < blankSlots.length; i++) {
            assertNotNull(this.layout.getSlot(blankSlots[i]));
        }
    }

    @Order(9)
    @Test
    void testClearSlot() {
        layout.clear(34);
        assertNull(this.layout.getSlot(34));
    }

    @Order(10)
    @Test
    void testClearSlots() {
        var slots = new int[]{1,2,3};
        this.layout.clear(slots);
        for (int i = 0; i < slots.length; i++) {
            assertNull(this.layout.getSlot(i));
        }
    }

    @Order(11)
    @Test
    void testUpdateWithIndex() {
        var newSlot = new InventorySlot(ItemStack.AIR, (player, clickType, slot1, condition) -> condition.setCancel(true));
        this.layout.setItem(10, newSlot);
        this.layout.update(10, InventoryLayout.CANCEL_CLICK);

        var slot = this.layout.getSlot(10);

        assertNotNull(slot);
        assertNotNull(slot.getClick());
        assertSame(InventoryLayout.CANCEL_CLICK, this.layout.getSlot(10).getClick());
    }

    /*@Order(7)
    @Test
    void testGetSlot() {
        var slot = layout.getSlot(0);
        assertNotNull(slot);
    }*/

    @Order(12)
    @Test
    void testGetSlotWithException() {
        assertThrows(IllegalArgumentException.class, () -> layout.getSlot(-1));
        assertThrows(IllegalArgumentException.class, () -> layout.getSlot(9999));
    }

    @Order(13)
    @Test
    void testToString() {
        var invLayout = new InventoryLayout(InventoryType.CHEST_1_ROW);
        assertEquals(
                "InventoryLayout{contents=[null, null, null, null, null, null, null, null, null]}",
                invLayout.toString());
    }

    @Order(14)
    @Test
    void testEqualsWithSameObjects() {
        assertEquals(this.layout, this.layout);
    }

    @Order(15)
    @Test
    void testEqualsWithDifferentReferences() {
        var otherLayout = new InventoryLayout(InventoryType.CHEST_4_ROW);
        assertNotEquals(this.layout, otherLayout);
    }

    @Order(16)
    @Test
    void testHashCode() {
        assertNotSame(1254, this.layout.hashCode());
    }
}