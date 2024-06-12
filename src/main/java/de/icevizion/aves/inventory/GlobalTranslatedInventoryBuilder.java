package de.icevizion.aves.inventory;

import de.icevizion.aves.i18n.TextData;
import de.icevizion.aves.inventory.holder.InventoryHolderImpl;
import net.kyori.adventure.translation.GlobalTranslator;
import net.minestom.server.entity.Player;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Patrick Zdarsky / Rxcki
 */

public class GlobalTranslatedInventoryBuilder extends BaseInventoryBuilderImpl {

    private final Map<Locale,CustomInventory> inventoryTranslatedObjectCache = new HashMap<>();
    private TextData titleData;

    public GlobalTranslatedInventoryBuilder(@NotNull InventoryType type) {
        super(type);
    }

    @Contract(value = " -> new", pure = true)
    private @NotNull CustomInventory create(Locale locale) {
        var title = GlobalTranslator.render(titleData.createComponent(), locale);
        var inventory = new CustomInventory(new InventoryHolderImpl(this), type, title);
        inventory.addInventoryCondition(this.inventoryCondition);
        updateInventory(inventory, title, locale, true);
        return inventory;
    }

    @Override
    public void unregister() {
        this.unregister(NODE, openListener, closeListener);
        this.holder = null;

        if (!this.inventoryTranslatedObjectCache.isEmpty()) return;

        for (var entry : this.inventoryTranslatedObjectCache.entrySet()) {
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

        return inventoryTranslatedObjectCache.computeIfAbsent(locale, this::create);
    }

    @Override
    protected boolean isOpen() {
        if (inventoryTranslatedObjectCache.isEmpty()) return false;
        for (var inventory : inventoryTranslatedObjectCache.values())
            if (!inventory.getViewers().isEmpty())
                return true;
        return false;
    }

    @Override
    protected void updateInventory() {
        for (var entry : inventoryTranslatedObjectCache.entrySet()) {
            var locale = entry.getKey();
            updateInventory(entry.getValue(), GlobalTranslator.render(titleData.createComponent(), locale), locale, true);
            updateViewer(entry.getValue());
        }
    }

    @Override
    protected void applyDataLayout() {
        synchronized (this) {
            if (getDataLayout() != null) {
                LOGGER.info("Applying data layout");
                for (var entry : inventoryTranslatedObjectCache.entrySet()) {
                    var contents = entry.getValue().getItemStacks();
                    getDataLayout().applyLayout(contents, entry.getKey());
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

