package net.theevilreaper.aves.map;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@link BaseMapBuilder} class is a builder implementation for creating instances of {@link net.theevilreaper.aves.map.BaseMap}.
 * It contains the necessary methods to set the properties of the map such as name, authors, and spawn position.
 * If you want to create a custom map, you can extend this class and implement the required methods.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.9.0
 */
public class BaseMapBuilder {

    protected final List<String> builders;
    protected String name;
    protected Pos spawn;

    /**
     * Constructs a new {@link BaseMapBuilder} instance with an empty list of builders.
     * This constructor is typically used when creating a new map without any predefined builders.
     */
    protected BaseMapBuilder() {
        this.builders = new ArrayList<>();
    }

    /**
     * Constructs a new {@link BaseMapBuilder} instance using the properties from an existing {@link BaseMap}.
     *
     * @param baseMap the base map to copy properties from
     */
    protected BaseMapBuilder(@NotNull BaseMap baseMap) {
        this.name = baseMap.getName();
        this.spawn = baseMap.getSpawn();
        if (baseMap.getBuilders() == null) {
            this.builders = new ArrayList<>();
        } else {
            // Copy the builders from the base map to the new list
            // This ensures that we do not modify the original list in the base map
            // and allows us to add new builders if needed.
            this.builders = new ArrayList<>(List.of(baseMap.getBuilders()));
        }
    }

    /**
     * Sets the name of the map.
     *
     * @param name the name of the map
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public @NotNull BaseMapBuilder name(@NotNull String name) {
        this.name = name;
        return this;
    }

    /**
     * Adds a single builder to the map.
     *
     * @param builder the name of the builder to be added
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public @NotNull BaseMapBuilder builder(@NotNull String builder) {
        this.builders.add(builder);
        return this;
    }

    /**
     * Adds multiple builders to the map.
     *
     * @param builders the names of the builders to be added
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public @NotNull BaseMapBuilder builders(@NotNull String... builders) {
        this.builders.addAll(List.of(builders));
        return this;
    }

    /**
     * Sets the spawn position for the map.
     *
     * @param spawn the position where the map will spawn
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public @NotNull BaseMapBuilder spawn(@Nullable Pos spawn) {
        this.spawn = spawn;
        return this;
    }

    /**
     * Builds and returns a new instance of {@link BaseMap} with the specified properties.
     *
     * @return a new instance of {@link BaseMap}
     */
    public @NotNull BaseMap build() {
        return new BaseMap(name, spawn, builders.toArray(new String[0]));
    }

    /**
     * Returns the spawn position of the map.
     *
     * @return the spawn position, or null if not set
     */
    public @Nullable Pos getSpawn() {
        return spawn;
    }

    /**
     * Returns the name of the map.
     *
     * @return the name of the map, or null if not set
     */
    public @Nullable String getName() {
        return name;
    }

    /**
     * Returns the list of builders associated with the map.
     *
     * @return a list of builder names
     */
    public @NotNull List<String> getBuilders() {
        return builders;
    }
}
