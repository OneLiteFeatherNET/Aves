package de.icevizion.aves.reflection;

import org.bukkit.Bukkit;

/**
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0
 * @since 18/04/2020 15:44
 */

@Deprecated
public class ServerHelper {

    /**
     * Returns the current TPS from the server.
     * @return The TPS values as double
     */

    public static double[] getTps() {
        return Bukkit.getTPS();
    }
}