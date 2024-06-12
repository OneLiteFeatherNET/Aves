package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.function.DefaultApplyLayoutFunction;
import de.icevizion.aves.inventory.slot.TranslatedSlot;
import de.icevizion.aves.inventory.util.LayoutCalculator;
import de.icevizion.aves.item.TranslatedItem;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.Locale;

import static de.icevizion.aves.inventory.util.InventoryConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class InventoryLayoutTest {

    @Test
    void testCopyConstructor() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        layout.setItem(0, new TranslatedSlot(TranslatedItem.EMPTY));
        layout.setNonClickItems(LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW), ItemStack.builder(Material.ALLIUM).build());
        var copiedLayout = InventoryLayout.of(layout);

        assertEquals(layout, copiedLayout);
    }

    @Test
    void testSetApplyFunction() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        var newFunction = new DefaultApplyLayoutFunction(layout.getContents());

        layout.setApplyLayoutFunction(newFunction);
        assertNotNull(layout.getApplyLayoutFunction());
        assertSame(newFunction, layout.getApplyLayoutFunction());
    }

    @Test
    void testApplyMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_2_ROW);
        layout.setItem(0, ItemStack.builder(Material.BLACK_STAINED_GLASS).build());
        var itemStacks = new ItemStack[InventoryType.CHEST_1_ROW.getSize()];

        Arrays.fill(itemStacks, ItemStack.AIR);

        layout.applyLayout(itemStacks);
        assertSame(Material.BLACK_STAINED_GLASS, layout.getSlot(0).getItem().material());

        layout.setItem(1, ItemStack.builder(Material.ALLIUM).build());

        layout.applyLayout(itemStacks, Locale.CANADA);

        assertSame(Material.ALLIUM, layout.getSlot(1).getItem().material());
    }

    @Test
    void testSetItemMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_2_ROW);

        layout.setItem(0, new InventorySlot(ItemStack.builder(Material.BLACK_STAINED_GLASS)));
        layout.setItem(1, ItemStack.AIR);
        layout.setItem(2, ItemStack.builder(Material.ALLIUM));
        layout.setItem(3, ItemStack.builder(Material.ENDER_CHEST).build(), CANCEL_CLICK);
        layout.setItem(4, new InventorySlot(ItemStack.AIR), CANCEL_CLICK);

        assertNotNull(layout.getSlot(0));
        assertSame(Material.AIR, layout.getSlot(1).getItem().material());
        assertNotNull(layout.getSlot(3).getClick());
    }

    @Test
    void testSetItemsMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_5_ROW);
        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW), ItemStack.builder(Material.ENDER_CHEST), CANCEL_CLICK);
        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_2_ROW), ItemStack.AIR, CANCEL_CLICK);
        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_3_ROW), ItemStack.builder(Material.ALLIUM));
        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_4_ROW), ItemStack.builder(Material.LIGHT).build());
        var slot = new InventorySlot(ItemStack.AIR);
        layout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_5_ROW), slot);

        for (int i = InventoryType.CHEST_5_ROW.getSize() - 9; i < InventoryType.CHEST_5_ROW.getSize(); i++) {
            assertSame(slot, layout.getSlot(i));
        }

        for (int i = InventoryType.CHEST_3_ROW.getSize() - 9; i < InventoryType.CHEST_3_ROW.getSize(); i++) {
            assertNotSame(Material.LIGHT, layout.getSlot(i).getItem().material());
        }
    }

    @Test
    void testNonClickItemMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_2_ROW);

        layout.setNonClickItem(0, new InventorySlot(ItemStack.builder(Material.BLACK_STAINED_GLASS)));
        layout.setNonClickItem(1, ItemStack.builder(Material.DIAMOND).build());
        layout.setNonClickItem(3, ItemStack.builder(Material.CHEST).build());
        assertNotNull(layout.getSlot(0));
        assertNotNull(layout.getSlot(3));
        assertNotSame(Material.AIR, layout.getSlot(1).getItem().material());
    }

    @Test
    void testSetNonItemsMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_4_ROW);
        layout.setNonClickItems(LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW), ItemStack.builder(Material.ENDER_CHEST));
        layout.setNonClickItems(LayoutCalculator.fillRow(InventoryType.CHEST_2_ROW), ItemStack.AIR);
        layout.setNonClickItems(LayoutCalculator.fillRow(InventoryType.CHEST_3_ROW), ItemStack.builder(Material.ALLIUM));
        var slot = new InventorySlot(ItemStack.AIR);
        layout.setNonClickItems(LayoutCalculator.fillRow(InventoryType.CHEST_4_ROW), slot);

        for (int i = InventoryType.CHEST_4_ROW.getSize() - 9; i < InventoryType.CHEST_4_ROW.getSize(); i++) {
            assertSame(slot, layout.getSlot(i));
        }
    }

    @Test
    void testBlankSlotMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_2_ROW);

        layout.blank(0);
        layout.blank(LayoutCalculator.fillRow(InventoryType.CHEST_2_ROW));

        assertSame(EMPTY_SLOT, layout.getSlot(0));
        assertNotNull(layout.getSlot(10));
    }

    @Test
    void testClearSlotMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_2_ROW);

        layout.setItems(LayoutCalculator.repeat(0, InventoryType.CHEST_2_ROW.getSize() - 1), ItemStack.AIR);
        layout.clear(0);
        layout.clear(LayoutCalculator.repeat(5, 10));

        assertNull(layout.getSlot(0));
        assertNotNull(layout.getSlot(11));
    }

    @Test
    void testUpdateSlotMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_2_ROW);
        layout.setItem(1, ItemStack.AIR);

        layout.update(1, CANCEL_CLICK);
        layout.update(1, ItemStack.builder(Material.ALLIUM).build(), CANCEL_CLICK);

        assertNotNull(layout.getSlot(1));
        assertNotSame(Material.AIR, layout.getSlot(1).getItem().material());
        layout.update(1, ItemStack.builder(Material.CHEST).build());
        assertNotSame(Material.ALLIUM, layout.getSlot(1).getItem().material());
    }

    @Test
    void failGetSlot() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        assertThrowsExactly(IllegalArgumentException.class, () -> layout.getSlot(-1));
        assertThrowsExactly(IllegalArgumentException.class, () -> layout.getSlot(12));
    }

    @Test
    void failItemSet() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);

        catchError(IllegalArgumentException.class, () -> layout.setItem(-1, ItemStack.builder(Material.ALLIUM)));
        catchError(IllegalArgumentException.class, () -> layout.setItem(100, ItemStack.builder(Material.ALLIUM)));

        catchError(IllegalArgumentException.class, () -> layout.setItem(-1, ItemStack.AIR, CANCEL_CLICK));
        catchError(IllegalArgumentException.class, () -> layout.setItem(100, ItemStack.AIR, CANCEL_CLICK));

        var slot = new InventorySlot(ItemStack.AIR);
        catchError(IllegalArgumentException.class, () -> layout.setItem(-1, slot, CANCEL_CLICK));
        catchError(IllegalArgumentException.class, () -> layout.setItem(100, slot, CANCEL_CLICK));

        catchError(IllegalArgumentException.class, () -> layout.setItem(-1, slot));
        catchError(IllegalArgumentException.class, () -> layout.setItem(100, slot));

        catchError(IllegalArgumentException.class, () -> layout.setNonClickItem(-1, ItemStack.AIR));
        catchError(IllegalArgumentException.class, () -> layout.setNonClickItem(100, ItemStack.AIR));

        catchError(IllegalArgumentException.class, () -> layout.setNonClickItem(-1, slot));
        catchError(IllegalArgumentException.class, () -> layout.setNonClickItem(100, slot));
    }

    @Test
    void testFailBlankAndClearMethod() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);

        catchError(IllegalArgumentException.class, () -> layout.blank(-1));
        catchError(IllegalArgumentException.class, () -> layout.blank(100));

        catchError(IllegalArgumentException.class, () -> layout.clear(-1));
        catchError(IllegalArgumentException.class, () -> layout.clear(100));
    }

    @Test
    void testUpdateMethods() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);

        catchError(IllegalArgumentException.class, () -> layout.update(-1, CANCEL_CLICK));
        catchError(IllegalArgumentException.class, () -> layout.update(100, CANCEL_CLICK));

        catchError(IllegalArgumentException.class, () -> layout.update(-1, ItemStack.AIR, CANCEL_CLICK));
        catchError(IllegalArgumentException.class, () -> layout.update(100, ItemStack.AIR, CANCEL_CLICK));
    }

    @Test
    void testGetContents() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        assertNotNull(layout.getContents());
    }

    @Test
    void testGetSize() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        assertNotSame(InventoryType.CHEST_6_ROW.getSize(), layout.getSize());
    }

    @Test
    void testGetApplyFunction() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        assertNotNull(layout.getApplyLayoutFunction());
    }

    @Test
    void testToString() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        assertTrue(layout.toString().contains("null"));
    }

    @Test
    void testEqualsMethod() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        var anotherLayout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);

        assertEquals(layout, anotherLayout);
        assertNotEquals(layout, new InventoryLayoutImpl(InventoryType.CARTOGRAPHY));
    }

    @Test
    void testHashCode() {
        var layout = new InventoryLayoutImpl(InventoryType.CHEST_1_ROW);
        assertNotSame(1231, layout.hashCode());
    }

    private <T extends Throwable> T catchError(Class<T> clazz, Executable executable) {
        return assertThrowsExactly(clazz, executable, "The given slot index is out of range");
    }
}
