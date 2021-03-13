package de.icevizion.aves.tinventory;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.TranslatedObjectCache;
import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GlobalTranslatedInventoryBuilder extends InventoryBuilder{

    private final MessageProvider messageProvider;
    private final TranslatedObjectCache<Inventory> inventoryTranslatedObjectCache;


    public GlobalTranslatedInventoryBuilder(InventoryRows rows, MessageProvider messageProvider, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        super(rows, dataLayoutProvider);
        this.messageProvider = messageProvider;
        inventoryTranslatedObjectCache = createCache();
    }

    public GlobalTranslatedInventoryBuilder(int slots, MessageProvider messageProvider, Function<InventoryLayout, InventoryLayout> dataLayoutProvider) {
        super(slots, dataLayoutProvider);
        this.messageProvider = messageProvider;
        inventoryTranslatedObjectCache = createCache();
    }

    private TranslatedObjectCache<Inventory> createCache() {
        return new TranslatedObjectCache<>(locale -> {


//Todo
            return null;
        });
    }

    @Override
    public Inventory getInventory(Locale locale) {
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

    }

    @Override
    protected void applyDataLayout() {
        synchronized (getDataLayoutFuture()) {
            for (var entry : inventoryTranslatedObjectCache.asMap().entrySet()) {
                getDataLayout().applyLayout(entry.getValue().getContents(), entry.getKey());
            }
        }
    }
}
