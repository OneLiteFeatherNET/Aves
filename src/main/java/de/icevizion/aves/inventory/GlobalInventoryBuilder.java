package de.icevizion.aves.inventory;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GlobalInventoryBuilder extends InventoryBuilder {

    private final String title;

    private Inventory inventory;
    private Holder holder;

    public GlobalInventoryBuilder(String title, InventoryRows rows) {
        super(rows);
        this.title = title;

        holder = new Holder(this);
    }

    public GlobalInventoryBuilder(String title, int slots) {
        super(slots);
        this.title = title;

        holder = new Holder(this);
    }

    //Event listeners
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() != holder)
            return;

        handleClose(event);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getView().getTopInventory().getHolder() != holder)
            return;

        handleClick(event);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory().getHolder() != holder)
            return;

        //Todo:
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
            inventory = Bukkit.createInventory((holder = new Holder(this)), getRows().getSize(), title);
            holder.setInventory(inventory);
            holder.setInventoryTitle(title);
            applyLayout = true;
        }

        updateInventory(inventory, title, null, null, applyLayout);
        update();
    }

    @Override
    protected void applyDataLayout() {
        synchronized (this) {
            if (getDataLayout() != null) {
                var contents = inventory.getContents();
                getDataLayout().applyLayout(contents, null, null);
                inventory.setContents(contents);
                update();
            }
        }
    }

    protected void update() {
        if (inventory.getViewers().isEmpty()) return;

        for (HumanEntity viewer : inventory.getViewers()) {
            ((Player)viewer).updateInventory();
        }
    }
}
