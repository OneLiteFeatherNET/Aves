package de.icevizion.aves.util.math;

import de.icevizion.aves.util.vector.Vec2D;
import net.minestom.server.coordinate.Vec;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public final class BezierCurves {

    private BezierCurves() {}

    @Contract("_, _, _, _ -> new")
    public static @NotNull Vec2D quadraticBezier(@NotNull Vec start, @NotNull Vec control, @NotNull Vec end, float delta) {
        var xFinal = Math.pow(1 - delta, 2) * start.x() + (1- delta) * 2 * delta * control.x() +
                delta * delta * end.x();

        var zFinal = Math.pow(1 - delta, 2) * start.z() + (1 - delta) * 2 * delta * control.z()
                + delta * delta * end.z();

        return new Vec2D(xFinal, zFinal);
    }

    public static double quadraticBezier(double p1, double p2, double p3, float t) {
        return ( 1.0 - t ) * (( 1.0 - t ) * p1 + t * p2) + t * (( 1.0 - t ) * p2 + t * p3);
    }
}