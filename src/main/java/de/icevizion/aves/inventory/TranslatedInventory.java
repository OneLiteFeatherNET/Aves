package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.events.ClickEvent;
import de.icevizion.aves.item.ItemBuilder;
import net.titan.cloudcore.i18n.Translator;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public abstract class TranslatedInventory extends InventoryBuilder {

	private final Translator translator;
	private final Locale locale;

	public TranslatedInventory(Translator translator, Locale locale, InventoryRows rows,
	                           String titleKey, Object... arguments) {
		super(translator.getString(locale, titleKey, arguments), rows);
		this.translator = translator;
		this.locale = locale;
	}

	public abstract void draw();

	public final Translator getTranslator() {
		return translator;
	}

	public final Locale getLocale() {
		return locale;
	}

	public final InventoryItem createItem(ItemBuilder itemBuilder) {
		return new InventoryItem(translator, locale, itemBuilder);
	}

	public final void setItem(int slot, InventoryItem item, Consumer<ClickEvent> clickEvent) {
		super.setItem(slot, item.getItemBuilder(), clickEvent);
	}

	public final void setItem(int slot, InventoryItem item) {
		super.setItem(slot, item.getItemBuilder());
	}

	/**
	 * Sets a specified item to a given slot.
	 * @param slot The slot id
	 * @param item The item to set
	 */

	public final void setBackgroundItem(int slot, InventoryItem item) {
		super.setBackgroundItem(slot, item.getItemBuilder());
	}

	/**
	 * Sets a specified item for a specified range.
	 * @param fromIndex The start index
 	 * @param toIndex The end index
	 * @param item The item to set
	 */

	public final void setBackgroundItems(int fromIndex, int toIndex, InventoryItem item) {
		for (int i = fromIndex; i <= toIndex; i++) {
			setBackgroundItem(i, item);
		}
	}

	/**
	 * Change the name of the underlying inventory.
	 * @param key he translation key
	 */

	@Override
	public final void setInventoryTitle(String key) {
		setInventoryTitle(key, new Object[0]);
	}

	/**
	 * Change the name of the underlying inventory.
	 * @param key The translation key
	 * @param arguments The arguments for the keys
	 */

	public final void setInventoryTitle(String key, Object... arguments) {
		String title = translator.getString(locale, key, arguments);
		super.setInventoryTitle(title);
	}
}
