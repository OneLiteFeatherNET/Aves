package de.icevizion.aves.npc;

import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * The class
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
@FunctionalInterface
public interface NPCInteraction {

    /**
     * Handles what happen when an interaction is make on a {@link NPC}.
     * @param player The player who interacts with the npc
     * @param npc The npc who is involved in the interaction
     */
    void interaction(@NotNull Player player, @NotNull NPC npc);
}