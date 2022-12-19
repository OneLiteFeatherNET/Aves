package de.icevizion.aves.inventory.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/

class InventoryConstantsTest {

    @Test
    void testInventoryWidth() {
        assertEquals(9, InventoryConstants.INVENTORY_WIDTH);
    }
}