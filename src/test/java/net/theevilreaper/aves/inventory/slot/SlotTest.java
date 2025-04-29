package net.theevilreaper.aves.inventory.slot;

import net.theevilreaper.aves.inventory.InventorySlot;
import net.theevilreaper.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SlotTest {

    @Test
    void testSlotCreationWithClick() {
        Slot slot = new InventorySlot(ItemStack.of(Material.PAPER), InventoryConstants.CANCEL_CLICK);
        assertNotNull(slot.getClick());
        assertSame(Material.PAPER, slot.getItem().material());
    }

    @Test
    void testSetItemStack() {
        Slot slot = new InventorySlot(ItemStack.of(Material.PAPER));
        slot.setItemStack(ItemStack.builder(Material.ACACIA_DOOR).build());
        assertNotEquals(Material.AIR, slot.getItem().material());
    }

    @Test
    void testHashCode() {
        Slot slot = new InventorySlot(ItemStack.of(Material.PAPER));
        assertNotEquals(10, slot.hashCode());
    }
}
