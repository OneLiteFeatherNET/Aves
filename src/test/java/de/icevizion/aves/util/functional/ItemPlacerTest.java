package de.icevizion.aves.util.functional;

import de.icevizion.aves.item.Item;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class ItemPlacerTest {

    @Test
    void testDefaultPlacer(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        ItemPlacer.FALLBACK.setItem(player, 0, Item.of(Material.SADDLE), null);
        player.remove();
        env.destroyInstance(instance);
        assertNotNull(player);
        assertNotNull(instance);
    }

    @Test
    void testSetArmorSet(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        var helmetItem = Item.of(ItemStack.builder(Material.LEATHER_HELMET));
        var chestPlateItem = Item.of(ItemStack.builder(Material.LEATHER_CHESTPLATE));
        var leggingsItem = Item.of(ItemStack.builder(Material.LEATHER_LEGGINGS));
        ItemPlacer.FALLBACK.setItem(player, 0, helmetItem, null, true);
        ItemPlacer.FALLBACK.setItem(player, 1, chestPlateItem, null, true);
        ItemPlacer.FALLBACK.setItem(player, 2, leggingsItem, null, true);
        ItemPlacer.FALLBACK.setItem(player, 3, leggingsItem, null, true);

        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> ItemPlacer.FALLBACK.setItem(player, -1, leggingsItem, null, true),
                "The slotID is greater than four"
        );
    }
}