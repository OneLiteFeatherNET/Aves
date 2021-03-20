package de.icevizion.aves.inventory;

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

        //Maybe ceil()?
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
}
