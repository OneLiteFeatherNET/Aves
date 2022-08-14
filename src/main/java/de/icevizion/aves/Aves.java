package de.icevizion.aves;

import com.jsoniter.spi.JsoniterSpi;
import com.jsoniter.spi.TypeLiteral;
import de.icevizion.aves.file.jsoniter.ItemStackDecoder;
import de.icevizion.aves.file.jsoniter.ItemStackEncoder;
import net.minestom.server.extensions.Extension;
import net.minestom.server.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Aves extends Extension {

    private static final Logger AVES_LOGGER = LoggerFactory.getLogger(Aves.class);

    @Override
    public void initialize() {
        AVES_LOGGER.info("""

                    /\\                \s
                   /  \\__   _____  ___\s
                  / /\\ \\ \\ / / _ \\/ __|
                 / ____ \\ V /  __/\\__ \\
                /_/    \\_\\_/ \\___||___/""");
        AVES_LOGGER.info("Starting Aves....");
        TypeLiteral.nativeTypes.put(ItemStack.class, TypeLiteral.NativeType.OBJECT);
        JsoniterSpi.registerTypeDecoder(ItemStack.class, new ItemStackDecoder());
        JsoniterSpi.registerTypeEncoder(ItemStack.class, new ItemStackEncoder());
    }

    @Override
    public void terminate() {
        // Nothing to do
    }
}
