package net.theevilreaper.aves.file;

import net.minestom.server.utils.validate.Check;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Internal utility class for file handling operations.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.14.0
 */
final class FileHandlerUtil {

    private FileHandlerUtil() {
    }

    /**
     * Prepares a target file path for saving by validating it is not a directory and ensuring parent directories exist.
     *
     * @param path   target path
     * @param logger logger instance to log warnings
     * @return true if the file does not exist yet, false otherwise
     */
    static boolean prepareSavePath(Path path, Logger logger) {
        Check.argCondition(Files.isDirectory(path), "Unable to save a directory. Please check the used path");
        boolean isNewFile = !Files.exists(path);
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException exception) {
            logger.warn("Unable to create directories for path: {}", path, exception);
        }
        return isNewFile;
    }
}
