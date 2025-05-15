package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.theevilreaper.aves.i18n.TextData;
import net.theevilreaper.aves.inventory.holder.InventoryHolderImpl;
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
import java.util.Set;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@SuppressWarnings("java:S3252")
public class GlobalTranslatedInventoryBuilder extends BaseInventoryBuilderImpl {

    private final Map<Locale,CustomInventory> inventoryTranslatedObjectCache = new HashMap<>();
    private TextData titleData;

    /**
     * Creates a new instance from the class with the given {@link InventoryType}.
     * @param type the type to define the size of the inventory
     */
    public GlobalTranslatedInventoryBuilder(@NotNull InventoryType type) {
        super(type);
    }

    @Contract(value = "_ -> new", pure = true)
    private @NotNull CustomInventory create(Locale locale) {
        Component title = GlobalTranslator.render(titleData.createComponent(), locale);
        CustomInventory inventory = new CustomInventory(new InventoryHolderImpl(this), type, title);
        updateInventory(inventory, locale, true);
        return inventory;
    }

    @Override
    public void unregister() {
        this.unregister(this.getInventory().eventNode(), openListener, closeListener, clickListener);
        this.holder = null;

        if (this.inventoryTranslatedObjectCache.isEmpty()) return;

        for (Map.Entry<Locale, CustomInventory> entry : this.inventoryTranslatedObjectCache.entrySet()) {
            if (!entry.getValue().hasViewers()) continue;

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
        for (Inventory inventory : inventoryTranslatedObjectCache.values())
            if (!inventory.getViewers().isEmpty())
                return true;
        return false;
    }

    @Override
    protected void updateInventory() {
        for (Map.Entry<Locale, CustomInventory> entry : inventoryTranslatedObjectCache.entrySet()) {
            Locale locale = entry.getKey();
            updateInventory(entry.getValue(), locale, true);
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
