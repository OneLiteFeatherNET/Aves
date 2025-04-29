package net.theevilreaper.aves.inventory.slot;

import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.theevilreaper.aves.item.TranslatedItem;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * The implentation of a {@link Slot} can be used for a translation context.
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
     * Creates a new instance from the {@link TranslatedSlot} with the given values.
     * @param translatedItem the {@link TranslatedItem} to for the slot
     * @param clickListener the {@link InventoryClick} for the slot
     */
    public TranslatedSlot(@NotNull TranslatedItem translatedItem, InventoryClick clickListener) {
        super(clickListener);
        this.translatedItem = translatedItem;
    }

    /**
     * Constructor which creates a copy of a {@link TranslatedSlot}.
     * @param translatedSlot the item to copy
     */
    private TranslatedSlot(@NotNull TranslatedSlot translatedSlot) {
        super(translatedSlot.getClick());
        this.translatedItem = translatedSlot.getTranslatedItem();
    }

    /**
     * Creates a copy of a given {@link TranslatedSlot}.
     * @param translatedSlot the slot to create the copy from
     * @return a created copy from the slots
     */
    @Contract("_ -> new")
    public static @NotNull TranslatedSlot of(@NotNull TranslatedSlot translatedSlot) {
        return new TranslatedSlot(translatedSlot);
    }

    /**
     * Throws an exception because the method is not allowed to be used in a translated context.
     */
    @Override
    public ItemStack getItem() {
        throw new UnsupportedOperationException("This TranslatedSlot needs a locale to retrieve the item");
    }

    /**
     * Throws in this implementation an error because it's not possible to update an {@link ItemStack} in a translated context
     * @param itemStack can be ignored
     * @return throws an {@link UnsupportedOperationException}
     */
    @Override
    public @NotNull ISlot setItemStack(ItemStack itemStack) {
        throw new UnsupportedOperationException("This TranslatedSlot needs a translated item");
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
