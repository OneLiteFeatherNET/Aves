package de.icevizion.aves.inventory.events;

import net.titan.spigot.CloudService;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public class ClickEvent {

	private final CloudService cloudService;
	private final InventoryClickEvent event;

	public ClickEvent(CloudService cloudService, InventoryClickEvent event) {
		this.cloudService = cloudService;
		this.event = event;
	}

	public Player getPlayer() {
		return (Player) event.getWhoClicked();
	}

	public CloudPlayer getCloudPlayer() {
		return cloudService.getPlayer(getPlayer());
	}

	public InventoryAction getAction() {
		return event.getAction();
	}

	public ClickType getClickType() {
		return event.getClick();
	}

	public int getClickedSlot() {
		return event.getRawSlot();
	}
}

