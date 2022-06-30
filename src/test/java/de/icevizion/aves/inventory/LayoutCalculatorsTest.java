package de.icevizion.aves.inventory;


import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import de.icevizion.aves.inventory.util.LayoutCalculator;
import net.minestom.server.inventory.InventoryType;
import org.junit.jupiter.api.Test;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class LayoutCalculatorsTest {

    @Test
    public void repeat() {
        var arr = LayoutCalculator.repeat(0, 3);
        var expected = new int[]{0,1,2};
        assertArrayEquals(expected, arr);
    }

    @Test
    public void quadSimple() {
        var arr = LayoutCalculator.quadOriginal(10, 20);
        var expected = new int[]{10, 11, 19, 20};

        assertArrayEquals(expected, arr);
    }

    @Test
    public void quadSimple2() {
        var arr = LayoutCalculator.quadOriginal(10, 21);
        var expected = new int[]{10, 11, 12, 19, 20, 21};

        assertArrayEquals(expected, arr);
    }

    @Test
    public void fillColumn() {
        var arr = LayoutCalculator.fillColumn(InventoryType.CHEST_4_ROW, 2);
        var expected = new int[]{2, 11, 20, 29};

        assertArrayEquals(expected, arr);
    }

    @Test
    public void frame() {
        var arr = LayoutCalculator.frame(10, 31);
        var expected = new int[] {10, 28, 11, 29, 12, 30, 13, 31, 19, 22};

        assertArrayEquals(expected, arr);
    }
}