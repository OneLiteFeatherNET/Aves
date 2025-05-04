package net.theevilreaper.aves.map;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

/**
 * The {@link MapEntry} is an object which stores a reference to a {@link Path} which contains data about a world in Minecraft.
 * Also, this path can contain some data about the map. This data is stored in a specific file.
 * The name from the file can be set when a new object is created via the given ways.
 * When the creation doesn't receive the specific file ending a default one is used to avoid issues
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.6.0
 */
public sealed interface MapEntry permits BaseMapEntry {

    @NotNull String MAP_FILE = "map.json";

    /**
     * Creates a new MapEntry from the given path.
     * It uses the {@code MAP_FILE} constant to find the file which contains the data
     *
     * @param directoryRoot the path for the entry
     * @return the created reference
     */
    @Contract(pure = true, value = "_ -> new")
    static @NotNull MapEntry of(@NotNull Path directoryRoot) {
        return new BaseMapEntry(directoryRoot, MAP_FILE);
    }

    /**
     * Creates a new MapEntry from the given path.
     *
     * @param directoryRoot the path for the entry
     * @param mapFileNaming the name of the file which contains the data
     * @return the created reference
     */
    @Contract(pure = true, value = "_, _ -> new")
    static @NotNull MapEntry of(@NotNull Path directoryRoot, @NotNull String mapFileNaming) {
        return new BaseMapEntry(directoryRoot, mapFileNaming);
    }

    /**
     * Creates a new MapEntry from the given path.
     */
    void createFile();

    /**
     * If the method is called the entry checks if the data file exists.
     * This can be used to update the path references in special cases.
     */
    @Deprecated(forRemoval = true, since = "Please use createFile instead")
    void refresh();

    /**
     * Returns a boolean indicator if the given file which contains the data is the default
     *
     * @return true when the file is the default variant otherwise false
     */
    boolean hasStandardEnding();

    /**
     * Returns a boolean indicator if the given path contains a data file.
     *
     * @return true when yes otherwise false
     */
    boolean hasMapFile();

    /**
     * Returns the path to the directory which contains the map data.
     *
     * @return the given path reference
     */
    @NotNull Path getDirectoryRoot();

    /**
     * Returns the path to the file which stores information about a map.
     * It can be nullable when the file doesn't contain the specific file
     *
     * @return the given path reference
     */
    @Nullable Path getMapFile();
}
