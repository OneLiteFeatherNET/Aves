package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.InventorySlot;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.util.LayoutCalculator;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageableInventoryTest {

    static final InventoryType TYPE = InventoryType.CHEST_3_ROW;

    PageableInventory pageableInventory;

    List<ISlot> slots;

    int[] slotRange = LayoutCalculator.repeat(InventoryType.CHEST_1_ROW.getSize() + 1, InventoryType.CHEST_2_ROW.getSize() - 1);


    @BeforeAll
    void init(Env env) {
        this.slots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            this.slots.add(
                    new InventorySlot(
                            ItemStack.builder(Material.ACACIA_BUTTON)
                                    .displayName(Component.text("Hallo " + i))
                                    .build()
                    )
            );
        }
        Player player = env.createPlayer(env.createFlatInstance(), Pos.ZERO);
        this.pageableInventory = PageableInventory
                .builder()
                .player(player)
                .title(Component.text("Test title"))
                .type(TYPE)
                .layout(InventoryLayout.fromType(TYPE))
                .slotRange(slotRange)
                .controls(new DefaultPageableControls(TYPE, TYPE.getSize() - 2,TYPE.getSize() - 1))
                .values(this.slots)
                .build();
    }

    @Test
    void testUpdate(Env env) {
        this.pageableInventory.add(new InventorySlot(ItemStack.of(Material.ITEM_FRAME)));
        assertEquals(2, this.pageableInventory.getMaxPages());
    }

    @Test
    void testNoControls(Env env) {
        Player player = env.createPlayer(env.createFlatInstance(), Pos.ZERO);
        var builder = PageableInventory
                .builder()
                .player(player)
                .type(TYPE)
                .layout(InventoryLayout.fromType(TYPE))
                .slotRange(12)
                .values(this.slots)
                .title(Component.text("A"));
        assertNotNull(builder.build());
        player.remove();
    }

    @Test
    void testMissingLayout(Env env) {
        var builder = PageableInventory.builder().type(TYPE).slotRange(12, 13);
        assertThrowsExactly(IllegalArgumentException.class, builder::build, "The layout can't be null");
    }

    @Test
    void testNoChestInventory(Env env) {
        var builder = PageableInventory.builder().layout(InventoryLayout.fromType(TYPE)).slotRange(12, 13);
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> builder.type(InventoryType.CRAFTING).build(),
                "The type must be a chest inventory"
        );
    }

    @Test
    void testInvalidItemRange(Env env) {
        var builder = PageableInventory.builder().layout(InventoryLayout.fromType(TYPE)).type(TYPE);
        assertThrowsExactly(IllegalArgumentException.class,
                () -> builder.slotRange().build(),
                "The slotRange can't be zero"
        );
    }

    @Test
    void testAddSlotUpdate(Env env) {
        var items = new ArrayList<ISlot>();
        Player player = env.createPlayer(env.createFlatInstance(), Pos.ZERO);
        var pageableInventory = PageableInventory
                .builder()
                .player(player)
                .title(Component.text("Test title"))
                .type(TYPE)
                .layout(InventoryLayout.fromType(TYPE))
                .slotRange(slotRange)
                .controls(new DefaultPageableControls(TYPE, TYPE.getSize() - 2,TYPE.getSize() - 1))
                .values(items)
                .build();
        assertEquals(1, pageableInventory.getMaxPages());

        var testSlot = new InventorySlot(ItemStack.of(Material.STICK));

        pageableInventory.add(testSlot);

        assertSame(1, pageableInventory.getMaxPages());

        var entries = new ArrayList<ISlot>();

        for (int i = 0; i <= 8; i++) {
            entries.add(testSlot);
        }

        pageableInventory.add(entries);

        assertSame(2, pageableInventory.getMaxPages());

        player.remove();
    }

    @Test
    void testRemoveSlotUpdate(Env env) {
        var items = new ArrayList<ISlot>();
        var uniqueSLot = new InventorySlot(ItemStack.of(Material.ACACIA_FENCE));
        items.add(uniqueSLot);
        var otherSlots = new ArrayList<ISlot>();
        var testSlot = new InventorySlot(ItemStack.of(Material.STICK));

        for (int i = 0; i <= 8; i++) {
            otherSlots.add(testSlot);
        }
        Player player = env.createPlayer(env.createFlatInstance(), Pos.ZERO);
        items.addAll(otherSlots);
        var pageableInventory = PageableInventory
                .builder()
                .player(player)
                .title(Component.text("Test title"))
                .type(TYPE)
                .layout(InventoryLayout.fromType(TYPE))
                .slotRange(slotRange)
                .controls(new DefaultPageableControls(TYPE, TYPE.getSize() - 2,TYPE.getSize() - 1))
                .values(items)
                .build();
        assertEquals(2, pageableInventory.getMaxPages());

        pageableInventory.remove(uniqueSLot);

        assertEquals(2, pageableInventory.getMaxPages());

        pageableInventory.remove(otherSlots);

        assertSame(1, pageableInventory.getMaxPages());

        player.remove();
    }
}