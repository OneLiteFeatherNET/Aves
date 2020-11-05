package de.icevizion.aves.util;

/**
 * Taken from: https://www.spigotmc.org/threads/gradient-chat-particles.470496/#post-3985977
 */

@FunctionalInterface
public interface Interpolator {

    double[] interpolate(double from, double to, int max);
}
