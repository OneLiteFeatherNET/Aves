package de.icevizion.aves.inventory.pageable;

import de.icevizion.aves.inventory.InventoryLayout;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class PageableInventory {

    private final InventoryLayout baseLayout;
    private final InventoryLayout dataLayout;
    private final PageableControls pageableControls;
    private int currentPage;

    public PageableInventory(InventoryLayout baseLayout, InventoryLayout dataLayout, PageableControls pageableControls) {
        this.baseLayout = baseLayout;
        this.dataLayout = dataLayout;
        this.pageableControls = pageableControls;

        this.currentPage = 0;
    }
}
