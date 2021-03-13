package de.icevizion.aves.tinventory.translated;

import de.icevizion.aves.tinventory.Slot;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class TranslatedSlot extends Slot {

    protected TranslatedItem translatedItem;

    @Override
    public ItemStack getItem() {
        throw new UnsupportedOperationException("This TranslatedSlot needs a locale to retrieve the Item");
    }

    public ItemStack getItem(Locale locale) {
        return translatedItem.get(locale);
    }
}
