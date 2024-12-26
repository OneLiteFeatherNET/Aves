package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.util.LayoutCalculator;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

/**
 * This class is the implementation for the {@link PageableInventory.Builder} interface.
 *
 * @author theEvilReaper
 * @version 1.1.0
 * @since 1.2.0
 */
@ApiStatus.Experimental
public non-sealed class PageableInventoryBuilder implements PageableInventory.Builder {

    private Player player;
    private InventoryType type;
    private PageableControls pageableControls;
    private InventoryLayout layout;
    private TitleData titleData;
    private List<ISlot> slots;
    private int[] slotRange;
    private boolean pagesInTitle;

    /**
     * Set the player reference which owns the inventory.
     *
     * @param player the player which owns the inventory
     * @return the builder instance
     */
    @Override
    public PageableInventory.@NotNull Builder player(@NotNull Player player) {
        this.player = player;
        return this;
    }

    /**
     * Set the layout reference which is used for the decoration in the layout.
     * Please don't add any items to it which displays dynamic data
     *
     * @param layout the layout which contains the decoration
     * @return the builder instance
     */
    @Override
    public PageableInventory.@NotNull Builder layout(@NotNull InventoryLayout layout) {
        this.layout = layout;
        return this;
    }

    /**
     * Set the {@link InventoryType} for the inventory.
     * This type must be a {@link InventoryType} which is related to a chest inventory.
     * Otherwise, the builder throws an exception
     *
     * @param type the {@link InventoryType} to set
     * @return the builder instance
     */
    @Override
    public PageableInventory.@NotNull Builder type(@NotNull InventoryType type) {
        Check.argCondition(!LayoutCalculator.isChestInventory(type), "The type must be a chest inventory");
        this.type = type;
        return this;
    }

    /**
     * Set an implementation reference from the {@link PageableControls}.
     *
     * @param pageableControls the instance to set
     * @return the builder instance
     */
    @Override
    public PageableInventory.@NotNull Builder controls(@NotNull PageableControls pageableControls) {
        this.pageableControls = pageableControls;
        return this;
    }

    /**
     * @param itemSlots the array which contains all valid slots
     * @return the builder instance
     */
    @Override
    public PageableInventory.@NotNull Builder slotRange(int @NotNull ... itemSlots) {
        Check.argCondition(itemSlots.length == 0, "The slotRange can't be zero");
        this.slotRange = itemSlots;
        return this;
    }

    /**
     * Set's a list reference which contains all items for the inventory.
     *
     * @param slots the list which contains all slots
     * @return the builder instance
     */
    @Override
    public PageableInventory.@NotNull Builder values(@NotNull List<ISlot> slots) {
        this.slots = slots;
        return this;
    }

    /**
     * Set the title data which contains the data for the title in the inventory.
     *
     * @param titleData the data to set
     * @return the builder instance
     */
    @Override
    public PageableInventory.@NotNull Builder titleData(@NotNull TitleData titleData) {
        this.titleData = titleData;
        return this;
    }

    /**
     * Set the title data which contains the data for the title in the inventory.
     *
     * @param titleBuilder the builder to set the title data
     * @return the builder instance
     */
    @Override
    public PageableInventory.@NotNull Builder titleData(@NotNull Consumer<TitleData.Builder> titleBuilder) {
        TitleData.Builder builder = TitleData.builder();
        titleBuilder.accept(builder);
        this.titleData = builder.build();
        return this;
    }

    /**
     * Returns a new instance from the {@link PlayerPageableInventoryImpl} with all parameters from the builder.
     *
     * @return the created instance from the inventory class
     */
    @Override
    public @NotNull PageableInventory build() {
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
