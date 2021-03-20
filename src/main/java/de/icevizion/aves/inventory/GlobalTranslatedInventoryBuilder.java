package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.TranslatedObjectCache;
import at.rxcki.strigiformes.text.TextData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GlobalTranslatedInventoryBuilder extends InventoryBuilder {

    private final MessageProvider messageProvider;
    private final TranslatedObjectCache<Inventory> inventoryTranslatedObjectCache;

    private TextData titleData;

    public GlobalTranslatedInventoryBuilder(InventoryRows rows, MessageProvider messageProvider) {
        super(rows);
        this.messageProvider = messageProvider;

        inventoryTranslatedObjectCache = createCache();
    }

    public GlobalTranslatedInventoryBuilder(int slots, MessageProvider messageProvider) {
        super(slots);
        this.messageProvider = messageProvider;
        inventoryTranslatedObjectCache = createCache();
    }

    private TranslatedObjectCache<Inventory> createCache() {
        return new TranslatedObjectCache<>(locale -> {
            var holder = new Holder(this);
            var title = messageProvider.getTextProvider().format(titleData, locale);
            var inventory = Bukkit.createInventory(holder, getRows().getSize(), title);
            holder.setInventory(inventory);
            holder.setInventoryTitle(title);

            updateInventory(inventory, title, locale, messageProvider, true);

            return inventory;
        });
    }

    @Override
    public Inventory getInventory(Locale locale) {
        if (!dataLayoutValid || !inventoryLayoutValid) {
            updateInventory();
        }

        return inventoryTranslatedObjectCache.get(locale);
    }

    @Override
    protected boolean isInventoryOpened() {
        for (var inventory : inventoryTranslatedObjectCache.asMap().values())
            if (!inventory.getViewers().isEmpty())
                return true;
        return false;
    }

    @Override
    protected void updateInventory() {
        for (var entry : inventoryTranslatedObjectCache.asMap().entrySet()) {
            var locale = entry.getKey();
            updateInventory(entry.getValue(), messageProvider.getTextProvider().format(titleData, locale), locale, messageProvider, true);
            entry.getValue().getViewers().forEach(humanEntity -> ((Player) humanEntity).updateInventory());
        }
    }

    @Override
    protected void applyDataLayout() {
        synchronized (this) {
            if (getDataLayout() != null) {
                System.out.println("Applying data layouts " + getDataLayout());
                for (var entry : inventoryTranslatedObjectCache.asMap().entrySet()) {
                    var contents = entry.getValue().getContents();
                    getDataLayout().applyLayout(contents, entry.getKey(), messageProvider);
                    entry.getValue().setContents(contents);
                    entry.getValue().getViewers().forEach(humanEntity -> ((Player) humanEntity).updateInventory());
                }
            }
        }
    }

    //Event listeners
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof Holder)
                || ((Holder) event.getView().getTopInventory().getHolder()).getInventoryBuilder() != this)
            return;

        handleClose(event);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getView().getTopInventory().getHolder() instanceof Holder)
                || ((Holder) event.getView().getTopInventory().getHolder()).getInventoryBuilder() != this)
            return;

        handleClick(event);
    }

    public TextData getTitleData() {
        return titleData;
    }

    public void setTitleData(TextData titleData) {
        this.titleData = titleData;
    }
}
