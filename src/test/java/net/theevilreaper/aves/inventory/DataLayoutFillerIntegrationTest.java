package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.i18n.TextData;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.EmptySlot;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class DataLayoutFillerIntegrationTest {

    private static final InventoryType TYPE = InventoryType.CHEST_1_ROW;
    private static final ItemStack DATA_ITEM = ItemStack.of(Material.PLAYER_HEAD);
    private static final ItemStack DESIGN_ITEM = ItemStack.of(Material.BLACK_STAINED_GLASS_PANE);

    @Test
    void testShrinkingFillerClearsStaleItem(@NotNull Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        var builder = new GlobalInventoryBuilder(Component.text("Test"), TYPE);
        var layout = InventoryLayout.fromType(TYPE);
        layout.setItem(8, DESIGN_ITEM);
        builder.setLayout(layout);
        var count = new AtomicInteger(3);
        builder.setDataLayoutFiller(dataLayout -> fill(dataLayout, count.get()));
        builder.register();

        player.openInventory(builder.getInventory());
        tick(env);
        assertEquals(Material.PLAYER_HEAD, builder.getInventory().getItemStack(2).material());

        count.set(2);
        builder.invalidateDataLayout();
        tick(env);

        assertEquals(Material.PLAYER_HEAD, builder.getInventory().getItemStack(1).material());
        assertTrue(builder.getInventory().getItemStack(2).isAir());
        assertEquals(Material.BLACK_STAINED_GLASS_PANE, builder.getInventory().getItemStack(8).material());
        // The freed slot is only blanked for one round, afterwards it is unmanaged again
        assertInstanceOf(EmptySlot.class, builder.getDataLayout().getSlot(2));

        builder.invalidateDataLayout();
        tick(env);

        assertNull(builder.getDataLayout().getSlot(2));
        assertTrue(builder.getInventory().getItemStack(2).isAir());

        cleanup(env, builder, player, instance);
    }

    @Test
    void testGrowingFillerAddsItems(@NotNull Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        var builder = new GlobalInventoryBuilder(Component.text("Test"), TYPE);
        builder.setLayout(InventoryLayout.fromType(TYPE));
        var count = new AtomicInteger(1);
        builder.setDataLayoutFiller(dataLayout -> fill(dataLayout, count.get()));
        builder.register();

        player.openInventory(builder.getInventory());
        tick(env);
        assertTrue(builder.getInventory().getItemStack(1).isAir());

        count.set(2);
        builder.invalidateDataLayout();
        tick(env);

        assertEquals(Material.PLAYER_HEAD, builder.getInventory().getItemStack(1).material());

        cleanup(env, builder, player, instance);
    }

    @Test
    void testShrinkingFillerInTranslatedBuilder(@NotNull Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        var builder = new GlobalTranslatedInventoryBuilder(TYPE);
        builder.setTitleData(new TextData("title"));
        builder.setLayout(InventoryLayout.fromType(TYPE));
        var count = new AtomicInteger(3);
        builder.setDataLayoutFiller(dataLayout -> fill(dataLayout, count.get()));
        builder.register();

        var inventory = builder.getInventory(Locale.ENGLISH);
        player.openInventory(inventory);
        tick(env);
        assertEquals(Material.PLAYER_HEAD, inventory.getItemStack(2).material());

        count.set(2);
        builder.invalidateDataLayout();
        tick(env);

        assertTrue(inventory.getItemStack(2).isAir());

        cleanup(env, builder, player, instance);
    }

    @Test
    void testFillerKeepsTheSameLayoutInstance(@NotNull Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        var builder = new GlobalInventoryBuilder(Component.text("Test"), TYPE);
        builder.setLayout(InventoryLayout.fromType(TYPE));
        var received = new ArrayList<InventoryLayout>();
        builder.setDataLayoutFiller(dataLayout -> {
            received.add(dataLayout);
            fill(dataLayout, 1);
        });
        var ownedLayout = builder.getDataLayout();
        assertNotNull(ownedLayout);
        builder.register();

        player.openInventory(builder.getInventory());
        tick(env);
        builder.invalidateDataLayout();
        tick(env);

        assertEquals(2, received.size());
        assertSame(ownedLayout, received.get(0));
        assertSame(ownedLayout, received.get(1));
        assertSame(ownedLayout, builder.getDataLayout());

        cleanup(env, builder, player, instance);
    }

    @Test
    void testFillerAndFunctionReplaceEachOther() {
        var builder = new GlobalInventoryBuilder(Component.text("Test"), TYPE);

        builder.setDataLayoutFiller(dataLayout -> fill(dataLayout, 1));
        assertNotNull(builder.dataLayoutFiller);
        assertNull(builder.dataLayoutFunction);

        builder.setDataLayoutFunction(dataLayout -> dataLayout);
        assertNull(builder.dataLayoutFiller);
        assertNotNull(builder.dataLayoutFunction);
        // The layout of the filler is not passed to the function
        assertNull(builder.getDataLayout());
    }

    private static void fill(@NotNull InventoryLayout layout, int count) {
        for (int i = 0; i < count; i++) {
            layout.setItem(i, DATA_ITEM);
        }
    }

    private static void tick(@NotNull Env env) {
        for (int i = 0; i < 3; i++) {
            env.tick();
        }
    }

    private static void cleanup(
            @NotNull Env env,
            @NotNull InventoryBuilder builder,
            @NotNull Player player,
            @NotNull Instance instance
    ) {
        player.closeInventory();
        builder.unregister();
        player.remove(true);
        env.destroyInstance(instance);
    }
}
