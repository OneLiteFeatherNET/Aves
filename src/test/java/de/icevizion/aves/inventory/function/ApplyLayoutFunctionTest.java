package de.icevizion.aves.inventory.function;

import de.icevizion.aves.inventory.InventorySlot;
import de.icevizion.aves.inventory.slot.ISlot;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApplyLayoutFunctionTest {

    @Test
    void testApplyLayoutWithOnlyItemStacks() {
        var slots = new ISlot[InventoryType.CHEST_1_ROW.getSize()];
        var item = ItemStack.builder(Material.BLACK_STAINED_GLASS).build();
        var inventorySlot = new InventorySlot(item);
        var itemStacks = new ItemStack[InventoryType.CHEST_1_ROW.getSize()];
        Arrays.fill(slots, inventorySlot);

        var applyFunction = new DefaultApplyLayoutFunction(slots);
        applyFunction.applyLayout(itemStacks);

        for (ItemStack itemStack : itemStacks) {
            assertNotNull(itemStack);
        }
    }
}