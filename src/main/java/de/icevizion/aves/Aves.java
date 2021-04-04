package de.icevizion.aves;

import co.aikar.taskchain.BukkitTaskChainFactory;
import com.jsoniter.spi.JsoniterSpi;
import com.jsoniter.spi.TypeLiteral;
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
        System.out.println("Starting Aves v" + getDescription().getVersion());
        TypeLiteral.nativeTypes.put(ItemStack.class, TypeLiteral.NativeType.OBJECT);
        JsoniterSpi.registerTypeDecoder(ItemStack.class, new ItemStackDecoder());
        JsoniterSpi.registerTypeEncoder(ItemStack.class, new ItemStackEncoder());
        ServiceRegistry.registerService("TaskChainFactory", BukkitTaskChainFactory.create(this));
    }

    @Override
    public void onDisable() {
    }
}
