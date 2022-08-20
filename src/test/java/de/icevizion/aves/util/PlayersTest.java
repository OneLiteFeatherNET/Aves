package de.icevizion.aves.util;

import de.icevizion.aves.item.IItem;
import net.minestom.server.entity.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayersTest {

    Player player;

    @BeforeAll
    void init() {
        this.player = Mockito.mock(Player.class);
    }

    @Test
    void hasInstance() {
        assertThrows(IllegalArgumentException.class, () -> Players.hasInstance(player), "The instance from a player can not be null");
    }

    @Test
    void testUpdateEquipment() {
        IItem[] armorItems = new IItem[7];
        IItem[] hotbarItems = new IItem[12];
        assertThrowsExactly(IllegalArgumentException.class, () ->
                Players.updateEquipment(player, armorItems, hotbarItems, null, 12), "");

        assertThrowsExactly(IllegalArgumentException.class, () ->
                Players.updateEquipment(player, new IItem[4], hotbarItems, null, 12),
                "The hotBar can only hold 9 items");

        assertThrowsExactly(IllegalArgumentException.class, () ->
            Players.updateEquipment(player, new IItem[4], new IItem[5], null, 1),
                    "The length from shiftedSlots has not the same length with the underlying array");
    }

    @Test
    void getRandomPlayer() {
        var players = new ArrayList<Player>();
        for (int i = 0; i < 5; i++) {
            players.add(Mockito.mock(Player.class));
        }

        var randomPlayerOptional = Players.getRandomPlayer(players);

        assertTrue(randomPlayerOptional::isPresent);

        var randomPlayer = randomPlayerOptional.get();

        assertNotNull(randomPlayer);
        assertNotSame(player, randomPlayer);

    }
}