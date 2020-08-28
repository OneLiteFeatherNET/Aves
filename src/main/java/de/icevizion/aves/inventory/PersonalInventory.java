package de.icevizion.aves.inventory;

import net.titan.cloudcore.i18n.Translator;
import net.titan.spigot.player.CloudPlayer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public abstract class PersonalInventory extends TranslatedInventory {

	private final CloudPlayer cloudPlayer;

	public PersonalInventory(Translator translator, CloudPlayer cloudPlayer, InventoryRows rows,
	                         String titleKey, Object... arguments) {
		super(translator, cloudPlayer.getLocale(), rows, titleKey, arguments);
		this.cloudPlayer = cloudPlayer;
	}

	public abstract void draw();

	public final CloudPlayer getCloudPlayer() {
		return cloudPlayer;
	}
}
