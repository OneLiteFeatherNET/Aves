package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.PersonalInventoryBuilder;
import de.icevizion.aves.inventory.function.InventoryClick;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.item.IItem;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

import static de.icevizion.aves.inventory.util.InventoryConstants.BLANK_SLOT;

/**
 * The class is the implementation for the {@link PageableInventory} interface.
 * There are also methods to move a page forward or backward and update the current page.
 * The update method updates the area in which the items should be displayed.
 * It will also set or remove the item accordingly to change a page depending on the number of items
 *
 * @author Joltra
 * @author theEvilReaper
 * @version 1.1.0
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
    private int endIndex;
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
        this.endIndex = this.slotRange.length;
        this.titleData = titleData;
        this.updateMaxPages();
        this.builder = new PersonalInventoryBuilder(getNewTitle(), type, player);
        this.dataLayout.blank(this.slotRange);
        this.startPageItemIndex = 0;
        this.builder.setLayout(this.layout);

        var backSlot = this.layout.getSlot(this.pageableControls.getBackSlot());

        this.oldBackSlot = backSlot == null ? BLANK_SLOT : ISlot.of(backSlot);

        this.forwardSlot = this.layout.getSlot(this.pageableControls.getNextSlot());

        ISlot givenForwardSlot = this.layout.getSlot(this.pageableControls.getNextSlot());
        this.forwardSlot = givenForwardSlot == null ? BLANK_SLOT : ISlot.of(givenForwardSlot);

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
        this.initItems();
        this.builder.invalidateDataLayout();
        this.builder.register();

        if (this.items.size() > this.slotRange.length) {
            this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), this.forwardClick);
        }
    }

    /**
     * Triggers a specific update to the inventory.
     * If the given action os {@link PageAction#REFRESH} it updates the items on the current page.
     * The {@link PageAction#BACKWARDS} and {@link PageAction#FORWARD} updates the page boundaries and also updates the item content.
     *
     * @param pageAction the action which should be triggered
     */
    public void update(@NotNull PageAction pageAction) {
        switch (pageAction) {
            case BACKWARDS -> this.previousPage();
            case FORWARD -> this.nextPage();
            default -> this.updatePage();
        }
    }

    /**
     * Fills the items for the first page into the data layout when the list contains items.
     * If the list doesn't contain any entries nothing will happen.
     */
    private void initItems() {
        if (this.items.isEmpty()) return;
        for (int i = 0; i < this.items.size() && i < slotRange.length; i++) {
            this.dataLayout.setItem(slotRange[i], this.items.get(i));
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
     * This method updates all items which are currently displayed at the page.
     */
    private void updatePage() {
        this.updateItems();
        if (this.dataLayout.getSlot(this.slotRange[endIndex - 1]) != null) {
            setControlItems(this.pageableControls.getNextButton(), this.pageableControls.getNextSlot(), true);
            this.builder.invalidateLayout();
        }

        this.builder.invalidateDataLayout();
        this.updateMaxPages();
    }

    /**
     * The method is called when the updatePage method is called with the {@link PageAction#FORWARD} value.
     * Its determines the next index boundary's and updates the inventory view for the player
     * Also it checks if the forward button must be replaced with the old {@link net.minestom.server.item.ItemStack} reference.
     */
    private void nextPage() {
        if (this.startPageItemIndex < this.items.size() - 1) {
            this.endIndex += this.slotRange.length;
            this.currentPage += 1;
            this.startPageItemIndex += this.slotRange.length;

            var backSlot = this.layout.getSlot(this.pageableControls.getBackSlot());
            if (backSlot != null && backSlot.getItem().material() != this.pageableControls.getBackMaterial()) {
                setControlItems(this.pageableControls.getBackButton(), this.pageableControls.getBackSlot(), false);
                this.builder.invalidateLayout();
            }

            if (this.layout.getSlot(this.pageableControls.getBackSlot()) == null) {
                setControlItems(this.pageableControls.getBackButton(), this.pageableControls.getBackSlot(), false);
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

    /**
     * The method is called when the updatePage method is called with the {@link PageAction#BACKWARDS} value.
     * Its determines the next index boundary's and updates the inventory view for the player
     * Also it checks if the back button must be replaced with the old {@link net.minestom.server.item.ItemStack} reference.
     */
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
                var forwardItemSlot = this.layout.getSlot(this.pageableControls.getNextSlot());

                if (forwardItemSlot != null && forwardItemSlot.getItem().material() != this.pageableControls.getForwardMaterial()) {
                    setControlItems(this.pageableControls.getNextButton(), this.pageableControls.getNextSlot(), true);
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
        Set<Player> viewers = this.builder.getInventory().getViewers();
        //Check if the inventory contains viewer and when close the inventories for the viewers to prevent issue
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

        //The values are the same. Ignore page update
        if (page == this.currentPage) {
            player.openInventory(this.builder.getInventory());
            return;
        }

        if (page == 1) {
            this.startPageItemIndex = 0;
            this.endIndex = this.slotRange.length;
        } else {
            this.startPageItemIndex = this.slotRange.length * (page - 1);
            this.endIndex = this.startPageItemIndex + this.slotRange.length;

        }
        this.currentPage = page;
        this.updateItems();
        builder.open();
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
