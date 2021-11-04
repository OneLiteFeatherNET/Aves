package de.icevizion.aves;

import com.jsoniter.spi.JsoniterSpi;
import com.jsoniter.spi.TypeLiteral;
import de.icevizion.aves.decoder.ItemStackDecoder;
import de.icevizion.aves.encoder.ItemStackEncoder;
import net.minestom.server.extensions.Extension;
import net.minestom.server.item.ItemStack;

public final class Aves extends Extension {

    @Override
    public void initialize() {
        System.out.println("""

                    /\\                \s
                   /  \\__   _____  ___\s
                  / /\\ \\ \\ / / _ \\/ __|
                 / ____ \\ V /  __/\\__ \\
                /_/    \\_\\_/ \\___||___/""");
        System.out.println("Starting Aves....");
        TypeLiteral.nativeTypes.put(ItemStack.class, TypeLiteral.NativeType.OBJECT);
        JsoniterSpi.registerTypeDecoder(ItemStack.class, new ItemStackDecoder());
        JsoniterSpi.registerTypeEncoder(ItemStack.class, new ItemStackEncoder());
    }

    @Override
    public void terminate() {

    }
}
