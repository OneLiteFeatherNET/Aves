package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.util.LayoutCalculator;
import de.icevizion.aves.item.IItem;
import de.icevizion.aves.item.Item;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PageableInventoryTest {

    static final InventoryType TYPE = InventoryType.CHEST_2_ROW;

    PageableInventory pageableInventory;

    List<IItem> items;

    @BeforeAll
    void init() {
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
                .type(InventoryType.CHEST_3_ROW)
                .slotRange(LayoutCalculator.repeat(InventoryType.CHEST_1_ROW.getSize() + 1, TYPE.getSize()))
                .controls(new DefaultPageableControls(TYPE.getSize() - 1,TYPE.getSize(),TYPE.getSize()))
                .values(this.items)
                .build();
    }



    @Test
    void testNoChestInventory() {
        var builder = PageableInventory.builder().type(InventoryType.BEACON);
        assertThrowsExactly(IllegalArgumentException.class, builder::build, "The type must be a chest inventory");
    }

    @Test
    void testInvalidItemRange() {
        var builder = PageableInventory.builder().type(InventoryType.BEACON).slotRange();
        assertThrowsExactly(IllegalArgumentException.class, builder::build, "The slotRange can't be zero");
    }
}