package de.icevizion.aves.inventory;

import at.rxcki.strigiformes.MessageProvider;
import at.rxcki.strigiformes.TranslatedObjectCache;
import at.rxcki.strigiformes.text.TextData;
import de.icevizion.aves.inventory.holder.InventoryHolderImpl;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@SuppressWarnings("java:S3252")
public class GlobalTranslatedInventoryBuilder extends BaseInventoryBuilderImpl {

    private final MessageProvider messageProvider;
    private final TranslatedObjectCache<CustomInventory> inventoryTranslatedObjectCache;
    private TextData titleData;

    public GlobalTranslatedInventoryBuilder(@NotNull InventoryType type, MessageProvider messageProvider) {
        super(type);
        this.messageProvider = messageProvider;
        this.inventoryTranslatedObjectCache = createCache();
    }

    @Contract(value = " -> new", pure = true)
    private @NotNull TranslatedObjectCache<CustomInventory> createCache() {
        return new TranslatedObjectCache<>(locale -> {
            var title = Component.text(messageProvider.getTextProvider().format(titleData, locale));
            var inventory = new CustomInventory(new InventoryHolderImpl(this), type, title);
            inventory.addInventoryCondition(this.inventoryCondition);
            updateInventory(inventory, title, locale, messageProvider, true);
            return inventory;
        });
    }

    @Override
    public void unregister() {
        this.unregister(NODE, openListener, closeListener);
        this.holder = null;

        if (!this.inventoryTranslatedObjectCache.asMap().isEmpty()) return;

        for (var entry : this.inventoryTranslatedObjectCache.asMap().entrySet()) {
            if (entry.getValue().getViewers().isEmpty()) continue;

            for (Player viewer : entry.getValue().getViewers()) {
                viewer.closeInventory();
            }
        }
    }

    @Override
    public Inventory getInventory(@Nullable Locale locale) {
        if (!dataLayoutValid || !inventoryLayoutValid) {
            updateInventory();
        }

        return inventoryTranslatedObjectCache.get(locale);
    }

    @Override
    protected boolean isOpen() {
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
            updateViewer(entry.getValue());
        }
    }

    @Override
    protected void applyDataLayout() {
        synchronized (this) {
            if (getDataLayout() != null) {
                LOGGER.info("Applying data layout");
                for (var entry : inventoryTranslatedObjectCache.asMap().entrySet()) {
                    var contents = entry.getValue().getItemStacks();
                    getDataLayout().applyLayout(contents, entry.getKey(), messageProvider);
                    for (int i = 0; i < contents.length; i++) {
                        if (contents[i].material() == Material.AIR) continue;
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
