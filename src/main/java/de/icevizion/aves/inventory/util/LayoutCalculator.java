package de.icevizion.aves.inventory.util;

import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class LayoutCalculator {

    private static final int INVENTORY_WIDTH = 9;

    public static int[] repeat(int fromSlot, int toSlot) {
        if (fromSlot > toSlot) {
            throw new IllegalArgumentException("fromSlot cannot be higher that toSlot!");
        }

        var arr = new int[toSlot-fromSlot];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = fromSlot + i;
        }

        return arr;
    }

    public static int[] quad(int firstCornerSlot, int lastCornerSlot) {
        var x1 = firstCornerSlot % INVENTORY_WIDTH;
        var y1 = Math.floor(firstCornerSlot / (double)INVENTORY_WIDTH);

        var x2 = lastCornerSlot % INVENTORY_WIDTH;
        var y2 = Math.floor(lastCornerSlot / (double)INVENTORY_WIDTH);

        var width = (x2-x1) + 1;
        var height = (y2-y1) + 1;

        var arr = new int[(int) Math.floor(width * height)];

        for (int i = 0; i < arr.length; i++) {
            var xSquare = i % width;
            var ySquare = Math.floor(i / (double)width);

            var x = x1 + xSquare;
            var y = y1 + ySquare;

            arr[i] = (int) Math.floor(y * INVENTORY_WIDTH) + x;
        }

        return arr;
    }

    public static int[] frame(int firstCornerSlot, int lastCornerSlot) {
        var x1 = firstCornerSlot % INVENTORY_WIDTH;
        var y1 = Math.floor(firstCornerSlot / (double)INVENTORY_WIDTH);

        var x2 = lastCornerSlot % INVENTORY_WIDTH;
        var y2 = Math.floor(lastCornerSlot / (double)INVENTORY_WIDTH);

        var width = (x2-x1) + 1;
        var height = (y2-y1) + 1;

        var arr = new int[(int) ((width-2)*2 + (height-2)*2 + 4)];
        var index = 0;

        for (int i = 0; i < width; i++) {
            arr[index++] = (int) y1 * INVENTORY_WIDTH + x1 + i;
            arr[index++] = (int) y2 * INVENTORY_WIDTH + x1 + i;
        }
        for (int i = 0; i < height-2; i++) {
            arr[index++] = (int) (y1+1+i) * INVENTORY_WIDTH + x1;
            arr[index++] = (int) (y1+1+i)*  INVENTORY_WIDTH + x2;
        }

        return arr;
    }

    public static int[] fillRow(@NotNull InventoryType type) {
        return repeat(type.getSize()-9, type.getSize());
    }

    public static int[] fillColumn(@NotNull InventoryType type, int column) {
        if (column < 0 || column > INVENTORY_WIDTH - 1) {
            throw new IllegalArgumentException("Column cant be less than 0 or more than 8");
        }

        var arr = new int[getRowCount(type)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i * INVENTORY_WIDTH + column;
        }
        return arr;
    }

    public static int getRowCount(@NotNull InventoryType inventoryType) {
        if (inventoryType.getSize() % INVENTORY_WIDTH != 0) return 1;
        return inventoryType.getSize() / INVENTORY_WIDTH;
    }
}
