package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.PersonalInventoryBuilder;
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
public final class PlayerPageableInventoryImpl implements PageableInventory {

    private final PageableControls pageableControls;
    private final Component title;
    private final InventoryLayout layout;
    private final List<ISlot> items;
    private final int[] slotRange;
    private final InventoryLayout dataLayout;
    private final InventoryClick forwardClick;
    private final InventoryClick backwardsClick;
    private final boolean pagesInTitle;
    private final PersonalInventoryBuilder builder;
    private int currentPage;
    private int startPageItemIndex;
    private int endIndex;
    private int maxPages;
    private ISlot oldBackSlot;
    private ISlot forwardSlot;

    private final Player player;

    PlayerPageableInventoryImpl(
            @NotNull Player player,
            @NotNull Component title,
            @NotNull InventoryType type,
            @NotNull PageableControls controls,
            @NotNull InventoryLayout layout,
            @NotNull List<ISlot> items,
            boolean pagesInTitle,
            int @NotNull ... slotRange) {
        this.player = player;
        this.title = title;
        this.pageableControls = controls;
        this.layout = layout;
        this.dataLayout = new InventoryLayout(type);
        this.items = items;
        this.currentPage = 1;
        this.slotRange = slotRange;
        this.pagesInTitle = pagesInTitle;
        this.endIndex = this.slotRange.length;
        this.updateMaxPages();
        this.builder = new PersonalInventoryBuilder(pagesInTitle ? getNewTitle() : title, type, player);
        this.dataLayout.blank(this.slotRange);
        this.startPageItemIndex = 0;
        this.builder.setLayout(this.layout);

        var backSlot = this.layout.getSlot(this.pageableControls.getBackSlot());

        this.oldBackSlot = backSlot == null ? EMPTY_SLOT : ISlot.of(backSlot);

        var forwardSlot = this.layout.getSlot(this.pageableControls.getNextSlot());

        this.forwardSlot = forwardSlot == null ? EMPTY_SLOT : ISlot.of(forwardSlot);

        this.forwardClick = (clickPlayer, clickType, slot, condition) -> {
            this.update(PageAction.FORWARD);
            condition.setCancel(true);
        };

        this.backwardsClick = (clickPlayer, clickType, slot, condition) -> {
          this.update(PageAction.BACKWARDS);
          condition.setCancel(true);
        };

        this.builder.setDataLayoutFunction(inventoryLayout -> dataLayout);
        this.dataLayout.blank(slotRange);

        if (!this.items.isEmpty()) {
            for (int i = 0; i < slotRange.length; i++) {
                this.dataLayout.setItem(slotRange[i], this.items.get(i));
            }
        }

        this.builder.invalidateDataLayout();
        this.builder.register();

        if (this.items.size() > this.slotRange.length) {
            this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), this.forwardClick);
        }
    }

    public void update(@NotNull PageAction pageAction) {
        switch (pageAction) {
            case BACKWARDS -> this.previousPage();
            case FORWARD -> this.nextPage();
            default -> this.updatePage();
        }
    }

    private void updatePage() {
        this.updateItems();
        if (this.dataLayout.getSlot(this.slotRange[endIndex - 1]) != null) {
            this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), this.forwardClick);
            this.builder.invalidateLayout();
        }

        this.builder.invalidateDataLayout();
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
                this.builder.invalidateLayout();
            }

            if (this.layout.getSlot(this.pageableControls.getBackSlot()) == null) {
                this.layout.setItem(this.pageableControls.getBackSlot(), this.pageableControls.getNextButton().get(), this.backwardsClick);
            }

            if (currentPage == getMaxPages()) {
                this.layout.setItem(this.pageableControls.getNextSlot(), forwardSlot);
                this.builder.invalidateLayout();
            }

            this.updateItems();
            this.updateTitle();
            this.builder.invalidateDataLayout();
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
                    this.builder.invalidateLayout();
                }
            }

            if (this.currentPage == 1) {
                this.layout.setItem(this.pageableControls.getBackSlot(), oldBackSlot);
                this.builder.invalidateLayout();
            }

            this.updateItems();
            this.updateTitle();
            this.builder.invalidateDataLayout();
        }
    }

    private void updateTitle() {
        if (this.pagesInTitle) {
            var component = getNewTitle();
            this.builder.setTitleComponent(component);
        }
    }

    private @NotNull Component getNewTitle() {
        return title.append(Component.text(" " + currentPage + "/" + maxPages));
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

    /**
     * Updates the max amount of pages.
     */
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
        this.builder.unregister();
    }

    /**
     * Opens the current page for a given player
     * @param player the player who receives the inventory
     */
    @Override
    public void open(@NotNull Player player) {
        throw new UnsupportedOperationException("Not supported for this specific implementation");
    }

    /**
     * Opens the specific page for the player.
     * @param page the page which should be displayed in the inventory
     */
    @Override
    public void open(int page) {
        Check.argCondition(page < 1, "The page index can't be zero oder negativ");
        Check.argCondition(page > this.maxPages, "The page index is to high");

        //The values are the same. Ignore page update
        if (page == this.currentPage) {
            player.openInventory(this.builder.getInventory());
            return;
        }

        if (page == 1) {
            this.startPageItemIndex = 0;
            this.endIndex = this.slotRange.length;
        } else{
            this.startPageItemIndex = this.slotRange.length * (page - 1);
            this.endIndex = this.startPageItemIndex + this.slotRange.length;

        }
        this.currentPage = page;
        this.updateItems();
        builder.open();
    }

    @Override
    public void open(@NotNull Player player, int page) {
        throw new UnsupportedOperationException("In a PlayerInventory it's not possible to open it for another player");
    }

    @Override
    public void open() {
        this.builder.open();
    }

    /**
     * Add a entry to the inventory.
     * @param slot the slot to add
     */
    @Override
    public void add(@NotNull ISlot slot) {
        this.items.add(slot);
        this.update(PageAction.UPDATE);
    }

    /**
     * Add a list of entries to the inventory.
     * @param slots the list that has all entries to add
     */
    @Override
    public void add(@NotNull List<ISlot> slots) {
        this.items.addAll(slots);
        this.update(PageAction.UPDATE);
    }

    /**
     * Removes a entry from the inventory
     * @param slot the slot to remove
     */
    @Override
    public void remove(@NotNull ISlot slot) {
        if (this.items.remove(slot)) {
            this.update(PageAction.UPDATE);
        }
    }

    /**
     * Removes a given list of entries from the inventory.
     * @param inventorySlots the list which contains the slots to remove
     */
    @Override
    public void remove(@NotNull List<ISlot> inventorySlots) {
        if (this.items.removeAll(inventorySlots)) {
            this.update(PageAction.UPDATE);
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
