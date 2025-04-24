package net.theevilreaper.aves.util;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BroadcasterTest {

    static Component text = Component.text("Test");

    @Test
    void testBroadcastWithEmptyList() {
        List<Player> players = new ArrayList<>();
        Broadcaster.broadcast(players, text);
        assertNotNull(text);
    }

}