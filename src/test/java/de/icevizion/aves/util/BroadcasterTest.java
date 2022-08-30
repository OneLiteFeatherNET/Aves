package de.icevizion.aves.util;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BroadcasterTest {

    Instance instance;

    Player player;

    @BeforeAll
    void init() {
        this.instance = Mockito.mock(Instance.class);
        this.player = Mockito.mock(Player.class);
        var players = new HashSet<Player>();
        Mockito.when(instance.getPlayers()).thenReturn(players);
    }

    @Test
    void testBroadcastToPlayersInInstance() {
        Broadcaster.broadcast(instance, "");
        assertNotNull(instance);
    }
}