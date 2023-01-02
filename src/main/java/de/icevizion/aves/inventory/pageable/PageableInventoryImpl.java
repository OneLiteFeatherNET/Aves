package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.GlobalInventoryBuilder;
import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.item.IItem;
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
    private final List<IItem> items;
    private final int[] slotRange;
    private final InventoryLayout dataLayout;
    private GlobalInventoryBuilder globalInventoryBuilder;
    private int currentPage;
    private int startPageItemIndex;
    private int endIndex;

    PageableInventoryImpl(
            @NotNull Component title,
            @NotNull InventoryType type,
            @NotNull PageableControls controls,
            @NotNull InventoryLayout layout,
            @NotNull List<IItem> items,
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

        this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), (player, clickType, slot, condition) -> {
            player.sendMessage("Next");
            this.update(PageDirection.FORWARD);
        });
        this.globalInventoryBuilder.setLayout(this.layout);
        this.globalInventoryBuilder.setDataLayoutFunction(inventoryLayout -> dataLayout);
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
            this.dataLayout.setItem(this.slotRange[i], this.items.get(i).get(), (player, clickType, slot, condition) -> {
                player.sendMessage("Test click from items");
            });
        }

        if (this.dataLayout.getSlot(this.slotRange[endIndex]) != null) {
            this.layout.setItem(this.pageableControls.getNextSlot(), this.pageableControls.getNextButton().get(), (player, clickType, slot, condition) -> {
               player.sendMessage("Meep");
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
                this.dataLayout.setItem(this.slotRange[i], this.items.get(i).get(), (player, clickType, slot, condition) -> player.sendMessage("Test click from items"));
            }

            this.globalInventoryBuilder.invalidateDataLayout();
        }
    }

    private void previousPage() {
        if (this.currentPage >= 0) {
            if (this.items.size() % slotRange.length != 0 && endIndex == this.items.size() - 1) {
                endIndex -= this.items.size() % slotRange.length;
            } else {
                endIndex -= slotRange.length;
            }
            startPageItemIndex -= slotRange.length;
            currentPage -= 1;

            if (currentPage == 0) {
                this.layout.setItem(this.pageableControls.getBackSlot(), ItemStack.AIR);
                this.globalInventoryBuilder.invalidateLayout();
            }

            for (int i = startPageItemIndex; i < endIndex; i++) {
                this.dataLayout.setItem(this.slotRange[i], this.items.get(i).get(), (player, clickType, slot, condition) -> {
                    player.sendMessage("Test click from items");
                });
            }

            this.globalInventoryBuilder.invalidateDataLayout();
        }
    }

    @Override
    public void open(@NotNull Player player) {

    }

    @Override
    public void add(@NotNull IItem item) {
        this.items.add(item);
        this.update(PageDirection.UPDATE);
    }

    @Override
    public void add(@NotNull List<IItem> items) {
        this.items.addAll(items);
        this.update(PageDirection.UPDATE);
    }

    @Override
    public void remove(@NotNull IItem item) {
        if (this.items.remove(item)) {
            this.update(PageDirection.UPDATE);
        }
    }

    @Override
    public void remove(@NotNull List<IItem> items) {
        if (this.items.removeAll(items)) {
            this.update(PageDirection.UPDATE);
        }
    }

    @Override
    public int getMaxPages() {
        return 0;
    }
}
