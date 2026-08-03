package net.theevilreaper.aves.inventory.pageable;

import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * This class is the implementation for the {@link PageableInventory.Builder} interface.
 *
 * @author theEvilReaper
 * @version 1.1.2
 * @since 1.2.0
 */
@ApiStatus.Experimental
public non-sealed class PageableInventoryBuilder implements PageableInventory.Builder {

    private final InventoryType type;
    private @Nullable Player player;
    private @Nullable PageableControls pageableControls;
    private @Nullable InventoryLayout layout;
    private TitleData titleData;
    private List<ISlot> slots;
    private int @Nullable [] slotRange;

    /**
     * Creates a new instance of the builder
     *
     * @param type of the inventory
     */
    protected PageableInventoryBuilder(InventoryType type) {
        this.type = type;
        this.titleData = TitleData.DEFAULT;
        this.slots = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableInventory.Builder player(Player player) {
        this.player = player;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableInventory.Builder layout(InventoryLayout layout) {
        this.layout = layout;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableInventory.Builder controls(PageableControls pageableControls) {
        this.pageableControls = pageableControls;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableInventory.Builder slotRange(int... itemSlots) {
        Check.argCondition(itemSlots.length == 0, "The slotRange can't be zero");
        this.slotRange = itemSlots;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableInventory.Builder values(List<ISlot> slots) {
        this.slots = slots;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableInventory.Builder titleData(TitleData titleData) {
        this.titleData = titleData;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableInventory.Builder titleData(Consumer<TitleData.Builder> titleBuilder) {
        TitleData.Builder builder = TitleData.builder();
        titleBuilder.accept(builder);
        this.titleData = builder.build();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageableInventory build() {
        Check.argCondition(this.layout == null, "The layout can't be null");
        Check.argCondition(this.player == null, "The player argument can't be null");
        if (this.pageableControls == null) {
            this.pageableControls = DefaultPageableControls.fromSize(this.type);
        }
        return new PlayerPageableInventoryImpl(
                player,
                type,
                pageableControls,
                layout,
                slots,
                titleData,
                slotRange
        );
    }
}
