package de.icevizion.aves.inventory.util;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;

import static de.icevizion.aves.inventory.util.InventoryConstants.*;

/**
 * Contains some usefully methods to calculate some layouts for an inventory.
 * @author Patrick Zdarsky / Rxcki
 * @since 1.0.0
 * @version 1.0.1
 */
public final class LayoutCalculator {

    private static final String CHEST_INVENTORY = "CHEST";

    private LayoutCalculator() {}

    /**
     * Calculates the slot numbers between two slot numbers.
     * @param fromSlot The id from the start slot
     * @param toSlot The id from the end slot
     * @return an array which contains the slot numbers
     */
    public static int[] repeat(int fromSlot, int toSlot) {
        Check.argCondition(fromSlot > toSlot, "fromSlot cannot be higher that toSlot!");
        var arr = new int[toSlot - fromSlot];
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

    public static int @NotNull [] frame(int firstCornerSlot, int lastCornerSlot) {
        Check.argCondition(firstCornerSlot == lastCornerSlot, "The values can't be the smae");
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

    /**
     * Calculates the slot numbers to fill a complete row in an inventory.
     * @param type The {@link InventoryType} to get the maximum slot value of a row
     * @return an array which contains the slot numbers
     */
    public static int @NotNull [] fillRow(@NotNull InventoryType type) {
        return repeat(type.getSize() - INVENTORY_WIDTH, type.getSize());
    }

    /**
     * Calculates the index numbers to fill a row.
     * @param type The type from an inventory
     * @param column The column to start
     * @return an array which contains the slot numbers
     */
    public static int @NotNull [] fillColumn(@NotNull InventoryType type, int column) {
        Check.argCondition(column < 0 || column > INVENTORY_WIDTH - 1, "Column cant be less than 0 or more than 8");
        var arr = new int[getRowCount(type)];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = i * INVENTORY_WIDTH + column;
        }
        return arr;
    }

    /**
     * Calculates the row count of a given {@link InventoryType}.
     * @param type The type to define the row count
     * @return Returns the determined row count
     */
    public static int getRowCount(@NotNull InventoryType type) {
        if (!isChestInventory(type)) return 1;
        return type.getSize() / INVENTORY_WIDTH;
    }

    /**
     * Checks if a given {@link InventoryType} is a chest or not.
     * @param type The typ to check
     * @return True if the type is a chest otherwise false
     */
    public static boolean isChestInventory(@NotNull InventoryType type) {
        return type.name().startsWith(CHEST_INVENTORY);
    }
}
