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
import net.minestom.testing.extension.MicrotusExtension;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MicrotusExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageableInventoryTest {

    static final InventoryType TYPE = InventoryType.CHEST_3_ROW;

    private final int[] slotRange = LayoutCalculator.repeat(InventoryType.CHEST_1_ROW.getSize() + 1, InventoryType.CHEST_2_ROW.getSize() - 1);
    private PageableInventory pageableInventory;
    private List<ISlot> slots;

    @BeforeAll
    void init(Env env) {
        this.slots = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            this.slots.add(
                    new InventorySlot(
                            ItemStack.builder(Material.ACACIA_BUTTON)
                                    .customName(Component.text("Hallo " + i))
                                    .build()
                    )
            );
        }
        Player player = env.createPlayer(env.createFlatInstance(), Pos.ZERO);
        this.pageableInventory = PageableInventory
                .builder()
                .player(player)
                .titleData(TitleData.builder().title(Component.text("Test title")).build())
                .type(TYPE)
                .layout(InventoryLayout.fromType(TYPE))
                .slotRange(slotRange)
                .controls(new DefaultPageableControls(TYPE, TYPE.getSize() - 2,TYPE.getSize() - 1))
                .values(this.slots)
                .build();
    }

    @Test
    void testUpdate() {
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
    void testMissingLayout() {
        var builder = PageableInventory.builder().type(TYPE).slotRange(12, 13);
        assertThrowsExactly(IllegalArgumentException.class, builder::build, "The layout can't be null");
    }

    @Test
    void testNoChestInventory() {
        var builder = PageableInventory.builder().layout(InventoryLayout.fromType(TYPE)).slotRange(12, 13);
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> builder.type(InventoryType.CRAFTING).build(),
                "The type must be a chest inventory"
        );
    }

    @Test
    void testInvalidItemRange() {
        var builder = PageableInventory.builder().layout(InventoryLayout.fromType(TYPE)).type(TYPE);
        assertThrowsExactly(IllegalArgumentException.class,
                () -> builder.slotRange().build(),
                "The slotRange can't be zero"
        );
    }

    @Test
    void testAddSlotUpdate(@NotNull Env env) {
        var items = new ArrayList<ISlot>();
        Player player = env.createPlayer(env.createFlatInstance(), Pos.ZERO);
        PageableInventory pageInventory = PageableInventory
                .builder()
                .player(player)
                .titleData(titleBuilder -> titleBuilder.title(Component.text("Test title")))
                .type(TYPE)
                .layout(InventoryLayout.fromType(TYPE))
                .slotRange(slotRange)
                .controls(new DefaultPageableControls(TYPE, TYPE.getSize() - 2,TYPE.getSize() - 1))
                .values(items)
                .build();
        assertEquals(1, pageInventory.getMaxPages());

        var testSlot = new InventorySlot(ItemStack.of(Material.STICK));

        pageInventory.add(testSlot);

        assertSame(1, pageInventory.getMaxPages());

        var entries = new ArrayList<ISlot>();

        for (int i = 0; i <= 8; i++) {
            entries.add(testSlot);
        }

        pageInventory.add(entries);

        assertSame(2, pageInventory.getMaxPages());
        env.destroyInstance(player.getInstance(), true);
    }

    @Test
    void testRemoveSlotUpdate(@NotNull Env env) {
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
        PageableInventory pageInventory = PageableInventory
                .builder()
                .player(player)
                .title(Component.text("Test title"))
                .type(TYPE)
                .layout(InventoryLayout.fromType(TYPE))
                .slotRange(slotRange)
                .controls(new DefaultPageableControls(TYPE, TYPE.getSize() - 2,TYPE.getSize() - 1))
                .values(items)
                .build();
        assertEquals(2, pageInventory.getMaxPages());
        pageInventory.remove(uniqueSLot);

        assertEquals(2, pageInventory.getMaxPages());
        pageInventory.remove(otherSlots);

        assertSame(1, pageInventory.getMaxPages());
        env.destroyInstance(player.getInstance(), true);
    }
}