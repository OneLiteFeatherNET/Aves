package de.icevizion.aves.tinventory.translated;

import de.icevizion.aves.tinventory.Slot;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class TranslatedSlot extends Slot {

    protected TranslatedItem translatedItem;

    public TranslatedSlot(@NotNull TranslatedItem translatedItem) {
        this.translatedItem = translatedItem;
    }

    public TranslatedSlot(@NotNull TranslatedItem translatedItem, Consumer<InventoryClickEvent> clickListener) {
        super(clickListener);
        this.translatedItem = translatedItem;
    }

    @Override
    public ItemStack getItem() {
        throw new UnsupportedOperationException("This TranslatedSlot needs a locale to retrieve the Item");
    }

    public ItemStack getItem(Locale locale) {
        return translatedItem.get(locale);
    }

    public TranslatedSlot setTranslatedItem(@NotNull TranslatedItem translatedItem) {
        this.translatedItem = translatedItem;
        return this;
    }
}
