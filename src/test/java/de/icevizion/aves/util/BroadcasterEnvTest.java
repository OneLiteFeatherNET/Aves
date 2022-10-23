package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnvTest
class BroadcasterEnvTest {

    static Component text = Component.text("Test");


    @Test
    void testBroadcast(Env env) {
        var instance = env.createFlatInstance();
        env.createPlayer(instance, Pos.ZERO);

        Broadcaster.broadcast(instance, text);
        assertNotNull(instance);
    }

    @Test
    void testBroadcastToAll(Env env) {
        var connection = env.createConnection();
        Broadcaster.broadcast(text);
        assertNotNull(connection);
    }
}