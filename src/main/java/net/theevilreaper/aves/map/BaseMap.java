package net.theevilreaper.aves.map;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The {@link BaseMap} defines a basic structure for a map which can be used in a game context or similar.
 * It contains some default values like the name, builders and spawn location.
 * The map object needs a separate mapper or parser to edit / load the given structure.
 * It should be extended to add more values and methods to the map structure.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 1.0.0
 */
public record BaseMap(String name, @Nullable Pos spawn, @Nullable String... builders) {

    /**
     * Creates a new instance of the {@link BaseMapBuilder} to build a new map.
     * The builder can be used to set all values that are required for a map.
     *
     * @return a new instance of the {@link BaseMapBuilder}
     */
    @Contract(pure = true)
    public static BaseMapBuilder builder() {
        return new BaseMapBuilder();
    }

    /**
     * Creates a new instance from the {@link BaseMap} with all given values.
     *
     * @param baseMap the base map to copy
     * @return a new instance of the {@link BaseMapBuilder} with the given values
     */
    @Contract(value = "_ -> new", pure = true)
    public static BaseMapBuilder builder(BaseMap baseMap) {
        return new BaseMapBuilder(baseMap);
    }

    /**
     * Returns the spawn location or the default spawn location if the spawn is null.
     *
     * @param defaultSpawn the default spawn location
     * @return the spawn location or the default spawn location
     */
    public Pos getSpawnOrDefault(Pos defaultSpawn) {
        return spawn != null ? spawn : defaultSpawn;
    }
}