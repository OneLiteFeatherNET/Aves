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

    Player player;

    PlayerInventory playerInventory;

    ItemStack[] items;

    ItemStack stack;

    @BeforeAll
    void init() {
        this.items = new ItemStack[InventoryType.CHEST_3_ROW.getSize()];
        Arrays.fill(items, ItemStack.AIR.withAmount(Items.MAX_STACK_SIZE));
        this.stack = ItemStack.builder(Material.ACACIA_LEAVES).amount(12).build();

        this.player = Mockito.mock(Player.class);
        this.playerInventory = Mockito.mock(PlayerInventory.class);

        Mockito.when(player.getInventory()).thenReturn(playerInventory);
        Mockito.when(player.getInventory().getSize()).thenReturn(InventoryType.CHEST_3_ROW.getSize());
        Mockito.when(playerInventory.getItemStacks()).thenReturn(items);
    }

    @Order(1)
    @Test
    void testFreeSpace() {
        assertEquals(
                (this.playerInventory.getSize() * Items.MAX_STACK_SIZE), Items.getFreeSpace(this.player));
    }

    @Order(2)
    @Test
    void testGetItemAmountWhichIsNotZero() {
        this.items[0] = stack;
        assertSame(12, Items.getAmountFromItem(this.player, stack));
    }

    @Order(3)
    @Test
    void testGetItemAmountFrom() {
        var amount = Items.getAmountFromItem(this.player, ItemStack.builder(Material.DIAMOND).build());
        assertSame(0, amount);
    }
}