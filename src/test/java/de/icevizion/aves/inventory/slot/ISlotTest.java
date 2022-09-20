package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.InventorySlot;
import de.icevizion.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ISlotTest {

    @Test
    void testSlotCreation() {
        var slot = new InventorySlot(ItemStack.AIR);
        assertSame(ItemStack.AIR, slot.getItem());
    }

    @Test
    void testOtherSlotCreation() {
        var slot = new InventorySlot(ItemStack.AIR, InventoryConstants.CANCEL_CLICK);
        assertSame(ItemStack.AIR, slot.getItem());
        assertSame(InventoryConstants.CANCEL_CLICK, slot.getClick());
    }

    @Test
    void testCopySlotCreation() {
        var slot = new InventorySlot(ItemStack.AIR, InventoryConstants.CANCEL_CLICK);
        var copySlot = InventorySlot.of(slot);
        assertSame(slot.getItem(), copySlot.getItem());
        assertSame(slot.getClick(), copySlot.getClick());
        assertNotSame(slot, copySlot);
    }

    @Test
    void setItemSet() {
        var slot = new InventorySlot(ItemStack.AIR, InventoryConstants.CANCEL_CLICK);
        slot.setItemStack(ItemStack.builder(Material.ALLIUM).build());
        assertNotSame(ItemStack.AIR, slot.getItem());
    }

    @Test
    void testToString() {
        var slot = new InventorySlot(ItemStack.builder(Material.ACACIA_BOAT).build());
        System.out.println(slot);
        assertNotSame("empty", slot.toString());
    }
}