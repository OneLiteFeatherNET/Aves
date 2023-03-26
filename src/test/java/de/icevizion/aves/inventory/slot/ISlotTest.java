package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.inventory.InventorySlot;
import de.icevizion.aves.inventory.util.InventoryConstants;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ISlotTest {

    @Test
    void testGetter() {
        ISlot iSlot = new InventorySlot(ItemStack.AIR, InventoryConstants.CANCEL_CLICK);
        assertSame(Material.AIR, iSlot.getItem().material());
        assertEquals(InventoryConstants.CANCEL_CLICK, iSlot.getClick());
    }

    @Test
    void testSetter() {
        ISlot iSlot = new InventorySlot(ItemStack.AIR);
        assertSame(Material.AIR, iSlot.getItem().material());
        assertNull(iSlot.getClick());
        iSlot.setItemStack(ItemStack.of(Material.ACACIA_FENCE));
        assertNotSame(Material.AIR, iSlot.getItem().material());
        iSlot.setClick(InventoryConstants.CANCEL_CLICK);
        assertNotNull(iSlot.getClick());
    }

    @Test
    void testCopyWithNullReturn() {
        ISlot iSlot = new UnknownSlotImpl();
        ISlot copiedSlot = ISlot.of(iSlot);

        assertNotNull(iSlot);
        assertNull(copiedSlot);
    }
}