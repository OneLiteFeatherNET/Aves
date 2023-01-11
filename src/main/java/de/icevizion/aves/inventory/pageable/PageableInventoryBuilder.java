package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.util.LayoutCalculator;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.utils.validate.Check;
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

    private Player player;
    private Component title;
    private InventoryType type;
    private PageableControls pageableControls;
    private InventoryLayout layout;
    private int[] slotRange;
    private List<ISlot> slots;
    private boolean pagesInTitle;

    @Override
    public PageableInventory.@NotNull Builder player(@NotNull Player player) {
        this.player = player;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder title(@NotNull Component component, boolean pagesInTitle) {
        this.title = component;
        this.pagesInTitle = pagesInTitle;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder layout(@NotNull InventoryLayout layout) {
        this.layout = layout;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder type(@NotNull InventoryType type) {
        Check.argCondition(!LayoutCalculator.isChestInventory(type), "The type must be a chest inventory");
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
        Check.argCondition(itemSlots.length == 0, "The slotRange can't be zero");
        this.slotRange = itemSlots;
        return this;
    }

    @Override
    public PageableInventory.@NotNull Builder values(@NotNull List<ISlot> slots) {
        this.slots = slots;
        return this;
    }

    @Override
    public @NotNull PageableInventory build() {
        Check.argCondition(this.layout == null, "The layout can't be null");
        if (this.pageableControls == null) {
            this.pageableControls = DefaultPageableControls.fromSize(this.type);
        }

        if (player == null) {
            return null;
        }

        return new PlayerPageableInventoryImpl(
                player,
                title,
                type,
                pageableControls,
                layout,
                slots,
                pagesInTitle,
                slotRange
        );
    }
}
