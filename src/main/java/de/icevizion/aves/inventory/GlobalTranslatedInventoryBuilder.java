package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.TranslatedObjectCache;
import at.rxcki.strigiformes.text.TextData;
import net.minestom.server.event.inventory.InventoryCloseEvent;
import net.minestom.server.event.inventory.InventoryPreClickEvent;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.item.Material;

import java.util.Locale;
import java.util.function.Consumer;

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
            var title = messageProvider.getTextProvider().format(titleData, locale);
            var inventory = new Inventory(getRows().getType(), title);
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
            Inventory value = entry.getValue();

            if (value.getViewers().isEmpty()) continue;
            value.update();
        }
    }

    @Override
    protected void applyDataLayout() {
        synchronized (this) {
            if (getDataLayout() != null) {
                //System.out.println("Applying data layouts " + getDataLayout());
                for (var entry : inventoryTranslatedObjectCache.asMap().entrySet()) {
                    var contents = entry.getValue().getItemStacks();
                    getDataLayout().applyLayout(contents, entry.getKey(), messageProvider);
                    for (int i = 0; i < contents.length; i++) {
                        if (contents[i].getMaterial() == Material.AIR) continue;
                        entry.getValue().setItemStack(i, contents[i]);
                        entry.getValue().update();
                    }
                }
            }
        }
    }

    private Consumer<InventoryPreClickEvent> preClickListener() {
        return clickEvent -> {
            for (Inventory value : inventoryTranslatedObjectCache.asMap().values()) {
                if (value.getViewers().contains(clickEvent.getPlayer())) {
                    handleClick(clickEvent);
                }
            }
        };
    }

    private Consumer<InventoryCloseEvent> closeListener() {
        return closeEvent -> {
            for (Inventory value : inventoryTranslatedObjectCache.asMap().values()) {
                if (value.getViewers().contains(closeEvent.getPlayer())) {
                    handleClose(closeEvent);
                }
            }
        };
    }



    /**
     * Returns the {@link TextData} from the builder.
     * @return The underlying value
     */

    public TextData getTitleData() {
        return titleData;
    }

    /**
     * Overwrites the current {@link TextData} with a new one.
     * @param titleData The {@link TextData} to set.
     */

    public void setTitleData(TextData titleData) {
        this.titleData = titleData;
    }
}
