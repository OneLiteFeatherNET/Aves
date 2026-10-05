package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.inventory.click.Click;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
class InventoryBuilderEmptySlotTest {

    private static final InventoryType TYPE = InventoryType.CHEST_1_ROW;

    @Test
    void testBlankedSlotClearsInventorySlot() {
        var builder = new GlobalInventoryBuilder(Component.text("Test"), TYPE);
        var inventory = new Inventory(TYPE, Component.text("Test"));
        inventory.setItemStack(0, ItemStack.of(Material.STONE));
        inventory.setItemStack(1, ItemStack.of(Material.DIRT));

        var layout = InventoryLayout.fromType(TYPE);
        layout.blank(0);

        ItemStack[] contents = inventory.getItemStacks();
        layout.applyLayout(contents, null);
        builder.setItemsInternal(inventory, contents);

        // Slot 0 is blanked, so it must be cleared. Slot 1 is unmanaged, so it must stay untouched
        assertTrue(inventory.getItemStack(0).isAir());
        assertEquals(Material.DIRT, inventory.getItemStack(1).material());
    }

    @Test
    void testEmptySlotClickHandling(@NotNull Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        var builder = new GlobalInventoryBuilder(Component.text("Test"), TYPE);
        var layout = InventoryLayout.fromType(TYPE);
        layout.blank(0);
        builder.setLayout(layout);

        assertFalse(builder.isCancelEmptySlotClicks());
        assertSame(ClickHolder.noClick(), click(builder, player, 0));
        assertSame(ClickHolder.noClick(), click(builder, player, 1));

        builder.setCancelEmptySlotClicks(true);
        assertTrue(builder.isCancelEmptySlotClicks());
        // Both the blanked and the unmanaged slot are cancelled
        assertSame(ClickHolder.cancelClick(), click(builder, player, 0));
        assertSame(ClickHolder.cancelClick(), click(builder, player, 1));

        player.remove(true);
        env.destroyInstance(instance);
    }

    private @NotNull ClickHolder click(
            @NotNull InventoryBuilder builder,
            @NotNull net.minestom.server.entity.Player player,
            int slot
    ) {
        AtomicReference<ClickHolder> result = new AtomicReference<>();
        builder.inventoryClick.onClick(player, slot, new Click.Left(slot), ItemStack.AIR, result::set);
        return result.get();
    }
}
