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
        System.out.println("Starting Aves v" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
    }
}
