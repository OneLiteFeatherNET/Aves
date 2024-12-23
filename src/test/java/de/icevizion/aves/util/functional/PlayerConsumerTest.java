package de.icevizion.aves.util.functional;

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
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MicrotusExtension.class)
class PlayerConsumerTest {

    private static final int SLOT_INDEX = 0x00;

    @Test
    void testPlayerConsumer(@NotNull Env env) {
        Instance testInstance = env.createFlatInstance();
        Player testPlayer = env.createPlayer(testInstance);
        ItemStack stack = ItemStack.builder(Material.DAMAGED_ANVIL).build();
        PlayerConsumer playerConsumer = player -> player.getInventory().setItemStack(SLOT_INDEX, stack);

        assertNotNull(playerConsumer);
        playerConsumer.accept(testPlayer);

        ItemStack slotItem = testPlayer.getInventory().getItemStack(SLOT_INDEX);
        assertNotNull(slotItem);
        assertEquals(Material.DAMAGED_ANVIL, slotItem.material());
        env.destroyInstance(testInstance, true);
    }

    @Test
    void testPlayerConsumerAcceptThen(@NotNull Env env) {
        Instance testInstance = env.createFlatInstance();
        Player testPlayer = env.createPlayer(testInstance);
        ItemStack stack = ItemStack.builder(Material.DAMAGED_ANVIL).build();
        PlayerConsumer playerConsumer = player -> player.getInventory().setItemStack(SLOT_INDEX, stack);
        PlayerConsumer playerConsumerAfter = player -> {
            ItemStack slotItem = testPlayer.getInventory().getItemStack(SLOT_INDEX);
            assertNotNull(slotItem);
            assertEquals(Material.DAMAGED_ANVIL, slotItem.material());
            player.getInventory().clear();
        };
        playerConsumer.andThen(playerConsumerAfter);
        assertEquals(ItemStack.AIR, testPlayer.getInventory().getItemStack(SLOT_INDEX));
        env.destroyInstance(testInstance, true);
    }
}
