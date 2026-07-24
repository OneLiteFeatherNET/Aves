package net.theevilreaper.aves.util;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

@ExtendWith(MicrotusExtension.class)
class ItemsIntegrationTest {

    @Test
    void testStackSizeConstant() {
        assertEquals(64, Items.MAX_STACK_SIZE);
    }

    @Test
    void testFreeSpace(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        assertEquals(player.getInventory().getSize() * Items.MAX_STACK_SIZE, Items.getFreeSpace(player));
        env.destroyInstance(instance, true);
    }

    @Test
    void testFreeSpaceWithNonStandardMaxStackSize(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);

        // Slot 0 has 10 Ender Pearls (max stack size 16 -> remaining space in slot 0 is 6)
        player.getInventory().setItemStack(0, ItemStack.of(Material.ENDER_PEARL, 10));

        // Slot 1 has 1 Diamond Sword (max stack size 1 -> remaining space in slot 1 is 0)
        player.getInventory().setItemStack(1, ItemStack.of(Material.DIAMOND_SWORD, 1));

        // Remaining empty slots have (size - 2) * 64 space
        int expectedFreeSpace = ((player.getInventory().getSize() - 2) * Items.MAX_STACK_SIZE) + (16 - 10) + (1 - 1);
        assertEquals(expectedFreeSpace, Items.getFreeSpace(player));

        env.destroyInstance(instance, true);
    }

    @Test
    void testGetItemAmountFrom(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        var amount = Items.getAmountFromItem(player, ItemStack.builder(Material.DIAMOND).build());
        assertSame(0, amount);
        env.destroyInstance(instance, true);
    }
}