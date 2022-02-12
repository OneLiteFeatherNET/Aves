package de.icevizion.aves.npc;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public final class NPCBuilder {

    private Instance instance;
    private String name;
    private Pos pos;
    private PlayerSkin playerSkin;
    private NPCInteraction interaction;
    private List<String> lines = new ArrayList<>();

    public NPCBuilder withInstance(@NotNull Instance instance) {
        this.instance = instance;
        return this;
    }

    public NPCBuilder withName(@NotNull String name) {
        if (name.trim().isEmpty()) {
            throw new RuntimeException("The name can not be empty");
        }

        this.name = name;
        return this;
    }

    public NPCBuilder withPos(@NotNull Pos pos) {
        this.pos = pos;
        return this;
    }

    public NPCBuilder withSkin(@NotNull PlayerSkin playerSkin) {
        this.playerSkin = playerSkin;
        return this;
    }

    public NPCBuilder withInteraction(NPCInteraction interaction) {
        this.interaction = interaction;
        return this;
    }

    public NPCBuilder withLine(@NotNull String line) {
        this.lines.add(line);
        return this;
    }

    public NPCBuilder withLines(@NotNull List<String> lines) {
        this.lines = lines;
        return this;
    }

    public NPC build() {
        if (lines.isEmpty()) {
            return new MinestomNPC(UUID.randomUUID(), instance, name, Component.text(name, NamedTextColor.GREEN), playerSkin, pos).setInteraction(interaction);
        }

        return new LinedNPC(UUID.randomUUID(), instance, name, Component.text(name, NamedTextColor.GREEN), playerSkin, pos, lines).setInteraction(interaction);
    }
}
