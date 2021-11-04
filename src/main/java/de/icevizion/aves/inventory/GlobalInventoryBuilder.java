package de.icevizion.aves.inventory;

import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;

import java.util.Locale;
import java.util.function.Consumer;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GlobalInventoryBuilder extends InventoryBuilder {

    private final String title;

    private Inventory inventory;
    //private Holder holder;

    public GlobalInventoryBuilder(String title, InventoryRows rows) {
        super(rows);
        this.title = title;
    }

    public GlobalInventoryBuilder(String title, int slots) {
        super(slots);
        this.title = title;

    }

    private Consumer<InventoryPreClickEvent> preClickListener() {
        return clickEvent -> {
            if (this.inventory.getViewers().contains(clickEvent.getPlayer())) {
                handleClick(clickEvent);
            }
        };
    }

    private Consumer<InventoryCloseEvent> closeListener() {
        return closeEvent -> {
            if (this.inventory.getViewers().contains(closeEvent.getPlayer())) {
                handleClose(closeEvent);
            }
        };
    }

    @Override
    public Inventory getInventory(Locale ignored) {
        if (dataLayoutValid && inventoryLayoutValid && inventory != null)
            return inventory;
        updateInventory();

        return inventory;
    }

    @Override
    protected boolean isInventoryOpened() {
        return !inventory.getViewers().isEmpty();
    }

    @Override
    protected void updateInventory() {
        boolean applyLayout = !inventoryLayoutValid;
        if (inventory == null) {
            this.inventory = new Inventory(getRows().getType(), title);
            applyLayout = true;
        }

        updateInventory(inventory, title, null, null, applyLayout);
        update();
    }

    @Override
    protected void applyDataLayout() {
        synchronized (this) {
            if (getDataLayout() != null) {
                var contents = inventory.getItemStacks();
                getDataLayout().applyLayout(contents, null, null);
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i].getMaterial() == Material.AIR) continue;
                    this.inventory.setItemStack(i, contents[i]);
                }
                update();
            }
        }
    }

    protected void update() {
        if (inventory.getViewers().isEmpty()) return;
        inventory.update();
    }
}
