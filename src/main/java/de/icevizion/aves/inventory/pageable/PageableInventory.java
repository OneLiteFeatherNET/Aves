package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.slot.ISlot;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 *
 * @version 1.0.0
 * @since 1.2.0
 */
public non-sealed interface PageableInventory extends OpenableInventory {

    /**
     * Creates a new instance from the {@link PageableInventory.Builder}.
     * @return the created instance
     */
    @Contract(pure = true)
    static PageableInventory.Builder builder() {
        return new PageableInventoryBuilder();
    }

    /**
     * Add a new entry which should be displayed in the inventory.
     * @param slot the slot to add
     */
    void add(@NotNull ISlot slot);

    /**
     * Add a list of entries which should be displayed in the inventory.
     * @param slots the list that has all entries to add
     */
    void add(@NotNull List<ISlot> slots);

    /**
     * Remove a single entry from the list.
     * @param slot the slot to remove
     */
    void remove(@NotNull ISlot slot);

    /**
     * Removes a list of {@link ISlot} from the underlying list.
     * @param slots the list which contains the slots to remove
     */
    void remove(@NotNull List<ISlot> slots);

    /**
     * Unregister some listener and other stuff from the server process.
     */
    void unregister();

    /**
     * Returns the maximum amount of pages which the inventory can have.
     * @return the given value
     */
    int getMaxPages();

    /**
     * The builder interface contains all method for the builder to build a new instance from an {@link PageableInventory}.
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.2.0
     */
    sealed interface Builder permits PageableInventoryBuilder {

        @NotNull Builder player(@NotNull Player player);

        /**
         * Set's the title for the {@link net.minestom.server.inventory.Inventory} as {@link Component}.
         * @param component the component to
         * @param pagesInTitle if the current and max pages should be displayed in the title
         * @return the builder instance
         */
        @NotNull Builder title(@NotNull Component component, boolean pagesInTitle);

        /**
         * Set's the title for the {@link net.minestom.server.inventory.Inventory} as {@link Component}.
         * @param component the component to
         * @return the builder instance
         */
        default @NotNull Builder title(@NotNull Component component) {
            return this.title(component, false);
        }

        /**
         * Set the layout which contains the decoration layout.
         * Don't add any entries to this layout which shoes data from a specific use case.
         * For that use the {@link PageableInventory#add(ISlot)} etc. methods to add or remove data values
         * @param layout the layout which contains the decoration
         * @return the builder instance
         */
        @NotNull Builder layout(@NotNull InventoryLayout layout);

        /**
         * Set the {@link InventoryType} for the paginated inventory.
         * The {@link InventoryType} must be a chest type otherwise an exception will be thrown
         * @param type the {@link InventoryType} to set
         * @return the builder instance
         */
        @NotNull Builder type(@NotNull InventoryType type);

        /**
         * Set a new reference from the {@link PageableControls} interface which defines which items are used to switch between pages.
         * @param pageableControls the instance to set
         * @return the builder instance
         */
        @NotNull Builder controls(@NotNull PageableControls pageableControls);

        /**
         * Set the slot range where the items should be placed in the layout
         * @param itemSlots the array which contains all valid slots
         * @return the builder instance
         */
        @NotNull Builder slotRange(int @NotNull ... itemSlots);

        /**
         * Set the list which contains the items to the builder.
         * @param slots the list which contains all slots
         * @return the builder instance
         */
        @NotNull Builder values(@NotNull List<ISlot> slots);

        /**
         * Returns a new instance from an {@link PageableInventory}.
         * @return the created instance from the {@link PageableInventory}
         */
        @NotNull PageableInventory build();
    }
}
