package net.theevilreaper.aves.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The {@link BaseMapEntry} is a basic implementation of the {@link MapEntry} interface.
 * It will store the root directory of the map and the map file name.
 *
 * @since 1.6.0
 * @version 1.1.0
 * @author theEvilReaper
 */
public final class BaseMapEntry implements MapEntry {

    private final String mapFileNaming;
    private final Path directory;
    private Path mapFilePath;

    /**
     * Creates a new instance of the map entry.
     *
     * @param directory the root directory of the map
     * @param mapFileNaming the name of the map file
     */
    BaseMapEntry(@NotNull Path directory, @NotNull String mapFileNaming) {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("The given path must be a directory");
        }

        this.directory = directory;
        this.mapFileNaming = mapFileNaming;
        Path resolvedPath = directory.resolve(mapFileNaming);
        if (Files.exists(resolvedPath)) {
            this.mapFilePath = resolvedPath;
        }
    }

    /**
     * Refreshes the map entry. It will check if the map file exists.
     */
    @Override
    public void refresh() {
        this.mapFilePath = this.directory.resolve(this.mapFileNaming);
    }

    /**
     * Checks if the map has a standard ending.
     *
     * @return {@code true} if the map has a standard ending
     */
    @Override
    public boolean hasStandardEnding() {
        return this.mapFileNaming.equals(MAP_FILE);
    }

    /**
     * Checks if the map has a map file.
     *
     * @return {@code true} if the map has a map file
     */
    @Override
    public boolean hasMapFile() {
        return this.mapFilePath != null;
    }


    /**
     * Returns the root directory of the map.
     *
     * @return the root directory
     */
    @Override
    public @NotNull Path getDirectoryRoot() {
        return this.directory;
    }

    /**
     * Returns the map file name.
     *
     * @return the map file name
     */
    @Override
    public @Nullable Path getMapFile() {
        return this.mapFilePath;
    }
}
