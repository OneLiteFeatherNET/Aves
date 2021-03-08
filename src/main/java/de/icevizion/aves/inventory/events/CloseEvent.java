package de.icevizion.aves.inventory.events;

import net.titan.spigot.Cloud;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.0.0
 * @since 1.0.6
 */

public class CloseEvent {

	private final InventoryCloseEvent event;
	private boolean cancelled;

	public CloseEvent(InventoryCloseEvent event) {
		this.event = event;
	}

	/**
	 * Returns the player involved in this event.
	 * @return the player who is involved in this event
	 */

	public Player getPlayer() {
		return (Player) event.getPlayer();
	}

	/**
	 * Returns the {@link CloudPlayer} involved in this event.
	 * @return the player who is involved in this event
	 */
	@Deprecated(forRemoval = true)
	public CloudPlayer getCloudPlayer() {
		return Cloud.getInstance().getPlayer(getPlayer());
	}

	/**
	 * Gets the cancellation state of this event.
	 * A cancelled event will not be executed in the server, but will still pass to other plugins
	 * @return true if this event is cancelled
	 */

	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * Sets the cancellation state of this event.
	 * A cancelled event will not be executed in the server, but will still pass to other plugins.
	 * @param cancelled true if you wish to cancel this event
	 */

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
