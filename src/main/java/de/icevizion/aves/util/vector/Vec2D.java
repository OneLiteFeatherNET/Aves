package de.icevizion.aves.util.vector;

import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class Vec2D implements Cloneable, Comparable<Vec2D> {

    public static Vec2D ZERO = new Vec2D(0,0);

    private int x;
    private int z;

    public Vec2D(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public static Vec2D of(int x, int z) {
        return new Vec2D(x, z);
    }

    public static Vec2D of(@NotNull Pos pos) {
        return new Vec2D(pos.blockX(), pos.blockZ());
    }

    public static Vec2D of(@NotNull Vec vec) {
        return new Vec2D(vec.blockX(), vec.blockZ());
    }

    public Vec2D add(int x, int z) {
        this.x += x;
        this.z += z;
        return this;
    }

    public Vec2D add(Vec2D vec2) {
        x += vec2.x;
        z += vec2.z;
        return this;
    }

    public Vec2D sub(int x, int z) {
        this.x -= x;
        this.z -= z;
        return this;
    }

    public Vec2D multi(int x, int z) {
        this.x *= x;
        this.z *= z;
        return this;
    }

    public Vec2D div(int x, int z) {
        this.x /= x;
        this.z /= z;
        return this;
    }

    public Vec2D zero() {
        this.x = 0;
        this.z = 0;
        return this;
    }

    public int length() {
        return (int) Math.sqrt(x * x + z * z);
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int compareTo(@NotNull Vec2D vec2D) {
        int deltaX = Double.compare(getX(), vec2D.getX());
        return deltaX != 0 ? deltaX : Double.compare(getZ(), vec2D.getZ());
    }

    @Override
    public String toString() {
        return "Vec2D{" +
                "x=" + x +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec2D vec2D = (Vec2D) o;
        return x == vec2D.x && z == vec2D.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }

    @Override
    protected Vec2D clone() throws CloneNotSupportedException {
        return (Vec2D) super.clone();
    }
}