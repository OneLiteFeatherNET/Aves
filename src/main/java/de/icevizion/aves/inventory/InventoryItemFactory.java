package de.icevizion.aves.inventory;

import de.icevizion.aves.item.ItemBuilder;
import net.titan.cloudcore.i18n.Translator;
import net.titan.spigot.player.CloudPlayer;

import java.util.Locale;
import java.util.Objects;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public abstract class InventoryItemFactory {

	private final Translator translator;
	private final Locale locale;
	private CloudPlayer cloudPlayer;
	private InventoryItem backgroundItem;

	public InventoryItemFactory(Translator translator, Locale locale) {
		this.translator = translator;
		this.locale = locale;
	}

	public InventoryItemFactory(Translator translator, CloudPlayer cloudPlayer) {
		this(translator, cloudPlayer.getLocale());
		this.cloudPlayer = cloudPlayer;
	}

	public Translator getTranslator() {
		return translator;
	}

	public final Locale getLocale() {
		return locale;
	}

	public final CloudPlayer getCloudPlayer() {
		Objects.requireNonNull(cloudPlayer, "CloudPlayer is null");
		return cloudPlayer;
	}

	public final InventoryItem getBackgroundItem() {
		return backgroundItem;
	}

	public final InventoryItem createItem(ItemBuilder itemBuilder) {
		return new InventoryItem(translator, locale, itemBuilder);
	}

	protected final void setBackgroundItem(ItemBuilder itemBuilder) {
		backgroundItem = createItem(itemBuilder).setEmptyDisplayName();
	}
}
