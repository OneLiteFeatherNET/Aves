package net.theevilreaper.aves.map;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * The {@link BaseMap} defines a basic structure for a map which can be used in a game context or similar.
 * It contains some default values like the name, builders and spawn location.
 * The map object needs a separate mapper or parser to edit / load the given structure.
 * It should be extended to add more values and methods to the map structure.
 *
 * @author theEvilReaper
 * @version 1.0.5
 * @since 1.0.0
 */
public class BaseMap {

    private String name;
    private String[] builders;
    private Pos spawn;

    /**
     * Empty constructor to create a new instance of the {@link BaseMap} with any values.
     * Sometimes such constructors are required for serialization or deserialization.
     */
    public BaseMap() {
    }

    /**
     * Creates a new reference from the {@link BaseMap}.
     * It requires all values which are needed to create a map.
     *
     * @param name     the name from the map
     * @param builders the builders from the map
     * @param spawn    the spawn location from the map
     */
    public BaseMap(@NotNull String name, Pos spawn, String... builders) {
        Check.argCondition(name.trim().isEmpty(), "The name can not be null or empty");
        this.name = name;
        this.builders = builders;
        this.spawn = spawn;
    }

    /**
     * Creates a new instance from the {@link BaseMap} with all given values.
     * Deprecated since 1.9.0, use {@link #builder()} instead.
     *
     * @param name the name from the map
     */
    @Deprecated(forRemoval = true, since = "1.9.0")
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BaseMap of(@NotNull String name) {
        return new BaseMap(name, null);
    }

    /**
     * The constructor sets all relevant values for a map.
     * Deprecated since 1.9.0, use {@link #builder()} instead.
     *
     * @param name  the name from the map
     * @param spawn the spawn location from the map
     */
    @Deprecated(forRemoval = true, since = "1.9.0")
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull BaseMap of(@NotNull String name, Pos spawn) {
        return new BaseMap(name, spawn, "team");
    }

    /**
     * The constructor sets all relevant values for a map.
     * Deprecated since 1.9.0, use {@link #builder()} instead.
     *
     * @param name     the name from the map
     * @param builders the builders from the map
     * @param spawn    the spawn location from the map
     */
    @Contract(value = "_, _, _ -> new", pure = true)
    @Deprecated(forRemoval = true, since = "1.9.0")
    public static @NotNull BaseMap of(@NotNull String name, Pos spawn, String... builders) {
        return new BaseMap(name, spawn, builders);
    }

    /**
     * Creates a new instance of the {@link BaseMapBuilder} to build a new map.
     * The builder can be used to set all values which are required for a map.
     *
     * @return a new instance of the {@link BaseMapBuilder}
     */
    @Contract(pure = true)
    public static @NotNull BaseMapBuilder builder() {
        return new BaseMapBuilder();
    }

    /**
     * Creates a new instance from the {@link BaseMap} with all given values.
     *
     * @param baseMap the base map to copy
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull BaseMapBuilder builder(@NotNull BaseMap baseMap) {
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
     * Returns a hash value from some data which are provided by the object.
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
     * Set the name of a map
     *
     * @param name the name to set
     */
    public void setName(@NotNull String name) {
        Check.argCondition(name.trim().isEmpty(), "The name can not be null or empty");
        this.name = name;
    }

    /**
     * Set the builder of a map
     *
     * @param builders the builder's to set
     */
    public void setBuilders(@Nullable String... builders) {
        this.builders = builders;
    }

    /**
     * Set the spawn location of a map
     *
     * @param spawn the spawn location to set
     */
    public void setSpawn(@Nullable Pos spawn) {
        this.spawn = spawn;
    }

    /**
     * Checks if the map has a spawn location.
     *
     * @return true if the map has a spawn location otherwise false
     */
    public boolean hasSpawn() {
        return spawn != null;
    }

    /**
     * Returns the map name.
     *
     * @return the name of the map
     */
    public @Nullable String getName() {
        return name;
    }

    /**
     * Returns the builders.
     *
     * @return the builders of the map
     */
    public @Nullable String[] getBuilders() {
        return builders;
    }

    /**
     * Returns the spawn location
     *
     * @return the spawn of the map
     */
    public @Nullable Pos getSpawn() {
        return spawn;
    }

    /**
     * Returns the spawn location or the default spawn location if the spawn is null.
     *
     * @param defaultSpawn the default spawn location
     * @return the spawn location or the default spawn location
     */
    public @NotNull Pos getSpawnOrDefault(@NotNull Pos defaultSpawn) {
        return spawn != null ? spawn : defaultSpawn;
    }
}