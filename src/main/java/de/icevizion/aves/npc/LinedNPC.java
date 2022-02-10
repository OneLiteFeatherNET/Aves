package de.icevizion.aves.npc;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class LinedNPC extends MinestomNPC {

    private final Hologram[] lines;

    public LinedNPC(@NotNull UUID uuid, @NotNull Instance instance, @NotNull Component displayName, @NotNull PlayerSkin playerSkin, @NotNull Pos spawnPos, Component... lines) {
        super(uuid, instance, displayName, playerSkin, spawnPos);
        this.lines = new Hologram[lines.length];

        for (int i = 0; i < lines.length; i++) {
            var component = lines[i];
            var hologram = new Hologram(instance, new Pos(spawnPos).add(0, 1.7 + (0.3 * i), 0), component);
            this.lines[i] = hologram;
        }
    }

    public void destroyLines() {
        if (lines.length == 0) return;
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].isRemoved()) continue;
            lines[i].remove();
        }
    }
}
