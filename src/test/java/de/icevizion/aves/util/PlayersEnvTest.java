package de.icevizion.aves.util;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class PlayersEnvTest {

    static Instance instance;

    @BeforeAll
    static void init(Env env) {
        instance = env.createFlatInstance();
        for (int i = 0; i < 3; i++) {
            env.createPlayer(instance, Pos.ZERO);
        }
    }

    @Test
    void testRandomPlayer(Env env) {
        var randomPlayer = Players.getRandomPlayer();
        assertFalse(randomPlayer::isPresent);
    }
}
