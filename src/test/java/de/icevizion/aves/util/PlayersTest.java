package de.icevizion.aves.util;

import de.icevizion.aves.item.IItem;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MicrotusExtension.class)
class PlayersTest {

    @BeforeAll
    static void init() {
        Players.setItemDuration(Duration.ofMinutes(2));
        Players.setItemPlacer(null);
    }

    @Test
    void testUpdateEquipment(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        IItem[] hotbarItems = new IItem[12];

        assertThrowsExactly(IllegalArgumentException.class, () ->
                        Players.updateHotBar(player, hotbarItems, null, 12),
                "The hotBar can only hold 9 items");

        assertThrowsExactly(IllegalArgumentException.class, () ->
                        Players.updateHotBar(player, new IItem[4], null, 55, 1, 1, 1, 1),
                "The length from shiftedSlots has not the same length with the underlying array");
        Players.updateHotBar(player, new IItem[8], null);
        env.destroyInstance(instance, true);
    }

    @Test
    void getRandomPlayer(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        var players = new ArrayList<Player>();
        for (int i = 0; i < 5; i++) {
            players.add(env.createPlayer(instance));
        }

        var randomPlayerOptional = Players.getRandomPlayer(players);

        assertTrue(randomPlayerOptional::isPresent);

        var randomPlayer = randomPlayerOptional.get();

        assertNotNull(randomPlayer);
        assertNotSame(player, randomPlayer);
        env.destroyInstance(instance, true);

    }

    @Test
    void testGetEmptyRandomPlayer(@NotNull Env env) {
        var playerOptional = Players.getRandomPlayer(List.of());
        assertTrue(playerOptional::isEmpty);
    }

    @Test
    void testDropItemStacksWithNullContent(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Players.dropItemStacks(instance, player.getPosition()),
                "he array can not be null or empty");
        env.destroyInstance(instance, true);
    }

    @Test
    void testDropPlayerInventory(@NotNull Env env) {
        Instance instance = env.createFlatInstance();
        Player player = env.createPlayer(instance);
        player.remove(true);
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Players.dropPlayerInventory(player),
                "The instance from the player can't be null");
        env.destroyInstance(instance, true);
    }
}