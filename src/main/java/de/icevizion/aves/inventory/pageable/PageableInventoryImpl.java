package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.GlobalInventoryBuilder;
import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.slot.ISlot;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static de.icevizion.aves.inventory.util.InventoryConstants.EMPTY_SLOT;

/**
 * The class is a concrete implementation for the {@link PageableInventory} interface.
 *
 * @author Joltra
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.2.0
 */
@ApiStatus.Experimental
public final class PageableInventoryImpl implements PageableInventory {

    private final PageableControls pageableControls;
    private final Component title;
    private final InventoryLayout layout;
    private final List<ISlot> items;
    private final int[] slotRange;
    private final InventoryLayout dataLayout;
    private GlobalInventoryBuilder globalInventoryBuilder;
    private int currentPage;
    private int startPageItemIndex;
    private int endIndex;
    private int maxPages;

    private final InventoryClick forwardClick;
    private final InventoryClick backwardsClick;

    private ISlot oldBackSlot;
    private ISlot forwardSlot;

    PageableInventoryImpl(
            @NotNull Component title,
            @NotNull InventoryType type,
            @NotNull PageableControls controls,
            @NotNull InventoryLayout layout,
            @NotNull List<ISlot> items,
            int @NotNull ... slotRange) {
        this.title = title;
        this.pageableControls = controls;
        this.layout = layout;
        this.dataLayout = new InventoryLayout(type);
        this.items = items;
        this.slotRange = slotRange;
        this.globalInventoryBuilder = new GlobalInventoryBuilder(title, type);
        this.dataLayout.blank(this.slotRange);
        this.startPageItemIndex = 0;
        this.globalInventoryBuilder.setLayout(this.layout);

        var backSlot = this.layout.getSlot(this.pageableControls.getBackSlot());

        this. oldBackSlot = backSlot == null ? EMPTY_SLOT : ISlot.of(backSlot);

        var forwardSlot = this.layout.getSlot(this.pageableControls.getNextSlot());

        this.forwardSlot = forwardSlot == null ? EMPTY_SLOT : ISlot.of(forwardSlot);

        this.forwardClick = (player, clickType, slot, condition) -> {
            this.update(PageDirection.FORWARD);
            condition.setCancel(true);
        };

        this.backwardsClick = (player, clickType, slot, condition) -> {
          this.update(PageDirection.BACKWARDS);
          condition.setCancel(true);
        };

        this.globalInventoryBuilder.setDataLayoutFunction(inventoryLayout -> dataLayout);
        this.currentPage = 1;
        this.dataLayout.blank(slotRange);

        if (!this.items.isEmpty()) {
            for (int i = 0; i < slotRange.length; i++) {
                this.dataLayout.setItem(slotRange[i], this.items.get(i));
            }
        }

        this.globalInventoryBuilder.invalidateDataLayout();
        this.globalInventoryBuilder.register();

        if (this.items.size() > this.slotRange.length) {
            this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), this.forwardClick);
        }

        this.endIndex = this.slotRange.length;
        this.updateMaxPages();
    }

    public void update(@NotNull PageDirection pageDirection) {
        switch (pageDirection) {
            case BACKWARDS -> this.previousPage();
            case FORWARD -> this.nextPage();
            default -> this.updatePage();
        }
    }

    private void updatePage() {
        this.updateItems();
        if (this.dataLayout.getSlot(this.slotRange[endIndex - 1]) != null) {
            this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), this.forwardClick);
            this.globalInventoryBuilder.invalidateLayout();
        }

        this.globalInventoryBuilder.invalidateDataLayout();
        this.updateMaxPages();
    }

    private void nextPage() {
        if (this.startPageItemIndex < this.items.size() - 1) {
            this.endIndex += this.slotRange.length;
            this.currentPage += 1;
            this.startPageItemIndex += this.slotRange.length;

            var backSlot = this.layout.getSlot(this.pageableControls.getBackSlot());
            if (backSlot != null && backSlot.getItem().material() != this.pageableControls.getBackMaterial()) {
                this.layout.setItem(this.pageableControls.getBackSlot(), this.pageableControls.getBackButton().get(), backwardsClick);
                this.globalInventoryBuilder.invalidateLayout();
            }

            if (this.layout.getSlot(this.pageableControls.getBackSlot()) == null) {
                this.layout.setItem(this.pageableControls.getBackSlot(), this.pageableControls.getNextButton().get(), this.backwardsClick);
            }

            if (currentPage == getMaxPages()) {
                this.layout.setItem(this.pageableControls.getNextSlot(), forwardSlot);
                this.globalInventoryBuilder.invalidateLayout();
            }

            this.updateItems();
            this.globalInventoryBuilder.invalidateDataLayout();
        }
    }

    private void previousPage() {
        if (this.currentPage >= 0) {
            if (this.items.size() % this.slotRange.length != 0 && this.endIndex == this.items.size() - 1) {
                this.endIndex -= this.items.size() % this.slotRange.length;
            } else {
                this.endIndex -= this.slotRange.length;
            }
            this.startPageItemIndex -= this.slotRange.length;
            this.currentPage -= 1;

            if (this.items.size() > this.slotRange.length) {
                var forwardSlot = this.layout.getSlot(this.pageableControls.getNextSlot());

                if (forwardSlot != null && forwardSlot.getItem().material() != this.pageableControls.getForwardMaterial()) {
                    this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), forwardClick);
                    this.globalInventoryBuilder.invalidateLayout();
                }
            }


            if (this.currentPage == 1) {
                this.layout.setItem(this.pageableControls.getBackSlot(), oldBackSlot);
                this.globalInventoryBuilder.invalidateLayout();
            }

            this.updateItems();
            this.globalInventoryBuilder.invalidateDataLayout();
        }
    }

    /**
     * Update which items should be displayed in the inventory.
     */
    private void updateItems() {
        for (int i = 0; i < this.slotRange.length; i++) {
            var newIndex = i + this.startPageItemIndex;
            if (newIndex >= this.items.size()) {
                this.dataLayout.setItem(this.slotRange[i], EMPTY_SLOT);
            } else {
                this.dataLayout.setItem(this.slotRange[i], this.items.get(newIndex));
            }
        }
    }

    private void updateMaxPages() {
        if (this.items.isEmpty() || this.items.size() <= this.slotRange.length) {
            this.maxPages = 1;
            return;
        }
        var pageAmount = this.items.size() / this.slotRange.length;
        if (this.items.size() % this.slotRange.length != 0) {
            pageAmount++;
        }
        this.maxPages = pageAmount;
    }

    @Override
    public void unregister() {

    }

    @Override
    public void open(@NotNull Player player) {
        player.openInventory(this.globalInventoryBuilder.getInventory());
    }

    @Override
    public void open(@NotNull Player player, int page) {
        Check.argCondition(page > this.maxPages, "The page index is to high");

    }

    @Override
    public void open() {

    }

    @Override
    public void add(@NotNull ISlot slot) {
        this.items.add(slot);
        this.update(PageDirection.UPDATE);
    }

    @Override
    public void add(@NotNull List<ISlot> slots) {
        this.items.addAll(slots);
        this.update(PageDirection.UPDATE);
    }

    @Override
    public void remove(@NotNull ISlot slot) {
        if (this.items.remove(slot)) {
            this.update(PageDirection.UPDATE);
        }

    }

    @Override
    public void remove(@NotNull List<ISlot> inventorySlots) {
        if (this.items.removeAll(inventorySlots)) {
            this.update(PageDirection.UPDATE);
        }
    }

    /**
     * Returns the maximum amount of pages.
     * @return the given maximum page count
     */
    @Override
    public int getMaxPages() {
        return this.maxPages;
    }
}
