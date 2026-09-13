package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Regression coverage for the case where the design layout and the data layout are invalidated independently.
 * <p>
 * {@link InventoryBuilder#updateInventory(Inventory, java.util.Locale, boolean)} clears the whole inventory when
 * it redraws the base {@link InventoryLayout}. Previously that redraw only reapplied the data layout overlay when
 * {@code dataLayoutValid} was already {@code false}, so invalidating only the base layout silently dropped a data
 * layout that was still considered valid, and invalidating only the data layout on a closed inventory never
 * triggered a recompute at all. Both cases forced callers to invalidate both layouts for a single logical update.
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.0.0
 */
@ExtendWith(MicrotusExtension.class)
class GlobalInventoryBuilderDataLayoutSyncTest {

    private static final Component TITLE_COMPONENT = Component.text("DataLayoutSync");
    private static final InventoryType INVENTORY_TYPE = InventoryType.CHEST_2_ROW;
    private static final int[] DATA_SLOTS = LayoutCalculator.fillRow(INVENTORY_TYPE);
    private static final int LAYOUT_SLOT = 0;

    /**
     * Invalidating only the base layout must not drop an already applied data layout.
     */
    @Test
    void invalidatingOnlyTheLayoutKeepsTheDataLayoutApplied(@NotNull Env env) {
        env.createFlatInstance();
        GlobalInventoryBuilder builder = createPreparedBuilder(env);
        Inventory inventory = builder.getInventory();

        assertDataLayoutApplied(inventory);

        // Only the base layout is invalidated here - invalidateDataLayout() is deliberately not called.
        builder.invalidateLayout();
        builder.updateInventory();

        assertEquals(Material.STONE, inventory.getItemStack(LAYOUT_SLOT).material(), "The base layout was not redrawn");
        assertDataLayoutApplied(inventory);
    }

    /**
     * Invalidating only the data layout on a closed inventory must still trigger a recompute once the
     * inventory is requested again, instead of silently doing nothing because the base layout was still valid.
     */
    @Test
    void invalidatingOnlyTheDataLayoutOnAClosedInventoryStillRecomputes(@NotNull Env env) {
        env.createFlatInstance();
        GlobalInventoryBuilder builder = createPreparedBuilder(env);
        assertDataLayoutApplied(builder.getInventory());

        builder.invalidateDataLayout();
        Inventory inventory = builder.getInventory();
        env.tick();

        assertDataLayoutApplied(inventory);
    }

    private void assertDataLayoutApplied(@NotNull Inventory inventory) {
        for (int slot : DATA_SLOTS) {
            assertEquals(Material.DIAMOND, inventory.getItemStack(slot).material(), "Slot " + slot + " does not carry the data layout");
        }
    }

    private @NotNull GlobalInventoryBuilder createPreparedBuilder(@NotNull Env env) {
        GlobalInventoryBuilder builder = new GlobalInventoryBuilder(TITLE_COMPONENT, INVENTORY_TYPE);
        InventoryLayout layout = InventoryLayout.fromType(INVENTORY_TYPE);
        InventoryLayout dataLayout = InventoryLayout.fromType(INVENTORY_TYPE);

        layout.setItem(LAYOUT_SLOT, ItemStack.of(Material.STONE));
        dataLayout.setItems(DATA_SLOTS, ItemStack.of(Material.DIAMOND));

        builder.setLayout(layout);
        builder.setDataLayoutFunction(previousLayout -> dataLayout);

        assertNotNull(builder.getInventory());
        env.tick();
        assertNotNull(builder.getDataLayout(), "The scheduled data layout retrieval did not run");

        return builder;
    }
}
