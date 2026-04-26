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
     * Overrides the equal the method from the object class.
     *
     * @param o the object to compare
     * @return true if the given object is the same otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMap baseMap = (BaseMap) o;
        return name.equals(baseMap.name);
    }

    /**
     * Returns a hash value from some data that are provided by the object.
     * In general, the hash relies on the unique data.
     * For the basic implementation that is only the name of the map.
     *
     * @return a created hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Returns the builders.
     *
     * @return the builders of the map
     */
    @Override
    public @Nullable String[] builders() {
        return builders;
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