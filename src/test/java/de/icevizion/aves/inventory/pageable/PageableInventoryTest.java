package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.InventorySlot;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.util.LayoutCalculator;
import net.kyori.adventure.text.Component;
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

    @BeforeAll
    void init(Env env) {
        this.slots = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            this.slots.add(
                    new InventorySlot(
                            ItemStack.builder(Material.ACACIA_BUTTON)
                                    .displayName(Component.text("Hallo " + i))
                                    .build()
                    )
            );
        }
        var slotRange = LayoutCalculator.fillRow(InventoryType.CHEST_2_ROW);
        System.out.println("Calculated range is " + slotRange.length);
        this.pageableInventory = PageableInventory
                .builder()
                .title(Component.text("Test title"))
                .type(TYPE)
                .decoration(new InventoryLayout(TYPE))
                .slotRange(slotRange)
                .controls(new DefaultPageableControls(TYPE, TYPE.getSize() - 2,TYPE.getSize() - 1))
                .values(this.slots)
                .build();
    }

    @Test
    void testUpdate(Env env) {
        this.pageableInventory.add(new InventorySlot(ItemStack.of(Material.ITEM_FRAME)));
        assertEquals(1, this.pageableInventory.getMaxPages());
    }

    @Test
    void testNoControls(Env env) {
        var builder = PageableInventory
                .builder()
                .type(TYPE)
                .decoration(new InventoryLayout(TYPE))
                .slotRange(12)
                .values(this.slots)
                .title(Component.text("A"));
        assertNotNull(builder.build());
    }

    @Test
    void testMissingLayout(Env env) {
        var builder = PageableInventory.builder().type(TYPE).slotRange(12, 13);
        assertThrowsExactly(IllegalArgumentException.class, builder::build, "The layout can't be null");
    }

    @Test
    void testNoChestInventory(Env env) {
        var builder = PageableInventory.builder().decoration(new InventoryLayout(TYPE)).slotRange(12, 13);
        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> builder.type(InventoryType.CRAFTING).build(),
                "The type must be a chest inventory"
        );
    }

    @Test
    void testInvalidItemRange(Env env) {
        var builder = PageableInventory.builder().decoration(new InventoryLayout(TYPE)).type(TYPE);
        assertThrowsExactly(IllegalArgumentException.class,
                () -> builder.slotRange().build(),
                "The slotRange can't be zero"
        );
    }
}