package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.util.LayoutCalculator;
import de.icevizion.aves.item.IItem;
import de.icevizion.aves.item.Item;
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

    List<IItem> items;

    @BeforeAll
    void init(Env env) {
        this.items = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            this.items.add(
                    new Item(
                            ItemStack.builder(Material.ACACIA_BUTTON)
                                    .displayName(Component.text("Hallo " + i))
                                    .build()
                    )
            );
        }
        this.pageableInventory = PageableInventory
                .builder()
                .title(Component.text("Test title"))
                .type(TYPE)
                .decoration(new InventoryLayout(TYPE))
                .slotRange(LayoutCalculator.repeat(InventoryType.CHEST_1_ROW.getSize() + 1, TYPE.getSize()))
                .controls(new DefaultPageableControls(TYPE.getSize() - 2,TYPE.getSize() - 1,TYPE.getSize()))
                .values(this.items)
                .build();
    }

    @Test
    void testNoControls(Env env) {
        var builder = PageableInventory
                .builder()
                .type(TYPE)
                .decoration(new InventoryLayout(TYPE))
                .slotRange(12)
                .values(this.items)
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