package de.icevizion.aves.tinventory;

import org.bukkit.inventory.Inventory;

import java.util.Locale;
import java.util.UUID;
import java.util.function.Function;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class PersonalInventoryBuilder extends InventoryBuilder{

    private final UUID uuid;
    private Inventory inventory;

    public PersonalInventoryBuilder(InventoryRows rows, Function<InventoryLayout, InventoryLayout> dataLayoutProvider, UUID uuid) {
        super(rows, dataLayoutProvider);
        this.uuid = uuid;
    }

    public PersonalInventoryBuilder(int slots, Function<InventoryLayout, InventoryLayout> dataLayoutProvider, UUID uuid) {
        super(slots, dataLayoutProvider);
        this.uuid = uuid;
    }

    @Override
    public Inventory getInventory(Locale locale) {
        return null;
    }


    @Override
    protected boolean isInventoryOpened() {
        return inventory != null && !inventory.getViewers().isEmpty();
    }

    @Override
    protected void updateInventory() {

    }

    @Override
    protected void applyDataLayout() {

    }
}
