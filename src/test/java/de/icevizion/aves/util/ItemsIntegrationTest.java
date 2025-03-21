package de.icevizion.aves.util;

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
    void testGetItemAmountFrom(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        var amount = Items.getAmountFromItem(player, ItemStack.builder(Material.DIAMOND).build());
        assertSame(0, amount);
        env.destroyInstance(instance, true);
    }
}