package de.icevizion.aves.util;

import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.PlayerInventory;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ItemsTest {

    private static final int STACK_SIZE = 64;

    Player player;

    PlayerInventory playerInventory;

    ItemStack[] items;

    ItemStack stack;

    @BeforeAll
    void init() {
        this.items = new ItemStack[InventoryType.CHEST_3_ROW.getSize()];
        Arrays.fill(items, ItemStack.AIR);
        this.stack = ItemStack.builder(Material.ACACIA_LEAVES).build();

        this.player = Mockito.mock(Player.class);
        this.playerInventory = Mockito.mock(PlayerInventory.class);

        Mockito.when(player.getInventory()).thenReturn(playerInventory);
        Mockito.when(playerInventory.getItemStacks()).thenReturn(items);
    }

    @Order(1)
    @Test
    void testGetItemAmountFrom() {
        var stack = ItemStack.builder(Material.ACACIA_LEAVES).build();
        var amount = Items.getAmountFromItem(this.player, stack);
        assertSame(0, amount);
    }

    @Order(2)
    @Test
    void testGetItemAmountWhichIsNotZero() {
        this.items[0] = stack;
        assertSame(12, Items.getAmountFromItem(this.player, stack));
    }

    @Order(3)
    @Test
    void testFreeSpace() {
        assertSame((26 * STACK_SIZE) - 12, Items.getFreeSpace(this.player, stack));
    }

}