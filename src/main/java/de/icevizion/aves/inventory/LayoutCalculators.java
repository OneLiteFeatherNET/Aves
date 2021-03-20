package de.icevizion.aves.inventory;

import org.jetbrains.annotations.NotNull;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class LayoutCalculators {

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
        var x1 = firstCornerSlot % 9;
        var y1 = Math.floor(firstCornerSlot / 9.0);

        var x2 = lastCornerSlot % 9;
        var y2 = Math.floor(lastCornerSlot / 9.0);

        var width = (x2-x1) + 1;
        var height = (y2-y1) + 1;

        var arr = new int[(int) Math.floor(width * height)];

        for (int i = 0; i < arr.length; i++) {
            var xSquare = i % width;
            var ySquare = Math.floor(i / width);

            var x = x1 + xSquare;
            var y = y1 + ySquare;

            arr[i] = (int) Math.floor(y*9) + x;

            System.out.println("x: "+x+" y: "+y+" xSquare: "+xSquare+" ySquare: "+xSquare+" arr: "+arr[i]);
        }

        return arr;
    }

    public static int[] frame(int firstCornerSlot, int lastCornerSlot) {
        var x1 = firstCornerSlot % 9;
        var y1 = Math.floor(firstCornerSlot / 9.0);

        var x2 = lastCornerSlot % 9;
        var y2 = Math.floor(lastCornerSlot / 9.0);

        var width = (x2-x1) + 1;
        var height = (y2-y1) + 1;

        var arr = new int[(int) ((width-2)*2 + (height-2)*2 + 4)];
        var index = 0;

        for (int i = 0; i < width; i++) {
            arr[index++] = (int) y1*9+x1+i;
            arr[index++] = (int) y2*9+x1+i;
        }
        for (int i = 0; i < height-2; i++) {
            arr[index++] = (int) (y1+1+i)*9+x1;
            arr[index++] = (int) (y1+1+i)*9+x2;
        }

        return arr;
    }

    public static int[] fillRow(@NotNull InventoryRows row) {
        return repeat(row.getSize()-9, row.getSize());
    }

    public static int[] fillColumn(@NotNull InventoryRows rows, int column) {
        if (column < 0 || column > 8) {
            throw new IllegalArgumentException("Column cant be less than 0 or more than 8");
        }

        var arr = new int[rows.getRowCount()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i*9 + column;
        }

        return arr;
    }
}
