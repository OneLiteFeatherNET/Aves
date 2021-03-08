package de.icevizion.aves.inventory;

import de.icevizion.aves.item.ItemBuilder;
import net.titan.cloudcore.i18n.Translator;
import net.titan.spigot.player.CloudPlayer;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

@Deprecated
public class InventoryItem {

	private final Translator translator;
	private final Locale locale;
	private final ItemBuilder itemBuilder;

	public InventoryItem(Translator translator, Locale locale, ItemBuilder itemBuilder) {
		this.translator = translator;
		this.locale = locale;
		this.itemBuilder = itemBuilder;
	}

	public InventoryItem(Translator translator, CloudPlayer cloudPlayer, ItemBuilder itemBuilder) {
		this(translator, cloudPlayer.getLocale(), itemBuilder);
	}

	public InventoryItem setEmptyDisplayName() {
		itemBuilder.setDisplayName("§0");
		return this;
	}

	public InventoryItem setDisplayName(String key, Object... arguments) {
		itemBuilder.setDisplayName(translator.getString(locale, key, arguments));
		return this;
	}

	public InventoryItem setLore(String key, Object... arguments) {
		String[] lore = translator.getString(locale, key, arguments).split("\n");
		itemBuilder.setLore(Arrays.asList(lore));
		return this;
	}

	public ItemBuilder getItemBuilder() {
		return itemBuilder;
	}
}
