package de.icevizion.aves.inventory.function;

import at.rxcki.strigiformes.MessageProvider;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.slot.TranslatedSlot;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import static de.icevizion.aves.inventory.util.InventoryConstants.EMPTY_SLOT;

/**
 * The class contains a default implementation for the {@link ApplyLayoutFunction}.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class DefaultApplyLayoutFunction implements ApplyLayoutFunction {

    private final ISlot[] contents;

    public DefaultApplyLayoutFunction(@NotNull ISlot[] content) {
        this.contents = content;
    }

    @Override
    public void applyLayout(ItemStack[] itemStacks, Locale locale, MessageProvider messageProvider) {
        if (itemStacks == null || itemStacks.length == 0) return;
        for (int i = 0; i < itemStacks.length; i++) {
            ISlot slot = contents[i];

            if (slot == null) continue;

            if (slot instanceof TranslatedSlot && locale == null) {
                throw new IllegalArgumentException("Tried to apply the InventoryLayout with an Translated slot and provided no locale!");
            }
            if (slot == EMPTY_SLOT) {
                itemStacks[i] = null;
            } else {
                if (slot instanceof TranslatedSlot translatedSlot) {
                    if (translatedSlot.getTranslatedItem().getMessageProvider() == null) {
                        translatedSlot.getTranslatedItem().setMessageProvider(messageProvider);
                    }

                    itemStacks[i] = translatedSlot.getItem(locale);
                } else {
                    itemStacks[i] = slot.getItem();
                }
            }
        }
    }
}
