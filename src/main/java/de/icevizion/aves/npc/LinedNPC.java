package de.icevizion.aves.npc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.entity.hologram.Hologram;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class LinedNPC extends MinestomNPC {

    private final Hologram[] lines;

    public LinedNPC(@NotNull UUID uuid, @NotNull Instance instance, @NotNull String name, @NotNull Component displayName, @NotNull PlayerSkin playerSkin, @NotNull Pos spawnPos, List<String> lines) {
        super(uuid, instance, name, displayName, playerSkin, spawnPos);
        this.lines = new Hologram[lines.size()];

        for (int i = 0; i < lines.size(); i++) {
            var component = lines.get(i);
            var hologram = new Hologram(instance, new Pos(spawnPos).add(0, 2 + (0.26 * i), 0), LegacyComponentSerializer.legacyAmpersand().deserialize(component));
            this.lines[i] = hologram;
        }
    }

    public void spawnLines() {
        if (lines.length == 0) return;

        for (int i = 0; i < lines.length; i++) {
            lines[i].getEntity().spawn();
        }
    }

    public void add(@NotNull Player player) {
        for (int i = 0; i < lines.length; i++) {
            lines[i].getEntity().addViewer(player);
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
