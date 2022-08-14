package de.icevizion.aves.util;

import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.particle.Particle;
import net.minestom.server.particle.ParticleCreator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.20
 **/
public class Particles {

    private static final int DEFAULT_PARTICLE_AMOUNT = 2;

    private static final int MIN_SIZE_Y = 2;
    private static List<Vec> pointCache = new ArrayList<>();

    private Particles() {}

    /**
     * Displays a particle line from a start point to an end point
     * @param particle The particle typ for the line
     * @param player The player who sees the line
     * @param start The start point as {@link Vec}
     * @param end The end point as {@link Vec}
     * @param distance
     */
    //TODO: Rename "distance" variable
    public static void playLine(@NotNull Particle particle, @NotNull Player player, @NotNull Vec start, @NotNull Vec end, float distance) {
        //Calculates the difference between the given vectors
        var deltaVec = end.sub(start);

        //Calculates the amount of points to display
        var points = (int) Math.round(deltaVec.length() / distance);

        deltaVec = deltaVec.mul(1f / points);

        //Sets the current vec to the start point
        Vec current = start;

        //Displays the start point
        playPoint(particle, player, current);

        //Loops through the amount of points
        for (int i = 0; i < points; i++) {
            current = current.add(deltaVec);
            playPoint(particle, player, current);
        }
    }

    public static void playLine(@NotNull Particle particle, @NotNull Player player, @NotNull List<Vec> vectors) {
        if (vectors.isEmpty()) return;
        for (int i = 0; i < vectors.size(); i++) {
            playPoint(particle, player, vectors.get(i));
        }
    }

    public static void playCuboid(@NotNull Particle particle, @NotNull Player player, @NotNull Vec min, @NotNull Vec max, double particleDistance) {
        if (max.blockY() - min.blockY() < MIN_SIZE_Y) {
            throw new IllegalArgumentException("The difference from the points of the y-axis is to small. The minimum is " + MIN_SIZE_Y);
        }

        if (!pointCache.isEmpty()) {
            loopList(particle, player);
            return;
        }

        var points = new ArrayList<Vec>();

        double minX = Math.min(min.blockX(), max.blockX());
        double minY = Math.min(min.blockY(), max.blockY());
        double minZ = Math.min(min.blockZ(), max.blockZ());
        double maxX = Math.max(min.blockX(), max.blockX()) + 1D;
        double maxY = Math.max(min.blockY(), max.blockY()) + 1D;
        double maxZ = Math.max(min.blockZ(), max.blockZ()) + 1D;

        for (double x = minX; x <= maxX; x = (x + particleDistance)) {
            for (double y = minY; y <= maxY; y = (y + particleDistance)) {
                for (double z = minZ; z <= maxZ; z = (z + particleDistance)) {
                    int components = 0;
                    if (x == minX || x == maxX) components++;
                    if (y == minY || y == maxY) components++;
                    if (components == 0) continue;
                    if (z == minZ || z == maxZ) components++;
                    if (components >= 2) {
                        points.add(new Vec(x, y, z));
                    }
                }
            }
        }

        if (points.isEmpty()) return;
        pointCache = points;

        loopList(particle, player);
    }

    public static void playPoint(@NotNull Particle particle, @NotNull Player player, @NotNull Vec pos, int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("The amount can not be zero or negativ");
        }
        player.sendPacket(ParticleCreator.createParticlePacket(
                particle,
                pos.x(),
                pos.y(),
                pos.z(),
                0,
                0,
                0,
                amount));
    }

    public static void playPoint(@NotNull Particle particle, @NotNull Player player, @NotNull Vec pos) {
        player.sendPacket(ParticleCreator.createParticlePacket(
                particle,
                pos.x(),
                pos.y(),
                pos.z(),
                0,
                0,
                0,
                DEFAULT_PARTICLE_AMOUNT));
    }

    @ApiStatus.Internal
    private static void loopList(@NotNull Particle particle, @NotNull Player player) {
        if (pointCache == null || pointCache.isEmpty()) return;

        for (int i = 0; i < pointCache.size(); i++) {
            playPoint(particle, player, pointCache.get(i));
        }
    }
}
