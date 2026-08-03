package net.theevilreaper.aves.inventory.pageable;

import net.theevilreaper.aves.inventory.PersonalInventoryBuilder;
import net.theevilreaper.aves.inventory.click.ClickHolder;
import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.item.IItem;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static net.theevilreaper.aves.inventory.util.InventoryConstants.BLANK_SLOT;

/**
 * The class is the implementation for the {@link PageableInventory} interface.
 * There are also methods to move a page forward or backward and update the current page.
 * The update method updates the area in which the items should be displayed.
 * It will also set or remove the item accordingly to change a page depending on the number of items
 *
 * @author Joltra
 * @author theEvilReaper
 * @version 1.1.2
 * @since 1.2.0
 */
@ApiStatus.Experimental
public final class PlayerPageableInventoryImpl implements PageableInventory {

    private final PageableControls pageableControls;
    private final InventoryLayout layout;
    private final List<ISlot> items;
    private final InventoryLayout dataLayout;
    private final InventoryClick forwardClick;
    private final InventoryClick backwardsClick;
    private final PersonalInventoryBuilder builder;
    private final TitleData titleData;
    private ISlot oldBackSlot;
    private ISlot forwardSlot;
    private final Player player;
    private final int[] slotRange;
    private int currentPage;
    private int startPageItemIndex;
    private int maxPages;

    /**
     * Creates a new instance from the {@link PlayerPageableInventoryImpl} with the given values from the constructor.
     *
     * @param player    the player who owns the inventory
     * @param type      the type for the inventory
     * @param controls  the class which contains the control to swap pages
     * @param layout    the layout that contains the background layout
     * @param items     the list of items to display
     * @param slotRange the area where the items would be displayed
     */
    PlayerPageableInventoryImpl(
            @NotNull Player player,
            @NotNull InventoryType type,
            @NotNull PageableControls controls,
            @NotNull InventoryLayout layout,
            @NotNull List<ISlot> items,
            @NotNull TitleData titleData,
            int @NotNull ... slotRange
    ) {
        this.player = player;
        this.pageableControls = controls;
        this.layout = layout;
        this.dataLayout = InventoryLayout.fromType(type);
        this.items = items;
        this.currentPage = 1;
        this.slotRange = slotRange;
        this.titleData = titleData;
        this.updateMaxPages();
        this.builder = new PersonalInventoryBuilder(getNewTitle(), type, player);
        this.dataLayout.blank(this.slotRange);
        this.startPageItemIndex = 0;
        this.builder.setLayout(this.layout);

        var backSlot = this.layout.getSlot(this.pageableControls.getBackSlot());
        this.oldBackSlot = backSlot == null ? BLANK_SLOT : ISlot.of(backSlot);

        ISlot givenForwardSlot = this.layout.getSlot(this.pageableControls.getNextSlot());
        this.forwardSlot = givenForwardSlot == null ? BLANK_SLOT : ISlot.of(givenForwardSlot);

        this.forwardClick = (_, _, _, _, result) -> {
            this.update(PageAction.FORWARD);
            result.accept(ClickHolder.cancelClick());
        };

        this.backwardsClick = (_, _, _,  _,result) -> {
            this.update(PageAction.BACKWARDS);
            result.accept(ClickHolder.cancelClick());
        };

        this.builder.setDataLayoutFunction(inventoryLayout -> dataLayout);
        this.updateItems();
        this.updateControls();
        this.builder.invalidateDataLayout();
        this.builder.register();
    }

    /**
     * Triggers a specific update to the inventory.
     * If the given action os {@link PageAction#REFRESH} it updates the items on the current page.
     * The {@link PageAction#BACKWARDS} and {@link PageAction#FORWARD} updates the page boundaries and also updates the item content.
     *
     * @param pageAction the action that should be triggered
     */
    public void update(@NotNull PageAction pageAction) {
        switch (pageAction) {
            case BACKWARDS -> this.previousPage();
            case FORWARD -> this.nextPage();
            default -> this.updatePage();
        }
    }

    /**
     * Updates the control item at a specific position in the inventory layout.
     *
     * @param controlItem the item to set
     * @param slotIndex   the index for the item
     * @param forward     true for the forward logic otherwise the backwards logic
     */
    private void setControlItems(@NotNull IItem controlItem, int slotIndex, boolean forward) {
        this.layout.setItem(slotIndex, controlItem.get(), forward ? forwardClick : backwardsClick);
    }

    /**
     * This method updates all items that are currently displayed at the page.
     */
    private void updatePage() {
        this.updateMaxPages();
        if (this.currentPage > this.maxPages) {
            this.currentPage = this.maxPages;
        }
        this.updateItems();
        this.updateControls();
        this.updateTitle();
        this.builder.invalidateDataLayout();
    }

    /**
     * The method is called when the updatePage method is called with the {@link PageAction#FORWARD} value.
     * Its determines the next index boundary's and updates the inventory view for the player
     * Also it checks if the forward button must be replaced with the old {@link net.minestom.server.item.ItemStack} reference.
     */
    private void nextPage() {
        if (this.currentPage < this.maxPages) {
            this.currentPage++;
            this.updateItems();
            this.updateControls();
            this.updateTitle();
            this.builder.invalidateDataLayout();
        }
    }

    /**
     * The method is called when the updatePage method is called with the {@link PageAction#BACKWARDS} value.
     * Its determines the next index boundary's and updates the inventory view for the player
     * Also it checks if the back button must be replaced with the old {@link net.minestom.server.item.ItemStack} reference.
     */
    private void previousPage() {
        if (this.currentPage > 1) {
            this.currentPage--;
            this.updateItems();
            this.updateControls();
            this.updateTitle();
            this.builder.invalidateDataLayout();
        }
    }

    /**
     * Updates the navigation control items (next / previous buttons) on the inventory layout.
     */
    private void updateControls() {
        if (this.currentPage > 1) {
            setControlItems(this.pageableControls.getBackButton(), this.pageableControls.getBackSlot(), false);
        } else {
            this.layout.setItem(this.pageableControls.getBackSlot(), oldBackSlot);
        }

        if (this.currentPage < this.maxPages) {
            setControlItems(this.pageableControls.getNextButton(), this.pageableControls.getNextSlot(), true);
        } else {
            this.layout.setItem(this.pageableControls.getNextSlot(), forwardSlot);
        }
        this.builder.invalidateLayout();
    }

    /**
     * Updates the inventory title when the given indicator boolean is true.
     */
    private void updateTitle() {
        if (this.titleData.showPageNumbers()) {
            var component = getNewTitle();
            this.builder.setTitleComponent(component);
        }
    }

    /**
     * Returns a {@link Component} which contains the current page and the max page value as string.
     *
     * @return the created component
     */
    private @NotNull Component getNewTitle() {
        if (this.titleData.showPageNumbers() && this.titleData.pageMapper() == null) {
            throw new IllegalStateException("If the page numbers should be displayed the page mapper must be set");
        }

        TitleMapper mapper = this.titleData.pageMapper();

        if (mapper == null) {
            return titleData.title();
        }

        return titleData.title().append(mapper.apply(currentPage, maxPages));
    }

    /**
     * Update which items should be displayed in the inventory.
     */
    private void updateItems() {
        // Convert 1-based page number to 0-based list offset (e.g., page 1 starts at index 0)
        this.startPageItemIndex = (this.currentPage - 1) * this.slotRange.length;
        for (int i = 0; i < this.slotRange.length; i++) {
            var newIndex = i + this.startPageItemIndex;
            if (newIndex >= this.items.size()) {
                this.dataLayout.setItem(this.slotRange[i], BLANK_SLOT);
            } else {
                this.dataLayout.setItem(this.slotRange[i], this.items.get(newIndex));
            }
        }
    }

    /**
     * Calculates the maximum page index based on the given list with the items and the slotRange.
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

    /**
     * Unregisters the event listener structure from the global event node.
     */
    @Override
    public void unregister() {
        Set<? extends Player> viewers = this.builder.getInventory().getViewers();
        if (!viewers.isEmpty()) {
            viewers.forEach(Player::closeInventory);
        }
        this.builder.unregister();
    }

    /**
     * Opens the current page for a given player
     *
     * @param player the player who receives the inventory
     */
    @Override
    public void open(@NotNull Player player) {
        throw new UnsupportedOperationException("Not supported for this specific implementation");
    }

    /**
     * Opens the specific page for the player.
     *
     * @param page the page which should be displayed in the inventory
     */
    @Override
    public void open(int page) {
        Check.argCondition(page < 1, "The page index can't be zero or negative");
        Check.argCondition(page > this.maxPages, "The page index is to high");

        if (page == this.currentPage) {
            player.openInventory(this.builder.getInventory());
            return;
        }

        this.currentPage = page;
        this.updateItems();
        this.updateControls();
        this.updateTitle();
        this.builder.invalidateDataLayout();
        this.builder.open();
    }

    /**
     * This method is not supported in the implementation of the {@link PlayerPageableInventoryImpl}.
     * It throws an exception when the method receives a call in this context
     *
     * @param player the player who receives the inventory
     * @param page   the page number
     */
    @Override
    public void open(@NotNull Player player, int page) {
        throw new UnsupportedOperationException("In a PlayerInventory it's not possible to open it for another player");
    }

    /**
     * Opens the inventory for the given player.
     */
    @Override
    public void open() {
        this.builder.open();
    }

    /**
     * Add an entry to the inventory.
     *
     * @param slot the slot to add
     */
    @Override
    public void add(@NotNull ISlot slot) {
        this.items.add(slot);
        this.update(PageAction.REFRESH);
    }

    /**
     * Add a list of entries to the inventory.
     *
     * @param slots the list that has all entries to add
     */
    @Override
    public void add(@NotNull List<ISlot> slots) {
        this.items.addAll(slots);
        this.update(PageAction.REFRESH);
    }

    /**
     * Removes an entry from the inventory.
     *
     * @param slot the slot to remove
     */
    @Override
    public void remove(@NotNull ISlot slot) {
        if (this.items.remove(slot)) {
            this.update(PageAction.REFRESH);
        }
    }

    /**
     * Removes a given list of entries from the inventory.
     *
     * @param inventorySlots the list which contains the slots to remove
     */
    @Override
    public void remove(@NotNull List<ISlot> inventorySlots) {
        if (this.items.removeAll(inventorySlots)) {
            this.update(PageAction.REFRESH);
        }
    }

    /**
     * Returns the maximum amount of pages.
     *
     * @return the given maximum page count
     */
    @Override
    public int getMaxPages() {
        return this.maxPages;
    }
}
