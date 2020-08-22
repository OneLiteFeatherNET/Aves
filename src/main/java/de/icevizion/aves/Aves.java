package de.icevizion.aves;

import net.titan.spigot.CloudService;
import net.titan.spigot.plugin.Plugin;

public final class Aves extends Plugin {

    private CloudService cloudService;

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

        cloudService = getService(CloudService.class);
    }

    @Override
    public void onDisable() {
    }
}
