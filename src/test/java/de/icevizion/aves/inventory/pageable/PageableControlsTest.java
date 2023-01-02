package de.icevizion.aves.inventory.pageable;

import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageableControlsTest {

    PageableControls pageableControls;

    @BeforeAll
    void init() {
        this.pageableControls = new DefaultPageableControls(10, 12, InventoryType.CHEST_3_ROW.getSize());
    }

    @Test
    void testBackSlot() {
        assertEquals(10, this.pageableControls.getBackSlot());
        assertEquals(Material.ARROW, this.pageableControls.getBackButton().get().material());

    }

    @Test
    void testNextSlot() {
        assertEquals(12, this.pageableControls.getNextSlot());
        assertEquals(Material.ARROW, this.pageableControls.getNextButton().get().material());

    }

    @Test
    void testBackSlotIndexFailure() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new DefaultPageableControls(-1, 2, InventoryType.CHEST_2_ROW.getSize()),
                "The backSlot index is not in the inventory range"
        );
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new DefaultPageableControls(112, 2, InventoryType.CHEST_2_ROW.getSize()),
                "The backSlot index is not in the inventory range"
        );
    }

    @Test
    void testForwardSlotIndexFailure() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new DefaultPageableControls(2, -2, InventoryType.CHEST_2_ROW.getSize()),
                "The backSlot index is not in the inventory range"
        );
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new DefaultPageableControls(1, 200, InventoryType.CHEST_2_ROW.getSize()),
                "The backSlot index is not in the inventory range"
        );
    }

}