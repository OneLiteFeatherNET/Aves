package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.item.IItem;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * The interface defines all method to implement an own instance for the control {@link ItemStack}s in a pageable inventory.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
@ApiStatus.Experimental
public interface PageableControls {

    /**
     * Returns the slot in which the {@link ItemStack} for the back button should be set.
     * @return the given slot index
     */
    int getBackSlot();

    /**
     * Returns the slot in which the {@link ItemStack} for the next page button should be set.
     * @return the given slot index
     */
    int getNextSlot();

    /**
     * Returns the {@link ItemStack} which represents the back button.
     * @param locale the {@link Locale} instance to get the localed variante
     * @return the given {@link ItemStack}
     */
    @NotNull IItem getBackButton(@Nullable Locale locale);

    /**
     * Returns the {@link ItemStack} which represents the back button.
     * @return the given {@link ItemStack}
     */
    @NotNull default IItem getBackButton() {
        return this.getBackButton(null);
    }

    /**
     * Returns the {@link ItemStack} which represents the next button.
     * @param locale the {@link Locale} instance to get the localed variante
     * @return the given {@link ItemStack}
     */
    @NotNull IItem getNextButton(@Nullable Locale locale);

    /**
     * Returns the {@link ItemStack} which represents the next button.
     * @return the given {@link ItemStack}
     */
    @NotNull default IItem getNextButton() {
        return this.getNextButton(null);
    }

    default Material getForwardMaterial() {
        return this.getNextButton().get().material();
    }

    default Material getBackMaterial() {
        return this.getBackButton().get().material();
    }
}
