package de.cosmiqglow.aves;

import org.bukkit.plugin.java.JavaPlugin;

public class Aves extends JavaPlugin {

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
        super.onEnable();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
