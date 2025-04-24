package net.theevilreaper.aves.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
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
    void testEmptyRandomPlayer(@NotNull Env env) {
        var randomPlayer = Players.getRandomPlayer();
        assertFalse(randomPlayer::isPresent);
    }

    @Test
    void testRandomPlayerWithOneExistingPlayer(@NotNull Env env) {
        Player player = env.createPlayer(instance);
        assertNotNull(player);
        Optional<Player> randomPlayer = Players.getRandomPlayer();
        assertTrue(randomPlayer::isPresent);
        Player targetPlayer = randomPlayer.get();
        assertNotNull(targetPlayer);
        assertEquals(player.getUuid(), targetPlayer.getUuid());
        player.remove();
    }

    @Test
    void testRandomPlayer(@NotNull Env env) {
        List<Player> playerList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            playerList.add(env.createPlayer(instance));
        }
        assertEquals(3, playerList.size());
        Optional<Player> randomPlayer = Players.getRandomPlayer();
        assertTrue(randomPlayer::isPresent);
        for (Player player : playerList) {
            player.remove();
        }
    }
}
