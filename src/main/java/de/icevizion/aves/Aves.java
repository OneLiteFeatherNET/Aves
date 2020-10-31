package de.icevizion.aves;

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

       /*CloudService cloudService = getService(CloudService.class);

      //Inventory Builder
	    InventoryService inventoryService = new InventoryService();
        addService(inventoryService);

        InventoryBuilderListener inventoryListener = new InventoryBuilderListener(cloudService);

        getServer().getPluginManager().registerEvents(
                new InventoryBuilderClickListener(inventoryListener), this);
        getServer().getPluginManager().registerEvents(
                new InventoryBuilderCloseListener(inventoryListener), this);
        getServer().getPluginManager().registerEvents(
                new InventoryBuilderDragListener(inventoryListener), this);

        //Name Tags
        NameTagService nameTagService = new NameTagService(cloudService);
        addService(nameTagService);

        getServer().getPluginManager().registerEvents(
                new NameTagPlayerJoinListener(nameTagService), this);
        getServer().getPluginManager().registerEvents(
                new NameTagPlayerQuitListener(nameTagService), this);*/
    }

    @Override
    public void onDisable() {
    }
}
