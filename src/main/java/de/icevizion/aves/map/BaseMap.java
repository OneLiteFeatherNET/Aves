package de.icevizion.aves.map;

import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The map class represents a summary with all relevant map data
 * This contains some default values like the name, builders and spawn location
 * The map object needs a separate mapper or parser to edit / load the given structure
 *
 * @author theEvilReaper
 * @since 1.0.0
 * @version 1.0.4
 */
public class BaseMap {

    private String name;
    private String[] builders;
    private Pos spawn;

    /**
     * Empty constructor for jsoniter.
     */
    public BaseMap() { }

    /**
     * The constructor sets all relevant values for a map.
     *
     * @param name     The name from the map
     * @param builders The builders from the map
     * @param spawn    The spawn location from the map
     */
    public BaseMap(@NotNull String name, Pos spawn, String... builders) {
        if (name.isEmpty()) {
            throw new IllegalArgumentException("The name can not be null or empty");
        }
        this.name = name;
        this.builders = builders;
        this.spawn = spawn;
    }

    /**
     * Creates a new instance from the {@link BaseMap} with all given values.
     * This method should only be used at runtime
     *
     * @param name     The name from the map
     */
    public static BaseMap of(String name) {
        return new BaseMap(name, null);
    }

    /**
     * The constructor sets all relevant values for a map.
     * This method should only be used at runtime
     *
     * @param name     The name from the map
     * @param spawn    The spawn location from the map
     */
    public static BaseMap of(String name, Pos spawn) {
        return new BaseMap(name, spawn, "Team");
    }

    /**
     * The constructor sets all relevant values for a map.
     * This method should only be used at runtime
     *
     * @param name     The name from the map
     * @param builders The builders from the map
     * @param spawn    The spawn location from the map
     */
    public static BaseMap of(String name, Pos spawn, String... builders) {
        return new BaseMap(name, spawn, builders);
    }

    /**
     * Overrides the equal the method from the object class.
     * @param o The object to compare
     * @return True if the given object is the same otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseMap baseMap = (BaseMap) o;
        return name.equals(baseMap.name);
    }

    /**
     * Overrides hashCode from the object class.
     * @return A hash value
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Set the name of a map
     * @param name The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the builder of a map
     * @param builders The builder's to set
     */
    public void setBuilders(String... builders) {
        this.builders = builders;
    }

    /**
     * Set the spawn location of a map
     * @param spawn The spawn location to set
     */
    public void setSpawn(Pos spawn) {
        this.spawn = spawn;
    }

    /**
     * Returns the map name.
     *
     * @return The name of the map
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the builders.
     *
     * @return The builders of the map
     */
    public String[] getBuilders() {
        return builders;
    }

    /**
     * Returns the spawn location
     *
     * @return The spawn of the map
     */
    public Pos getSpawn() {
        return spawn;
    }
}