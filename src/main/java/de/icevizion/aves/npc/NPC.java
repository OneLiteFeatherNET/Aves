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
public sealed interface NPC permits MinestomNPC {

    /**
     * Set's the interaction for the npc.
     * @param npcInteraction The npc to set
     * @return the NPC object
     */
    NPC setInteraction(NPCInteraction npcInteraction);

    /**
     * Returns the {@link Instance} from the npc.
     * @return the given instance
     */
    @NotNull
    Instance getInstance();

    /**
     * Returns the name of npc.
     * @return the given name
     */
    @NotNull
    String getName();

    /**
     * Returns the uuid from the {@link NPC}.
     * @return the given uuid
     */
    @NotNull
    UUID getUUID();

    /**
     * Returns the {@link Component} which include the display name from the npc.
     * @return the given component
     */
    @NotNull
    Component getDisplayName();

    /**
     * Returns the {@link PlayerSkin} which contains the data for the skin.
     * @return the given skin data
     */
    @NotNull
    PlayerSkin getSkin();

    /**
     * Returns the spawn position from the npc.
     * @return the given position as {@link Pos}
     */
    @NotNull
    Pos getSpawnPos();

    /**
     * Returns the given interaction from the npc.
     * The interaction can ben null
     * @return the given interaction or null
     */
    @Nullable
    NPCInteraction getInteraction();
}
