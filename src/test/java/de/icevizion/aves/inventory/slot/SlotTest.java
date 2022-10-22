package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.InventorySlot;
import de.icevizion.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {

    @Test
    void testSlotCreationWithClick() {
        Slot slot = new InventorySlot(ItemStack.AIR, InventoryConstants.CANCEL_CLICK);
        assertNotNull(slot.getClick());
        assertSame(Material.AIR, slot.getItem().material());
    }

    @Test
    void testSetItemStack() {
        Slot slot = new InventorySlot(ItemStack.AIR);
        slot.setItemStack(ItemStack.builder(Material.ACACIA_DOOR).build());
        assertNotEquals(Material.AIR, slot.getItem().material());
    }

}