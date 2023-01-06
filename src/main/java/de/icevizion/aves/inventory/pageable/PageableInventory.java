package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.slot.ISlot;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PageableInventory {

    /**
     * Creates a new instance from the {@link PageableInventory.Builder}.
     * @return the created instance
     */
    @Contract(pure = true)
    static PageableInventory.Builder builder() {
        return new PageableInventoryBuilder();
    }

    void add(@NotNull ISlot slot);

    void add(@NotNull List<ISlot> slots);

    void remove(@NotNull ISlot slot);

    void remove(@NotNull List<ISlot> slots);

    void open(@NotNull Player player);

    int getMaxPages();

    /**
     * The builder interface contains all method for the builder to build a new instance from an {@link PageableInventory}.
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.2.0
     */
    sealed interface Builder permits PageableInventoryBuilder {

        /**
         * Set's the title for the {@link net.minestom.server.inventory.Inventory} as {@link Component}.
         * @param component the component to set
         * @return the builder instance
         */
        @NotNull Builder title(@NotNull Component component);

        @NotNull Builder decoration(@NotNull InventoryLayout layout);

        /**
         * Set the {@link InventoryType} for the paginated inventory.
         * The {@link InventoryType} must be a chest type otherwise an exception will be thrown
         * @param type the {@link InventoryType} to set
         * @return the builder instance
         */
        @NotNull Builder type(@NotNull InventoryType type);

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
