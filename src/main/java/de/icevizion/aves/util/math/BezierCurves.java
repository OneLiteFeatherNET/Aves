package de.icevizion.aves.util.math;

import de.icevizion.aves.util.Particles;
import de.icevizion.aves.util.vector.Vec2D;
import net.minestom.server.coordinate.Vec;
import net.minestom.server.entity.Player;
import net.minestom.server.particle.Particle;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public final class BezierCurves {

    private BezierCurves() {}

    public static Vec2D quadraticBezier(Vec start, Vec control, Vec end, float delta) {
        var xFinal = Math.pow(1 - delta, 2) * start.x() + (1- delta) * 2 * delta * control.x() +
                delta * delta * end.x();

        var zFinal = Math.pow(1 - delta, 2) * start.z() + (1 - delta) * 2 * delta * control.z()
                + delta * delta * end.z();

        return new Vec2D(xFinal, zFinal);
    }

    public static void test(Player player, Vec start, Vec control, Vec end) {
        for (float t = 0; t <= 1; t+= 0.01) {
            var xFinal = quadraticBezier(start.x(), control.x(), end.x(), t);
            var yFinal = quadraticBezier(start.y(), control.y(), end.y(), t);
            var zFinal  = quadraticBezier(start.z(), control.z(), end.z(), t);
            Particles.playPoint(Particle.WITCH, player, new Vec(xFinal, yFinal, zFinal));
        }
    }

    public static double quadraticBezier(double p1, double p2, double p3, float t) {
        return ( 1.0 - t ) * (( 1.0 - t ) * p1 + t * p2) + t * (( 1.0 - t ) * p2 + t * p3);
    }

}
