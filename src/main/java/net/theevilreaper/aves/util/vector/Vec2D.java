package net.theevilreaper.aves.util.vector;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.utils.MathUtils;
import org.jetbrains.annotations.Contract;

/**
 * The {@link Vec2D} class represents a vector in a two-dimensional space with x and y coordinates.
 *
 * @param x coordinate of the vector
 * @param y coordinate of the vector
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public record Vec2D(double x, double y) {

    public static final Vec2D ZERO = new Vec2D(0, 0);
    public static final Vec2D UNIT_X = new Vec2D(1, 0);
    public static final Vec2D UNIT_Y = new Vec2D(0, 1);

    /**
     * Creates a 2d vector from a {@link Pos}.
     *
     * @param pos The input pos
     */
    public Vec2D(Pos pos) {
        this(pos.blockX(), pos.blockZ());
    }

    /**
     * Creates a 2d vector from a {@link Vec}.
     *
     * @param vec The input vec
     */
    public Vec2D(Vec vec) {
        this(vec.blockX(), vec.blockZ());
    }

    /**
     * Adds the given values to the vector and returns the result in a new vector.
     *
     * @param x x value to add
     * @param y y value to add
     * @return result vector
     */
    @Contract("_, _ -> new")
    public Vec2D add(double x, double y) {
        return new Vec2D(this.x + x, this.y + y);
    }

    /**
     * Adds the vector to a given vector and returns the result in a new vector.
     *
     * @param vector vector to add
     * @return result vector
     */
    @Contract("_ -> new")
    public Vec2D add(Vec2D vector) {
        return new Vec2D(this.x + vector.x, this.y + vector.y);
    }

    /**
     * Subtracts the given values from the vector and returns the result in a new vector.
     *
     * @param x x value to add
     * @param y y value to add
     * @return result vector
     */
    @Contract("_, _ -> new")
    public Vec2D sub(double x, double y) {
        return new Vec2D(this.x - x, this.y - y);
    }

    /**
     * Subtracts the given vector from the vector and returns the result in a new vector.
     *
     * @param vector vector to subtract
     * @return result vector
     */
    @Contract("_ -> new")
    public Vec2D sub(Vec2D vector) {
        return new Vec2D(this.x - vector.x, this.y - vector.y);
    }

    /**
     * Creates the negation of a vector and returns the result in a new vector.
     *
     * @return result vector
     */
    @Contract(" -> new")
    public Vec2D neg() {
        return new Vec2D(-x, -y);
    }

    /**
     * Multiplicates the given value to the vector and returns the result in a new vector.
     *
     * @param multiplier value to multiply
     * @return result vector
     */
    @Contract("_ -> new")
    public Vec2D scalarMul(int multiplier) {
        return new Vec2D(this.x * multiplier, this.y * multiplier);
    }

    /**
     * Returns the length of the vector with a squared calculation.
     *
     * @return the calculated length
     */
    public double lengthSquared() {
        return MathUtils.square(x) + MathUtils.square(y);
    }

    /**
     * Calculates the length of the vector.
     *
     * @return length of the vector
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }
}