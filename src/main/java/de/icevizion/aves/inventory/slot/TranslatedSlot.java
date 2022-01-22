package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.item.TranslatedItem;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
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

    public TranslatedSlot(@NotNull TranslatedItem translatedItem, Consumer<InventoryPreClickEvent> clickListener) {
        super(clickListener);
        this.translatedItem = translatedItem;
    }

    @Override
    public ItemStack getItem() {
        throw new UnsupportedOperationException("This TranslatedSlot needs a locale to retrieve the item");
    }

    public ItemStack getItem(Locale locale) {
        return translatedItem.get(locale);
    }

    public TranslatedSlot setTranslatedItem(@NotNull TranslatedItem translatedItem) {
        this.translatedItem = translatedItem;
        return this;
    }

    public TranslatedItem getTranslatedItem() {
        return translatedItem;
    }

    @Override
    public String toString() {
        return "TranslatedSlot{" +
                "translatedItem=" + translatedItem +
                "} " + super.toString();
    }
}
