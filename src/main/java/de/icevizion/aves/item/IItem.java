package de.icevizion.aves.item;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Locale;

/**
 * Represents an item which can be translated or not.
 * The implementation for a non translated item is {@link Item} and for translated items
 * is the {@link TranslatedItem} the implementation
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.13
 **/
public sealed interface IItem permits Item, TranslatedItem {

    /**
     * Returns the underlying {@link ItemStack}.
     * This method should only be used if there is no translation
     * @return the underlying stack
     */
    @UnknownNullability(value = "In a translation context this method can return a null value")
    default ItemStack get() {
        return get(null);
    }

    /**
     * Returns a {@link ItemStack} that matches with a specific {@link Locale}.
     * @param locale The locale to get the right stack
     * @return the fetched stack
     */
    ItemStack get(Locale locale);
}
