package de.icevizion.aves.inventory.events;

import net.titan.spigot.CloudService;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class CloseEvent {

	private final CloudService cloudService;
	private final InventoryCloseEvent event;
	private boolean cancelled;

	public CloseEvent(CloudService cloudService, InventoryCloseEvent event) {
		this.cloudService = cloudService;
		this.event = event;
	}

	public Player getPlayer() {
		return (Player) event.getPlayer();
	}

	public CloudPlayer getCloudPlayer() {
		return cloudService.getPlayer(getPlayer());
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
