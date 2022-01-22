package de.icevizion.aves.inventory;

/**
 * Contains some method which checks if a given value is not higher than a maximum value.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.1.0
 **/
interface SizeChecker {

    /**
     * Checks if a given amount of slots is not higher than {@link InventoryRow#SIX}.
     * @param slots The amount of slots to check
     */
    default void checkInventorySize(int slots) {
        checkSize(slots, InventoryRow.SIX.getSize());
    }

    /**
     * Checks if a given slot amount is higher than the maximum allowed size.
     * @param value The value to check
     * @param max The maximum value which is allowed
     */
    default void checkSize(int value, int max) {
        if (value > max) {
            throw new IllegalArgumentException("Maximum amount of slots for an inventory is 54!");
        }
    }
}
