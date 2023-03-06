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
        this.pageableControls = new DefaultPageableControls(InventoryType.CHEST_3_ROW,10, 12);
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
                () -> new DefaultPageableControls(InventoryType.CHEST_2_ROW, -1, 2),
                "The backSlot index is not in the inventory range"
        );
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new DefaultPageableControls(InventoryType.CHEST_2_ROW, 112, 2),
                "The backSlot index is not in the inventory range"
        );
    }

    @Test
    void testForwardSlotIndexFailure() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new DefaultPageableControls(InventoryType.CHEST_2_ROW, 2, -2),
                "The backSlot index is not in the inventory range"
        );
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new DefaultPageableControls(InventoryType.CHEST_2_ROW, 1, 200),
                "The backSlot index is not in the inventory range"
        );
    }

    @Test
    void testOtherCreation() {
        var type = InventoryType.CHEST_3_ROW;
        var controls = DefaultPageableControls.fromSize(type);
        assertNotNull(controls);
        assertEquals(type.getSize() - 1, controls.getNextSlot());
        assertEquals(type.getSize() - 2, controls.getBackSlot());
    }

    @Test
    void testNoChestInventory() {
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> DefaultPageableControls.fromSize(InventoryType.CRAFTING),
                "The type must be a chest inventory!"
        );
    }

    @Test
    void testGetMaterial() {
        var controls = DefaultPageableControls.fromSize(InventoryType.CHEST_3_ROW);
        assertSame(Material.ARROW, controls.getBackMaterial());
        assertNotSame(Material.AIR, controls.getForwardMaterial());
    }

}