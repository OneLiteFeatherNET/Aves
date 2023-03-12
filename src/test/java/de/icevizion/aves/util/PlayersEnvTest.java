package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class PlayersEnvTest {

    static Instance instance;

    @BeforeAll
    static void init(@NotNull Env env) {
        instance = env.createFlatInstance();
    }

    @AfterAll
    static void cleanUp(@NotNull Env env) {
        env.destroyInstance(instance);
    }

    @Test
    void testTitleSend(@NotNull Env env) {
        var player = env.createPlayer(instance, Pos.ZERO);
        assertNotNull(player);
        Players.showTitle(player, Component.text("Hallo"), Component.empty(), 10, 50, 10);
        player.remove();
    }

    @Test
    void testRandomPlayer(Env env) {
        var randomPlayer = Players.getRandomPlayer();
        assertFalse(randomPlayer::isPresent);
    }

    /*@Test
    void testDropInventory(@NotNull Env env) {
        var player = env.createPlayer(instance, Pos.ZERO);
        player.getInventory().addItemStack(ItemStack.builder(Material.STONE).build());
        Players.dropPlayerInventory(player);
        assertNotNull(player);
    }*/
}
