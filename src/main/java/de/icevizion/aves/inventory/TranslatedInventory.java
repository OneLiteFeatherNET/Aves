package de.icevizion.aves.inventory;

import de.icevizion.aves.inventory.events.ClickEvent;
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

	public final void setItem(int slot, InventoryItem item, Consumer<ClickEvent> clickEvent) {
		super.setItem(slot, item.getItemBuilder(), clickEvent);
	}

	public final void setItem(int slot, InventoryItem item) {
		super.setItem(slot, item.getItemBuilder());
	}

	public final void setBackgroundItem(int slot, InventoryItem item) {
		super.setBackgroundItem(slot, item.getItemBuilder());
	}

	public final void setBackgroundItems(int fromIndex, int toIndex, InventoryItem item) {
		for (int i = fromIndex; i <= toIndex; i++) {
			setBackgroundItem(i, item);
		}
	}
}
