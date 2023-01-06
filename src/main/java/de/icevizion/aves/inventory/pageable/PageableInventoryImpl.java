package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.GlobalInventoryBuilder;
import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.slot.ISlot;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        this.globalInventoryBuilder.setDataLayoutFunction(inventoryLayout -> dataLayout);
        this.currentPage = 1;

        if (this.items.size() < this.slotRange.length) {
            this.maxPages = 1;
        } else {
            this.maxPages = this.items.size() / this.slotRange.length;
        }
    }

    public void update(@NotNull PageDirection pageDirection) {
        switch (pageDirection) {
            case BACKWARDS -> this.previousPage();
            case FORWARD -> this.nextPage();
            default -> this.updatePage();
        }
    }

    private void updatePage() {
        for (int i = startPageItemIndex; i < endIndex; i++) {
            var item = this.items.get(i);
            this.dataLayout.setItem(this.slotRange[i], item);
        }

        if (this.dataLayout.getSlot(this.slotRange[endIndex]) != null) {
            this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), (player, clickType, slot, condition) -> {
               this.update(PageDirection.FORWARD);
               player.sendMessage("Forward");
            });
            this.globalInventoryBuilder.invalidateLayout();
        }

        this.globalInventoryBuilder.invalidateDataLayout();
    }

    private void nextPage() {
        if (this.endIndex != this.items.size() - 1) {
            if (this.endIndex + this.slotRange.length >= this.items.size()) {
                this.endIndex += this.items.size() % this.slotRange.length;
            } else {
                this.endIndex += this.slotRange.length;
            }
            this.currentPage += 1;
            this.startPageItemIndex += this.slotRange.length;

            if (currentPage == getMaxPages()) {
                this.layout.setItem(this.pageableControls.getNextSlot(), ItemStack.AIR);
                this.globalInventoryBuilder.invalidateLayout();
            }

            for (int i = this.startPageItemIndex; i < this.endIndex; i++) {
                var item = this.items.get(i);
                this.dataLayout.setItem(this.slotRange[i], item);
            }

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

            if (this.currentPage == 0) {
                this.layout.setItem(this.pageableControls.getBackSlot(), ItemStack.AIR);
                this.globalInventoryBuilder.invalidateLayout();
            }

            for (int i = startPageItemIndex; i < endIndex; i++) {
                var item = this.items.get(i);
                this.dataLayout.setItem(this.slotRange[i], item);
            }

            this.globalInventoryBuilder.invalidateDataLayout();
        }
    }

    private void updateMaxPages() {
        if (this.items.size() % this.slotRange.length != 0) {
            this.maxPages = this.items.size() / this.slotRange.length;
        }
    }

    @Override
    public void open(@NotNull Player player) {

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
