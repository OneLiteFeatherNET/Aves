package de.icevizion.aves.inventory.function;

import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.slot.TranslatedSlot;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static de.icevizion.aves.inventory.util.InventoryConstants.BLANK_SLOT;

/**
 * This class includes a default implementation from the {@link ApplyLayoutFunction}.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class DefaultApplyLayoutFunction implements ApplyLayoutFunction {

    private final ISlot[] contents;

    /**
     * Creates a new instance from the default function.
     * @param content the array which contains all slots
     */
    public DefaultApplyLayoutFunction(@NotNull ISlot[] content) {
        this.contents = content;
    }

    /**
     * Applies the given content which is located in the {@link ISlot} array to the given array from the method.
     * @param itemStacks the array which contains {@link ItemStack}'s
     * @param locale the locale to get the right item if the context is translated
     */
    @Override
    public void applyLayout(ItemStack[] itemStacks, Locale locale) {
        if (itemStacks == null || itemStacks.length == 0) return;
        for (int i = 0; i < itemStacks.length; i++) {
            ISlot slot = contents[i];

            if (slot == null) continue;

            if (slot instanceof TranslatedSlot && locale == null) {
                throw new IllegalArgumentException("Tried to apply the InventoryLayout with an Translated slot and provided no locale!");
            }
            if (slot == BLANK_SLOT) {
                itemStacks[i] = null;
            } else {
                setItemStack(slot, itemStacks, locale, i);
            }
        }
    }

    /**
     * Set the {@link ItemStack} from a {@link ISlot} into stack array.
     * The methods also check if the {@link ISlot} is a {@link TranslatedSlot}.
     * When the slot is {@link TranslatedSlot} the method use the locale variable to get the right {@link ItemStack}
     * @param slot the slot to get the item from it
     * @param itemStacks the array which contains {@link ItemStack}'s
     * @param locale the locale to get the right item if the context is translated
     * @param index the index position to set the item
     */
    private void setItemStack(@NotNull ISlot slot, @NotNull ItemStack[] itemStacks, Locale locale, int index) {
        if (slot instanceof TranslatedSlot translatedSlot) {
            itemStacks[index] = translatedSlot.getItem(locale);
        } else {
            itemStacks[index] = slot.getItem();
        }
    }
}
