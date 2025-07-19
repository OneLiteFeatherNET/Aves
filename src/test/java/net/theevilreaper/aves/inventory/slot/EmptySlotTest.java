package net.theevilreaper.aves.inventory.slot;

import net.theevilreaper.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmptySlotTest {

    @Test
    void testEmptySlot() {
        EmptySlot emptySlot = InventoryConstants.BLANK_SLOT;

        assertThrowsExactly(
                UnsupportedOperationException.class,
                () -> emptySlot.setClick((player, slot, click, stack, result) -> {}),
                "Cannot set a click on an empty slot"
        );
        assertThrowsExactly(
                UnsupportedOperationException.class,
                () -> emptySlot.setItemStack(ItemStack.AIR),
                "Cannot set an item on an empty slot"
        );
        assertEquals(InventoryConstants.CANCEL_CLICK, emptySlot.getClick());
    }

}