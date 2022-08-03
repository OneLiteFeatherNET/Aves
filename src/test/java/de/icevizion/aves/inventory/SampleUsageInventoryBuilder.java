package de.icevizion.aves.inventory;

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
        var test = new GlobalInventoryBuilder("Just a test", InventoryType.CHEST_1_ROW);
        test.setLayout(new InventoryLayout(InventoryType.CHEST_1_ROW));
        test.setDataLayoutFunction(SampleUsageInventoryBuilder::set);
        test.invalidateDataLayout();
        var inv = test.getInventory();
    }

    private static InventoryLayout set(InventoryLayout inventoryLayout) {
        // throw new NullPointerException("YOLO");
        inventoryLayout.setItem(0, ItemStack.builder(Material.OAK_DOOR).build());
        return inventoryLayout;
    }
}
