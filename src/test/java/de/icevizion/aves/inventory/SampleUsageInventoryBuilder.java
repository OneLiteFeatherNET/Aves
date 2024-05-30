package de.icevizion.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.bungee.BungeeCordProxy;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;

public class SampleUsageInventoryBuilder {
    public static void main(String[] args) {
        var server = MinecraftServer.init();
        BungeeCordProxy.enable();
        server.start("localhost", 25577);
        var test = new GlobalInventoryBuilder(Component.text("Just a test"), InventoryType.CHEST_1_ROW);
        test.setLayout(new InventoryLayoutImpl(InventoryType.CHEST_1_ROW));
        test.setDataLayoutFunction(SampleUsageInventoryBuilder::set);
        test.invalidateDataLayout();
        var inv = test.getInventory();
    }

    private static InventoryLayout set(InventoryLayout layout) {
        // throw new NullPointerException("YOLO");
        layout.setItem(0, ItemStack.builder(Material.OAK_DOOR).build());
        return layout;
    }
}
