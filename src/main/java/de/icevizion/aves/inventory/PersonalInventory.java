package de.icevizion.aves.inventory;

import org.bukkit.entity.Player;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.1.0
 * @since 1.0.6
 */

public abstract class PersonalInventory extends InventoryBuilder {

	/*** The variable for the player.*/

	private final Player player;

	/**
	 * Creates a new instance from the {@link PersonalInventory}.
	 * @param player The {@link Player} who owns the inventory
	 * @param rows The amount of rows
	 * @param title The title of the inventory
	 */

	public PersonalInventory(Player player, InventoryRows rows, String title) {
		super(title, rows);
		this.player = player;
	}

	/**
	 * Draws the content into the inventory.
	 */

	public abstract void draw();

	/**
	 * Returns the {@link Player} who owns the inventory.
	 * @return The underlying player
	 */

	public Player getPlayer() {
		return player;
	}
}
