package de.icevizion.aves.npc;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public non-sealed class MinestomNPC implements NPC {

    private final UUID uuid;
    private final Instance instance;
    private final Component displayName;
    private final PlayerSkin playerSkin;
    private final Pos spawnPos;
    private NPCInteraction interaction;

    public MinestomNPC(@NotNull UUID uuid, @NotNull Instance instance, @NotNull Component displayName, @NotNull PlayerSkin playerSkin, @NotNull Pos spawnPos) {
        this.uuid = uuid;
        this.instance = instance;
        this.displayName = displayName;
        this.playerSkin = playerSkin;
        this.spawnPos = spawnPos;
    }

    @Override
    public NPC setInteraction(NPCInteraction npcInteraction) {
        this.interaction = npcInteraction;
        return this;
    }

    @Override
    public @NotNull Instance getInstance() {
        return instance;
    }

    @NotNull
    public UUID getUUID() {
        return uuid;
    }

    @NotNull
    public Component getDisplayName() {
        return displayName;
    }

    @NotNull
    public PlayerSkin getPlayerSkin() {
        return playerSkin;
    }

    @NotNull
    public Pos getSpawnPos() {
        return spawnPos;
    }

    @Override
    @Nullable
    public NPCInteraction getInteraction() {
        return interaction;
    }
}
