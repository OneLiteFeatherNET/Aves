package de.icevizion.aves;

import net.minestom.server.extensions.Extension;
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
    }

    @Override
    public void terminate() {
        // Nothing to do
    }
}
