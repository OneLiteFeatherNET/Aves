package de.icevizion.aves;

import com.jsoniter.spi.JsoniterSpi;
import de.icevizion.aves.decoder.ItemStackDecoder;
import de.icevizion.aves.encoder.ItemStackEncoder;
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
    }

    @Override
    public void onDisable() {
    }
}
