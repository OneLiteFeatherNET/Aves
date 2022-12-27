package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.item.IItem;
import de.icevizion.aves.item.Item;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * This class represents the default implementation for the {@link PageableControls} interfaces.
 * Only the next and back slot are customizable in this implementation.
 * When you want to set the {@link ItemStack} as well please create your own implementation from the {@link PageableControls} interface.
 * Please note that this implementation doesn't support a translation context!
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 **/
public final class DefaultPageableControls implements PageableControls {

    private final IItem nextPage;
    private final IItem previousPage;
    private final int backSlot;
    private final int nextSlot;

    /**
     * Creates a new instance from the class with the given values from the parameter list.
     * @param backSlot the slot where the back button should be set
     * @param nextSlot the slot where the next button should be set
     * @param inventorySize the size from the inventory
     */
    public DefaultPageableControls(int backSlot, int nextSlot, int inventorySize) {
        Check.argCondition(backSlot < 0 || backSlot > inventorySize, "The backSlot index is not in the inventory range");
        Check.argCondition(nextSlot < 0 || nextSlot > inventorySize, "The nextSlot index is not in the inventory range");
        this.backSlot = backSlot;
        this.nextSlot = nextSlot;
        this.nextPage = new Item(ItemStack.builder(Material.ARROW).displayName(Component.text("Next page", NamedTextColor.GRAY)).build());
        this.previousPage = new Item(ItemStack.builder(Material.ARROW).displayName(Component.text("Previous page", NamedTextColor.GRAY)).build());
    }

    /**
     * Returns the slot in which the {@link ItemStack} for the back button should be set.
     * @return the given slot index
     */
    @Override
    public int getBackSlot() {
        return backSlot;
    }

    /**
     * Returns the slot in which the {@link ItemStack} for the next page button should be set.
     * @return the given slot index
     */
    @Override
    public int getNextSlot() {
        return nextSlot;
    }

    /**
     * Returns the {@link ItemStack} which represents the back button.
     * @param locale the {@link Locale} instance to get the localed variante
     * @return the given stack
     */
    @Override
    public @NotNull IItem getBackButton(@Nullable Locale locale) {
        return previousPage;
    }

    /**
     *
     * @param locale the {@link Locale} instance to get the localed variante
     * @return the given stack
     */
    @Override
    public @NotNull IItem getNextButton(@Nullable Locale locale) {
        return nextPage;
    }
}
