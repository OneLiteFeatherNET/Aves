package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.TranslatedObjectCache;
import at.rxcki.strigiformes.text.TextData;
import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GlobalTranslatedInventoryBuilder extends InventoryBuilder {

    private final MessageProvider messageProvider;
    private final TranslatedObjectCache<Inventory> inventoryTranslatedObjectCache;

    private TextData titleData;

    public GlobalTranslatedInventoryBuilder(@NotNull InventoryType type, MessageProvider messageProvider) {
        super(type);
        this.messageProvider = messageProvider;
        this.inventoryTranslatedObjectCache = createCache();
    }

    private TranslatedObjectCache<Inventory> createCache() {
        return new TranslatedObjectCache<>(locale -> {
            var title = Component.text(messageProvider.getTextProvider().format(titleData, locale));
            var inventory = new Inventory(type, title);
            updateInventory(inventory, title, locale, messageProvider, true);
            return inventory;
        });
    }

    @Override
    public Inventory getInventory(@Nullable Locale locale) {
        if (!dataLayoutValid || !inventoryLayoutValid) {
            updateInventory();
        }

        return inventoryTranslatedObjectCache.get(locale);
    }

    @Override
    protected boolean isInventoryOpen() {
        if (inventoryTranslatedObjectCache.asMap().isEmpty()) return false;
        for (var inventory : inventoryTranslatedObjectCache.asMap().values())
            if (!inventory.getViewers().isEmpty())
                return true;
        return false;
    }

    @Override
    protected void updateInventory() {
        for (var entry : inventoryTranslatedObjectCache.asMap().entrySet()) {
            var locale = entry.getKey();
            updateInventory(entry.getValue(), Component.text(messageProvider.getTextProvider().format(titleData, locale)), locale, messageProvider, true);
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

    /**
     * Overwrites the current {@link TextData} with a new one.
     * @param titleData The {@link TextData} to set.
     */
    public void setTitleData(@NotNull TextData titleData) {
        this.titleData = titleData;
    }

    /**
     * Returns the {@link TextData} from the builder.
     * @return The underlying value
     */
    public TextData getTitleData() {
        return titleData;
    }
}
