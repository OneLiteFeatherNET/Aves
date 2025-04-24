package net.theevilreaper.aves.inventory.util;

import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

/**
 * @author Patrick Zdarsky / Rxcki
 */
class LayoutCalculatorsTest {

    @Test
    void testFrom() {
        var testFrom = LayoutCalculator.from(1, 2, 3, 4, 5);

        for (int i = 0; i < 5; i++) {
            assertEquals(i + 1, testFrom[i]);
        }
    }

    @Test
    void repeat() {
        var arr = LayoutCalculator.repeat(0, 3);
        var expected = new int[]{0, 1, 2};
        assertArrayEquals(expected, arr);
    }

    @Test
    void failRepeat() {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> LayoutCalculator.repeat(0, -1),
                "fromSlot cannot be higher that toSlot!");
    }

    @Test
    void quadSimple() {
        var arr = LayoutCalculator.quad(10, 20);
        var expected = new int[]{10, 11, 19, 20};

        assertArrayEquals(expected, arr);
    }

    @Test
    void quadSimple2() {
        var arr = LayoutCalculator.quad(10, 21);
        var expected = new int[]{10, 11, 12, 19, 20, 21};

        assertArrayEquals(expected, arr);
    }

    @Test
    void frame() {
        var arr = LayoutCalculator.frame(10, 31);
        var expected = new int[]{10, 28, 11, 29, 12, 30, 13, 31, 19, 22};

        assertArrayEquals(expected, arr);
    }

    @Test
    void failFrame() {
        assertThrowsExactly(IllegalArgumentException.class, () -> LayoutCalculator.frame(10, 10), "The values are the same");
    }

    @Test
    void testFillRow() {
        var array = LayoutCalculator.fillRow(InventoryType.CHEST_1_ROW);
        var expected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(expected, array);
    }

    @Test
    void fillColumn() {
        var arr = LayoutCalculator.fillColumn(InventoryType.CHEST_4_ROW, 2);
        var expected = new int[]{2, 11, 20, 29};

        assertArrayEquals(expected, arr);
    }

    @Test
    void failFillColumnOnColumnParamToSmall() {
        assertThrowsExactly(IllegalArgumentException.class, () -> LayoutCalculator.fillColumn(InventoryType.CHEST_5_ROW, -1), "Column cant be less than 0 or more than 8");
    }

    @Test
    void failFillColumnOnColumnParamToHigh() {
        assertThrowsExactly(IllegalArgumentException.class, () -> LayoutCalculator.fillColumn(InventoryType.CHEST_5_ROW, 10), "Column cant be less than 0 or more than 8");
    }

    @Test
    void testGetRowCount() {
        assertSame(1, LayoutCalculator.getRowCount(InventoryType.BEACON));
        assertSame(1, LayoutCalculator.getRowCount(InventoryType.CARTOGRAPHY));
    }
}