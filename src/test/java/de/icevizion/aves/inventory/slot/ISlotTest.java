package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.InventorySlot;
import net.minestom.server.item.ItemStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class ISlotTest {

    @Test
    void testSlotCreation() {
        var slot = new InventorySlot(ItemStack.AIR);
        assertSame(ItemStack.AIR, slot.getItem());
    }
}