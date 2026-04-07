package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventFilter;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryOpenEvent;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.network.packet.server.play.SystemChatPacket;
import net.minestom.testing.Collector;
import net.minestom.testing.Env;
import net.minestom.testing.TestConnection;
import net.minestom.testing.extension.MicrotusExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MicrotusExtension.class)
class BaseInventoryBuilderImplIntegrationTest {

    private static final Component TITLE_COMPONENT = Component.text("Test");

    private Instance instance;
    private TestConnection testConnection;
    private Player player;

    @BeforeEach
    void setup(Env env) {
        instance = env.createFlatInstance();
        testConnection = env.createConnection();
        player = testConnection.connect(instance);
    }

    @AfterEach
    void teardown(Env env) {
        env.destroyInstance(instance, true);
    }

    @Test
    void testCloseFunctionCall(Env env) {
        GlobalInventoryBuilder testInventory = buildInventory();
        testInventory.setCloseFunction(event ->
                event.getPlayer().sendMessage(Component.text("Closed"))
        );
        testInventory.register();

        assertNotNull(testInventory.getInventory());

        player.openInventory(testInventory.getInventory());
        assertNotNull(player.getOpenInventory());
        assertInstanceOf(CustomInventory.class, player.getOpenInventory());

        Collector<SystemChatPacket> chatCollector = testConnection.trackIncoming(SystemChatPacket.class);
        Collector<InventoryCloseEvent> closeTracker = env.trackEvent(InventoryCloseEvent.class, EventFilter.INVENTORY, testInventory.getInventory());

        player.closeInventory();

        closeTracker.assertSingle(event -> {
            assertInstanceOf(CustomInventory.class, event.getInventory());
            CustomInventory inventory = (CustomInventory) event.getInventory();
            assertEquals("Test", PlainTextComponentSerializer.plainText().serialize(inventory.getTitle()));
        });

        env.tick();
        assertSingleChatMessage(chatCollector, "Closed");
    }

    @Test
    void testOpenFunctionCall(Env env) {
        GlobalInventoryBuilder testInventory = buildInventory();
        testInventory.setOpenFunction(event ->
                event.getPlayer().sendMessage(Component.text("Opened"))
        );
        testInventory.register();

        assertNotNull(testInventory.getInventory());

        Collector<SystemChatPacket> chatCollector = testConnection.trackIncoming(SystemChatPacket.class);
        Collector<InventoryOpenEvent> openTracker = env.trackEvent(InventoryOpenEvent.class, EventFilter.INVENTORY, testInventory.getInventory());

        player.openInventory(testInventory.getInventory());
        assertNotNull(player.getOpenInventory());
        assertInstanceOf(CustomInventory.class, player.getOpenInventory());

        openTracker.assertSingle(event -> {
            assertInstanceOf(CustomInventory.class, event.getInventory());
            CustomInventory inventory = (CustomInventory) event.getInventory();
            assertEquals("Test", PlainTextComponentSerializer.plainText().serialize(inventory.getTitle()));
        });

        env.tick();
        assertSingleChatMessage(chatCollector, "Opened");
    }

    /**
     * Creates a pre-configured {@link GlobalInventoryBuilder} with {@link #TITLE_COMPONENT}
     * as title and {@link InventoryType#ANVIL} as type, including a matching layout.
     *
     * @return a new {@link GlobalInventoryBuilder} ready for use in tests
     */
    private GlobalInventoryBuilder buildInventory() {
        GlobalInventoryBuilder builder = new GlobalInventoryBuilder(TITLE_COMPONENT, InventoryType.ANVIL);
        builder.setLayout(InventoryLayout.fromType(builder.getType()));
        return builder;
    }

    /**
     * Asserts that the given {@link Collector} contains exactly one {@link SystemChatPacket}
     * whose first component matches the expected plain-text message.
     *
     * @param collector the packet collector to assert against
     * @param expected  the expected plain-text message
     */
    private void assertSingleChatMessage(Collector<SystemChatPacket> collector, String expected) {
        collector.assertSingle(packet -> {
            assertNotNull(packet.components());
            assertEquals(1, packet.components().size());
            Component first = packet.components().iterator().next();
            assertNotNull(first);
            assertEquals(expected, PlainTextComponentSerializer.plainText().serialize(first));
        });
    }
}