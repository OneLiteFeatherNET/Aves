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
        System.out.println(String.format("Starting Aves v%s by %s",
                getDescription().getVersion(), getDescription().getAuthors()));
    }

    @Override
    public void onDisable() {
    }
}
