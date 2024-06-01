package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.util.LayoutCalculator;
import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.EnvTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@EnvTest
class GlobalInventoryBuilderTest {

    @Test
    void testGlobalBuilder(Env env) {
        var instance = env.createFlatInstance();
        var player = env.createPlayer(instance, Pos.ZERO);
        var builder = new GlobalInventoryBuilder(Component.text("Test"), InventoryType.CHEST_2_ROW);
        var layout = new InventoryLayoutImpl(builder.getType());
        var dataLayout = new InventoryLayoutImpl(builder.getType());

        dataLayout.setItems(LayoutCalculator.fillRow(InventoryType.CHEST_2_ROW), ItemStack.builder(Material.DIAMOND));

        layout.setItem(0, ItemStack.builder(Material.STONE).build());

        builder.setLayout(layout);
        builder.setDataLayoutFunction(inventoryLayout -> dataLayout);
        builder.setOpenFunction(event -> event.getPlayer().sendMessage("Test"));
        builder.setCloseFunction(event -> event.getPlayer().sendMessage("Test"));

        player.openInventory(builder.getInventory());
        assertTrue(builder.isOpen());
        player.closeInventory();

        builder.register();
        builder.unregister();


        assertNotNull(player);
        assertNotNull(builder);
        assertNotNull(builder.getLayout());
        builder.invalidateLayout();

        player.openInventory(builder.getInventory());
        builder.invalidateDataLayout();
        player.closeInventory();

        player.remove(true);
        env.destroyInstance(instance);
    }

    @Test
    void testListenerRegisterTwice(Env env) {
        var component = Component.text("Test");
        var instance = env.createFlatInstance();
        var builder = new GlobalInventoryBuilder(component, InventoryType.CHEST_2_ROW);
        builder.setLayout(InventoryLayout.fromType(builder.getType()));
        builder.setOpenFunction(event -> event.getPlayer().sendMessage(component));
        builder.setCloseFunction(event ->  event.getPlayer().sendMessage(component));
        builder.register();

        assertThrowsExactly(IllegalStateException.class, builder::register, "Can't register listener twice");

        builder.unregister();
        env.destroyInstance(instance);
    }
}