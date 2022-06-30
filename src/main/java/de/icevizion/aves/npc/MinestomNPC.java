package de.icevizion.aves.npc;

import net.kyori.adventure.text.Component;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityType;
import net.minestom.server.entity.GameMode;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.PlayerSkin;
import net.minestom.server.instance.Instance;
import net.minestom.server.network.packet.server.play.PlayerInfoPacket;
import net.minestom.server.network.packet.server.play.SpawnEntityPacket;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.UUID;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public non-sealed class MinestomNPC extends Entity implements NPC {

    private final Instance instance;
    private final String name;
    private final Component displayName;
    private final PlayerSkin skin;
    private final Pos spawnPos;
    private NPCInteraction interaction;

    public MinestomNPC(@NotNull UUID uuid, @NotNull Instance instance, @NotNull String name, @NotNull Component displayName, @NotNull PlayerSkin skin, @NotNull Pos spawnPos) {
        super(EntityType.PLAYER);
        this.uuid = uuid;
        this.name = name;
        this.instance = instance;
        this.displayName = displayName;
        this.skin = skin;
        this.spawnPos = spawnPos;
        setCustomNameVisible(false);

        setInstance(instance, spawnPos);

        spawn();
    }

    public void sendPacket(@NotNull Player player) {
        var spawnPacket = new SpawnEntityPacket(getEntityId(), uuid, EntityType.PLAYER.id(), position, 1,(short)0, (short)0,(short)0);
        player.sendPacket(spawnPacket);
        var infoPacket = getPacket();
        player.sendPacket(infoPacket);
        addViewer(player);
        player.sendPacket(this.getMetadataPacket());
    }

    private PlayerInfoPacket getPacket() {
        var textureProperty = new PlayerInfoPacket.AddPlayer.Property("textures", skin.textures(), skin.signature());
        var playerEntry = new PlayerInfoPacket.AddPlayer(uuid, name, Collections.singletonList(textureProperty), GameMode.CREATIVE, 0, null);
        return new PlayerInfoPacket(PlayerInfoPacket.Action.ADD_PLAYER, Collections.singletonList(playerEntry));
    }

    @Override
    public void tick(long time) {}

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public NPC setInteraction(NPCInteraction npcInteraction) {
        this.interaction = npcInteraction;
        return this;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public @NotNull Instance getInstance() {
        return instance;
    }

    @NotNull
    public UUID getUUID() {
        return this.getUuid();
    }

    @NotNull
    public Component getDisplayName() {
        return displayName;
    }

    @NotNull
    public PlayerSkin getSkin() {
        return skin;
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
