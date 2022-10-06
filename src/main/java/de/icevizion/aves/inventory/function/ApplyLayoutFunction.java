package de.icevizion.aves.inventory.function;

import at.rxcki.strigiformes.MessageProvider;
import net.minestom.server.item.ItemStack;

import java.util.Locale;

/**
 * This interface defines all method to write an own implementation for the applyLayout function.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
@FunctionalInterface
public interface ApplyLayoutFunction {

    /**
     *
     * @param itemStacks
     * @param locale
     * @param messageProvider
     */
    void applyLayout(ItemStack[] itemStacks, Locale locale, MessageProvider messageProvider);

    /**
     * Apply the layout to an inventory.
     * @param itemStacks the array with contains the items
     */
    default void applyLayout(ItemStack[] itemStacks) {
        this.applyLayout(itemStacks, null, null);
    }
}
