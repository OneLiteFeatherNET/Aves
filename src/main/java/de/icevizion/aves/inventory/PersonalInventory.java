package de.icevizion.aves.inventory;

import net.titan.cloudcore.i18n.Translator;
import net.titan.spigot.player.CloudPlayer;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.0.0
 * @since 1.0.6
 */

public abstract class PersonalInventory extends TranslatedInventory {

	private final CloudPlayer cloudPlayer;

	/**
	 * Creates a new instance from the {@link PersonalInventory}.
	 * @param translator The instance to a {@link Translator}
	 * @param cloudPlayer The {@link CloudPlayer} who owns the inventory
	 * @param rows The amount of rows
	 * @param titleKey The key for the inventory name
	 * @param arguments The arguments if the name contains some variables or so
	 */

	public PersonalInventory(Translator translator, CloudPlayer cloudPlayer, InventoryRows rows,
	                         String titleKey, Object... arguments) {
		super(translator, cloudPlayer.getLocale(), rows, titleKey, arguments);
		this.cloudPlayer = cloudPlayer;
	}

	public abstract void draw();

	/**
	 * Returns the {@link CloudPlayer} who owns the inventory.
	 * @return The underlying player
	 */

	public final CloudPlayer getCloudPlayer() {
		return cloudPlayer;
	}
}
