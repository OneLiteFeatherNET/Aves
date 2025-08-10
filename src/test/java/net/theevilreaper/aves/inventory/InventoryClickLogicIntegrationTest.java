package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.AbstractInventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.network.packet.client.play.ClientClickWindowPacket;
import net.minestom.server.network.packet.server.play.WindowItemsPacket;
import net.minestom.server.utils.inventory.PlayerInventoryUtils;
import net.minestom.testing.Collector;
import net.minestom.testing.Env;
import net.minestom.testing.TestConnection;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class InventoryClickLogicIntegrationTest {

    private static ItemStack testItem;

    @BeforeAll
    static void setup() {
        testItem = ItemStack.builder(Material.DIAMOND)
                .customName(Component.text("Test Item"))
                .build();
    }

    @Test
    void testInventoryClickFlow(@NotNull Env env) {
        Instance instance = env.createEmptyInstance();
        TestConnection testConnection = env.createConnection();
        Player testPlayer = testConnection.connect(instance);
        InventoryBuilder builder = new GlobalInventoryBuilder(Component.text("Test Inventory"), InventoryType.CHEST_3_ROW);

        assertNotNull(builder);

        InventoryLayout layout = InventoryLayout.fromType(builder.getType());
        AtomicBoolean clicked = new AtomicBoolean(false);
        layout.setItem(0, testItem, (player, slot, click, stack, result) -> {
            result.accept(ClickHolder.cancelClick());
            clicked.set(true);
        });

        builder.setLayout(layout);
        builder.register();

        testPlayer.openInventory(builder.getInventory());

        assertNotNull(testPlayer.getOpenInventory());

        Collector<WindowItemsPacket> windowsPacketCollector = testConnection.trackIncoming(WindowItemsPacket.class);

        testLeftClick(testPlayer, 0, testItem);
        // The return value should only be one packet, the one that updates the slots
        windowsPacketCollector.assertCount(2);
        assertTrue(clicked.get());

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should not have any players");
        clicked.setRelease(false);
    }


    @Test
    void testIfClickPassesInventoryLayout(@NotNull Env env) {
        Instance instance = env.createEmptyInstance();
        TestConnection testConnection = env.createConnection();
        Player testPlayer = testConnection.connect(instance);
        InventoryBuilder builder = new GlobalInventoryBuilder(Component.text("Test Inventory"), InventoryType.CHEST_3_ROW);

        assertNotNull(builder);

        InventoryLayout layout = InventoryLayout.fromType(builder.getType());

        AtomicBoolean clicked = new AtomicBoolean(false);
        layout.setItem(0, testItem, (player, slot, click, stack, result) -> {
            result.accept(ClickHolder.cancelClick());
            clicked.set(true);
        });

        builder.setLayout(layout);
        builder.register();

        testPlayer.openInventory(builder.getInventory());

        assertNotNull(testPlayer.getOpenInventory());

        Collector<WindowItemsPacket> windowsPacketCollector = testConnection.trackIncoming(WindowItemsPacket.class);

        testLeftClick(testPlayer, 0, testItem);
        // The return value should only be one packet, the one that updates the slots
        windowsPacketCollector.assertCount(2);

        WindowItemsPacket inventoryPacket = windowsPacketCollector.collect().getLast();
        assertNotNull(inventoryPacket);

        List<ItemStack> items = inventoryPacket.items();
        assertNotNull(items, "Items in the inventory packet should not be null");
        assertEquals(layout.getSize(), items.size(), "Items size should match layout size");

        assertEquals(testItem, items.getFirst(), "First item should be the test item");

        env.destroyInstance(instance, true);
        assertTrue(instance.getPlayers().isEmpty(), "Instance should not have any players");
    }


    private void testLeftClick(@NotNull Player player, int slot, @NotNull ItemStack stack) {
        _leftClick(player.getOpenInventory(), true, player, slot, stack);
    }

    private void _leftClick(AbstractInventory openInventory, boolean clickOpenInventory, Player player, int slot, ItemStack stack) {
        final byte windowId = openInventory != null ? openInventory.getWindowId() : 0;
        if (clickOpenInventory) {
            assert openInventory != null;
        } else {
            int offset = openInventory != null ? openInventory.getInnerSize() : 0;
            slot = PlayerInventoryUtils.convertMinestomSlotToWindowSlot(slot);
            if (openInventory != null) {
                slot = slot - 9 + offset;
            }
        }
        ClientClickWindowPacket clickWindowPacket = new ClientClickWindowPacket(windowId, 0, (short) slot, (byte) 0,
                ClientClickWindowPacket.ClickType.PICKUP, HashMap.newHashMap(0), ItemStack.Hash.of(stack));
        player.addPacketToQueue(clickWindowPacket);
        player.interpretPacketQueue();
    }
}
