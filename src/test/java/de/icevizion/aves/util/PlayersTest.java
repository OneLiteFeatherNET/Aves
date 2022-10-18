package de.icevizion.aves.util;

import de.icevizion.aves.item.IItem;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayersTest {

    Player player;

    Instance instance;

    @BeforeAll
    void init() {
        this.player = Mockito.mock(Player.class);
        this.instance = Mockito.mock(Instance.class);
        Mockito.when(player.getPosition()).thenReturn(Pos.ZERO);
    }

    @Test
    void testUpdateEquipment() {
        IItem[] hotbarItems = new IItem[12];

        assertThrowsExactly(IllegalArgumentException.class, () ->
                Players.updateHotBar(player, hotbarItems, null, 12),
                "The hotBar can only hold 9 items");

        assertThrowsExactly(IllegalArgumentException.class, () ->
            Players.updateHotBar(player, new IItem[4], null, 55, 1, 1, 1, 1),
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

    @Test
    void testGetEmptyRandomPlayer() {
        var playerOptional = Players.getRandomPlayer(List.of());
        assertTrue(playerOptional::isEmpty);
    }

    @Test
    void testDropItemStacksWithNullContent() {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> Players.dropItemStacks(instance, player.getPosition()),
                "he array can not be null or empty");
    }

    @Test
    void testDropPlayerInventory() {
        var otherPlayer = Mockito.mock(Player.class);
        assertThrowsExactly(NullPointerException.class,
                () -> Players.dropPlayerInventory(otherPlayer),
                "The instance from the player can't be null");
    }
}