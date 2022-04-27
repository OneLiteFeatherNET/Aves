package de.icevizion.aves.inventory.slot;

import de.icevizion.aves.item.TranslatedItem;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * Represents a {@link Slot} implementation for a translated context.
 * @author Patrick Zdarsky / Rxcki
 * @since 1.0.12
 * @version 1.0.0
 */
public class TranslatedSlot extends Slot {

    protected TranslatedItem translatedItem;

    /**
     * Creates a new instance from the slot.
     * @param translatedItem The item for the slot
     */
    public TranslatedSlot(@NotNull TranslatedItem translatedItem) {
        this.translatedItem = translatedItem;
    }

    /**
     * Creates a new instance from the slot.
     * @param translatedItem The item for the slot
     * @param clickListener The click listener for the slot
     */
    public TranslatedSlot(@NotNull TranslatedItem translatedItem, Consumer<InventoryPreClickEvent> clickListener) {
        super(clickListener);
        this.translatedItem = translatedItem;
    }

    /**
     * Throws an exception because the method is not allowed to be used in a translated context.
     */
    @Override
    public ItemStack getItem() {
        throw new UnsupportedOperationException("This TranslatedSlot needs a locale to retrieve the item");
    }

    /**
     * Returns the {@link ItemStack} which is bounds on the slot
     * @param locale The locale to get the right {@link ItemStack}
     * @return the determined item which is based on the locale
     */
    public ItemStack getItem(@NotNull Locale locale) {
        return translatedItem.get(locale);
    }

    /**
     * Updates the given item reference to a new reference.
     * @param translatedItem the item to set
     */
    public TranslatedSlot setTranslatedItem(@NotNull TranslatedItem translatedItem) {
        this.translatedItem = translatedItem;
        return this;
    }

    /**
     * Returns the {@link TranslatedItem} reference from the slot.
     * @return the given reference to the {@link TranslatedItem}
     */
    @NotNull
    public TranslatedItem getTranslatedItem() {
        return translatedItem;
    }

    /**
     * Returns a representation of the class as string.
     * @return the object as string
     */
    @Override
    public String toString() {
        return "TranslatedSlot{" +
                "translatedItem=" + translatedItem +
                "} " + super.toString();
    }
}
