package de.icevizion.aves.inventory;

import org.junit.Test;

import static org.junit.Assert.*;


/**
 * @author Patrick Zdarsky / Rxcki
 */
public class LayoutCalculatorsTest {

    @Test
    public void repeat() {
    }

    @Test
    public void quadSimple() {
        var arr = LayoutCalculators.quad(10, 20);
        var expected = new int[]{10, 11, 19, 20};

        assertArrayEquals(expected, arr);
    }

    @Test
    public void quadSimple2() {
        var arr = LayoutCalculators.quad(10, 21);
        var expected = new int[]{10, 11, 12, 19, 20, 21};

        assertArrayEquals(expected, arr);
    }

    @Test
    public void fillColumn() {
        var arr = LayoutCalculators.fillColumn(InventoryRows.FOUR, 2);
        var expected = new int[]{2, 11, 20, 29};

        assertArrayEquals(expected, arr);
    }

    @Test
    public void frame() {
        var arr = LayoutCalculators.frame(10, 31);
        var expected = new int[] {10, 28, 11, 29, 12, 30, 13, 31, 19, 22};

        assertArrayEquals(expected, arr);
    }
}