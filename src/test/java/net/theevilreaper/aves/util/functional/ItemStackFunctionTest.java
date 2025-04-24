package net.theevilreaper.aves.util.functional;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ItemStackFunctionTest {

    @Test
    void testItemStackFunctionalInterface() {
        ItemStackFunction<Integer> function = itemStack -> itemStack == null ? 0 : itemStack.amount();
        assertEquals(0, function.apply(null));
        final ItemStack stack = ItemStack.builder(Material.ACACIA_FENCE).amount(12).build();
        assertEquals(12, function.apply(stack));
    }

    @Test
    void testItemStackBiFunction() {
        ItemStackBiFunction<Integer, Integer> function = (itemStack, amount) -> {
            if (itemStack == null) {
                return 0;
            }
            itemStack = itemStack.withAmount(amount);
            return itemStack.amount();
        };
        assertEquals(0, function.apply(null, 0));
        final ItemStack stack = ItemStack.builder(Material.ACACIA_FENCE).amount(12).build();
        assertNotEquals(stack.amount(), function.apply(stack, 0));
    }
}
