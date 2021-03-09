package de.icevizion.aves.inventory.events;

import net.titan.spigot.Cloud;
import net.titan.spigot.player.CloudPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.0.0
 * @since 1.0.6
 */

public class ClickEvent {

	private final InventoryClickEvent event;

	public ClickEvent(InventoryClickEvent event) {
		this.event = event;
	}

	/**
	 * Returns the player involved in this event.
	 * @return the player who is involved in this event
	 */

	public Player getPlayer() {
		return (Player) event.getWhoClicked();
	}

	/**
	 * Returns the {@link CloudPlayer} involved in this event.
	 * @return the player who is involved in this event
	 */

	@Deprecated
	public CloudPlayer getCloudPlayer() {
		return Cloud.getInstance().getPlayer(getPlayer());
	}

	/**
	 * Returns the {@link InventoryAction} involved in this event.
	 * @return the action
	 */

	public InventoryAction getAction() {
		return event.getAction();
	}

	/**
	 * Returns the {@link ClickType} involved in this event.
	 * @return the clicked
	 */

	public ClickType getClickType() {
		return event.getClick();
	}

	/**
	 * Returns the clicked slot as int.
	 * @return the slot id
	 */

	public int getClickedSlot() {
		return event.getRawSlot();
	}
}

