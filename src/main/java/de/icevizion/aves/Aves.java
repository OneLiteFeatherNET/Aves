package de.icevizion.aves;

import com.jsoniter.spi.JsoniterSpi;
import de.icevizion.aves.decoder.ItemStackDecoder;
import de.icevizion.aves.encoder.ItemStackEncoder;
import de.icevizion.aves.item.ItemBuilder;
import de.icevizion.aves.inventory.GlobalInventoryBuilder;
import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.InventoryRows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class Aves extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("\n" +
            "    /\\                 \n" +
            "   /  \\__   _____  ___ \n" +
            "  / /\\ \\ \\ / / _ \\/ __|\n" +
            " / ____ \\ V /  __/\\__ \\\n" +
            "/_/    \\_\\_/ \\___||___/");
        System.out.printf("Starting Aves v%s by %s%n",
            getDescription().getVersion(), getDescription().getAuthors());
        JsoniterSpi.registerTypeDecoder(ItemStack.class, new ItemStackDecoder());
        JsoniterSpi.registerTypeEncoder(ItemStack.class, new ItemStackEncoder());



        var builder = new GlobalInventoryBuilder("Navigator", InventoryRows.THREE, inventoryLayout -> {
            //Data loading
            var layout = inventoryLayout == null ? new InventoryLayout(InventoryRows.THREE.getSize()) : inventoryLayout;

            var counter = 0;
            for (Player player : Bukkit.getOnlinePlayers()) {
                layout.item(counter, new ItemBuilder(Material.SKELETON_SKULL).setDisplayName(player.getName()), event -> {});
                counter++;
            }

            return layout;
        });

        //Configure layout
        var layout = builder.getInventoryLayout();
        layout.item(9, new ItemBuilder(Material.STONE), event -> {});

        //Retrieve inventory to open it by a player
        builder.getInventory();
    }

    @Override
    public void onDisable() {
    }
}
