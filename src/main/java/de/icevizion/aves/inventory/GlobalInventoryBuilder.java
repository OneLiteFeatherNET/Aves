package de.icevizion.aves.inventory;

import org.bukkit.Bukkit;
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

    private Function<InventoryCloseEvent, Boolean> closeListener;

    public GlobalInventoryBuilder(String title, InventoryRows rows, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        super(rows, dataLayoutProvider);
        this.title = title;

        holder = new Holder(this);
    }

    public GlobalInventoryBuilder(String title, int slots, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        super(slots, dataLayoutProvider);
        this.title = title;

        holder = new Holder(this);
    }

    //Event listeners
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTopInventory().getHolder() != holder)
            return;

        if (closeListener != null) {
            var closeInv = closeListener.apply(event);
            var holder = (Holder) event.getView().getTopInventory().getHolder();

            if (!closeInv) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> event.getPlayer().openInventory(holder.getInventory()), 3);
            }
        }
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
        inventory.getViewers().forEach(humanEntity -> ((Player) humanEntity).updateInventory());
    }

    @Override
    protected void applyDataLayout() {
        synchronized (getDataLayoutFuture()) {
            if (getDataLayout() != null) {
                var contents = inventory.getContents();
                getDataLayout().applyLayout(contents, null, null);
                inventory.setContents(contents);
                inventory.getViewers().forEach(humanEntity -> ((Player) humanEntity).updateInventory());
            }
        }
    }
}
