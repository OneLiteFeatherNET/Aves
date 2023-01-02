package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.util.LayoutCalculator;
import de.icevizion.aves.item.IItem;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.InventoryType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This class is the implementation for the {@link PageableInventory.Builder} interface.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
@ApiStatus.Experimental
public non-sealed class PageableInventoryBuilder implements PageableInventory.Builder {

    private Component title;
    private InventoryType type;
    private PageableControls pageableControls;
    private InventoryLayout layout;
    private int[] slotRange;
    private List<IItem> values;

    @Override
    public PageableInventory.@NotNull Builder title(@NotNull Component component) {
        this.title = component;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder decoration(@NotNull InventoryLayout layout) {
        this.layout = layout;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder type(@NotNull InventoryType type) {
        if (!LayoutCalculator.isChestInventory(type)) {
            throw new IllegalArgumentException("The type must be a chest inventory");
        }
        this.type = type;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder controls(@NotNull PageableControls pageableControls) {
        this.pageableControls = pageableControls;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder slotRange(int @NotNull ... itemSlots) {
        if (itemSlots.length == 0) {
            throw new IllegalArgumentException("The slotRange can't be zero");
        }
        this.slotRange = itemSlots;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder values(@NotNull List<IItem> values) {
        this.values = values;
        return this;
    }

    @Override
    public @NotNull PageableInventory build() {
        if (this.pageableControls == null) {
            int nextSlot = this.type.getSize() - 1;
            int previousSlot = this.type.getSize() - 2;
            this.pageableControls = new DefaultPageableControls(nextSlot, previousSlot, this.type.getSize());
        }
        return new PageableInventoryImpl(title, type, pageableControls, layout, values, slotRange);
    }
}
