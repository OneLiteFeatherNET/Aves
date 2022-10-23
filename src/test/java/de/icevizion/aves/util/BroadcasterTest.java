package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class BroadcasterTest {

    static Component text = Component.text("Test");

    @Test
    void testBroadcastWithEmptyList() {
        List<Player> players = Collections.emptyList();
        Broadcaster.broadcast(players, text);
        assertNotNull(text);
    }

    @Test
    void testBroadcast(Env env) {
        var instance = env.createFlatInstance();
        env.createPlayer(instance, Pos.ZERO);

        Broadcaster.broadcast(instance, text);
        assertNotNull(instance);
    }
}