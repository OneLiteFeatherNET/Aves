package de.icevizion.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class GlobalInventoryBuilder extends InventoryBuilder {

    private final Component titleComponent;
    private Inventory inventory;

    public GlobalInventoryBuilder(@NotNull String title, @NotNull InventoryType type) {
        super(type);
        this.titleComponent = Component.text(title);
    }

    public GlobalInventoryBuilder(@NotNull Component title, @NotNull InventoryType type) {
        super(type);
        this.titleComponent = title;
    }

    @Override
    public Inventory getInventory(Locale ignored) {
        if (dataLayoutValid && inventoryLayoutValid && inventory != null)
            return inventory;
        updateInventory();
        return inventory;
    }

    @Override
    protected boolean isInventoryOpen() {
        return !inventory.getViewers().isEmpty();
    }

    @Override
    protected void updateInventory() {
        boolean applyLayout = !inventoryLayoutValid;
        if (inventory == null) {
            this.inventory = new Inventory(getType(), titleComponent);
            applyLayout = true;
        }

        updateInventory(inventory, titleComponent, null, null, applyLayout);
        inventory.update();
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
                this.inventory.update();
            }
        }
    }
}
