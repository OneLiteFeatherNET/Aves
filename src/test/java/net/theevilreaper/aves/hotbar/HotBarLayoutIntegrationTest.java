package net.theevilreaper.aves.hotbar;

import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MicrotusExtension.class)
class HotBarLayoutIntegrationTest {

    @Test
    void testApplyWithoutClearingInventory(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance);
        player.getInventory().setItemStack(10, ItemStack.of(Material.DIAMOND));

        var layout = new HotBarLayout().set(0, ItemStack.of(Material.STICK));
        layout.apply(player, false);

        assertEquals(Material.STICK, player.getInventory().getItemStack(0).material());
        assertEquals(Material.DIAMOND, player.getInventory().getItemStack(10).material());
        env.destroyInstance(instance, true);
    }

    @Test
    void testApplyWithClearingInventory(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance);
        player.getInventory().setItemStack(10, ItemStack.of(Material.DIAMOND));

        var layout = new HotBarLayout().set(0, ItemStack.of(Material.STICK));
        layout.apply(player, true);

        assertEquals(Material.STICK, player.getInventory().getItemStack(0).material());
        assertEquals(ItemStack.AIR, player.getInventory().getItemStack(10));
        env.destroyInstance(instance, true);
    }

    @Test
    void testApplyClearsEntireInventoryAndArmor(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance);

        player.getInventory().setItemStack(15, ItemStack.of(Material.GOLD_INGOT));
        player.getInventory().setItemStack(25, ItemStack.of(Material.IRON_INGOT));
        player.setHelmet(ItemStack.of(Material.DIAMOND_HELMET));
        player.setChestplate(ItemStack.of(Material.DIAMOND_CHESTPLATE));
        player.setLeggings(ItemStack.of(Material.DIAMOND_LEGGINGS));
        player.setBoots(ItemStack.of(Material.DIAMOND_BOOTS));
        player.setItemInOffHand(ItemStack.of(Material.SHIELD));

        var layout = new HotBarLayout().set(2, ItemStack.of(Material.COMPASS));
        layout.apply(player, true);

        assertEquals(Material.COMPASS, player.getInventory().getItemStack(2).material());

        assertEquals(ItemStack.AIR, player.getInventory().getItemStack(15));
        assertEquals(ItemStack.AIR, player.getInventory().getItemStack(25));
        assertEquals(ItemStack.AIR, player.getHelmet());
        assertEquals(ItemStack.AIR, player.getChestplate());
        assertEquals(ItemStack.AIR, player.getLeggings());
        assertEquals(ItemStack.AIR, player.getBoots());
        assertEquals(ItemStack.AIR, player.getItemInOffHand());

        env.destroyInstance(instance, true);
    }
}
