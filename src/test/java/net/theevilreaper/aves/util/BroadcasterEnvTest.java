package net.theevilreaper.aves.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MicrotusExtension.class)
class BroadcasterEnvTest {

    static Component text = Component.text("Test");


    @Test
    void testBroadcast(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);

        Broadcaster.broadcast(instance, text);
        assertNotNull(instance);
        player.remove();
        env.destroyInstance(instance);
    }

    @Test
    void testBroadcastToAll(Env env) {
        var instance = env.createFlatInstance();
        var playerOne = env.createPlayer(instance, Pos.ZERO);
        var playerTwo = env.createPlayer(instance, Pos.ZERO);

        assertNotNull(playerOne);
        assertNotNull(playerTwo);

        Broadcaster.broadcast(List.of(playerOne, playerTwo), Component.text("Hallo"));

        playerOne.remove();
        playerTwo.remove();

        env.destroyInstance(instance);
    }
}
