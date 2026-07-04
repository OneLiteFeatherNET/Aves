package net.theevilreaper.aves.map;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The {@link BaseMapBuilder} class is a builder implementation for creating instances of {@link net.theevilreaper.aves.map.BaseMap}.
 * It contains the necessary methods to set the properties of the map such as name, authors, and spawn position.
 * If you want to create a custom map, you can extend this class and implement the required methods.
 *
 * @author theEvilReaper
 * @version 1.2.0
 * @since 1.9.0
 */
public class BaseMapBuilder {

    public static final String DEFAULT_NAME = "Map";

    protected final List<String> builders;
    protected String name = DEFAULT_NAME;
    protected @Nullable Pos spawn;

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
    protected BaseMapBuilder(BaseMap baseMap) {
        this.name = baseMap.name();
        this.spawn = baseMap.spawn();
        this.builders = baseMap.builders() != null
                ? new ArrayList<>(baseMap.builders())
                : new ArrayList<>();
    }

    /**
     * Sets the name of the map.
     *
     * @param name the name of the map
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public BaseMapBuilder name(@Nullable String name) {
        this.name = Objects.requireNonNullElse(name, DEFAULT_NAME);
        return this;
    }

    /**
     * Adds a single builder to the map.
     *
     * @param builder the name of the builder to be added
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public BaseMapBuilder builder(String builder) {
        if (!this.builders.contains(builder)) {
            this.builders.add(builder);
        }
        return this;
    }

    /**
     * Adds multiple builders to the map, skipping any that are already present.
     *
     * @param builders the names of the builders to be added
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public BaseMapBuilder builders(String... builders) {
        for (String builder : builders) {
            builder(builder);
        }
        return this;
    }

    /**
     * Clears all previously added builders.
     *
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public BaseMapBuilder clearBuilders() {
        this.builders.clear();
        return this;
    }

    /**
     * Sets the spawn position for the map.
     *
     * @param spawn the position where the map will spawn
     * @return the current instance of {@link BaseMapBuilder} for method chaining
     */
    public BaseMapBuilder spawn(@Nullable Pos spawn) {
        this.spawn = spawn;
        return this;
    }

    /**
     * Builds and returns a new instance of {@link BaseMap} with the specified properties.
     *
     * @return a new instance of {@link BaseMap}
     */
    public BaseMap build() {
        return new BaseMap(this.name, this.spawn, this.builders);
    }

    /**
     * Checks whether the current name is still the default name.
     *
     * @return {@code true} if no custom name has been set, {@code false} otherwise
     */
    public boolean isDefaultName() {
        return DEFAULT_NAME.equals(this.name);
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
    public String getName() {
        return name;
    }

    /**
     * Returns the list of builders associated with the map.
     *
     * @return a list of builder names
     */
    @UnmodifiableView
    public List<String> getBuilders() {
        return Collections.unmodifiableList(builders);
    }
}
