package net.theevilreaper.aves.map;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

/**
 * The {@link BaseMap} defines a basic structure for a map which can be used in a game context or similar.
 * It contains some default values like the name, builders and spawn location.
 * The map object needs a separate mapper or parser to edit / load the given structure.
 * It should be extended to add more values and methods to the map structure.
 *
 * @author theEvilReaper
 * @version 1.1.2
 * @since 1.0.0
 */
public class BaseMap {

    private final String name;
    private final @Nullable Pos spawn;
    private final @Nullable List<String> builders;

    /**
     * Creates a new instance of the {@link BaseMap} with the given values.
     *
     * @param name     of the map
     * @param spawn    of the map
     * @param builders of the map
     */
    public BaseMap(String name, @Nullable Pos spawn, @Nullable List<String> builders) {
        this.name = name;
        this.spawn = spawn;
        this.builders = builders;
    }

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

    public String name() {
        return name;
    }

    public @Nullable Pos spawn() {
        return spawn;
    }

    public @Nullable List<String> builders() {
        return builders;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BaseMap) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.spawn, that.spawn) &&
                Objects.equals(this.builders, that.builders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, spawn, builders);
    }

    @Override
    public String toString() {
        return "BaseMap[" +
                "name=" + name + ", " +
                "spawn=" + spawn + ", " +
                "builders=" + builders + ']';
    }
}
