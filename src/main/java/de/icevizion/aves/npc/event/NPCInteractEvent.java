package de.icevizion.aves.npc.event;

import de.icevizion.aves.npc.NPC;
import net.minestom.server.entity.Player;
import net.minestom.server.event.trait.CancellableEvent;
import net.minestom.server.event.trait.PlayerEvent;
import org.jetbrains.annotations.NotNull;

/**
 * The event will be called when a player interacts with a {@link NPC}.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public class NPCInteractEvent implements PlayerEvent, CancellableEvent {

    private final Player player;
    private final NPC npc;

    private boolean cancelled;

    /**
     * Creates a new instance from the interact event.
     * @param player The {@link Player} which is involved in the event
     * @param npc The {@link NPC} which is involved in the event
     */
    public NPCInteractEvent(@NotNull Player player, @NotNull NPC npc) {
        this.player = player;
        this.npc = npc;
    }

    /**
     * Gets the cancellation state of this event.
     * A cancelled event will not be executed in the server, but will still pass to other extension.
     *
     * @return true if this event is cancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancellation state of this event.
     *
     * @param cancel - true if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    /**
     * Returns the player which is involved in this event.
     * @return the involved player
     */
    @Override
    public @NotNull Player getPlayer() {
        return player;
    }

    /**
     * Returns the {@link NPC} which is involved in this event.
     * @return the involved npc
     */
    @NotNull
    public NPC getNpc() {
        return npc;
    }
}
