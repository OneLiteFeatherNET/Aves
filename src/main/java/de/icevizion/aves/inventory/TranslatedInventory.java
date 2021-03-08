package de.icevizion.aves.inventory;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.1.0
 * @since 1.0.6
 */

@Deprecated
public abstract class TranslatedInventory extends InventoryBuilder {

	/**
	 * Creates a new instance form the {@link TranslatedInventory}.
	 * @param title The title for the inventory
	 * @param rows The amount of rows for the inventory
	 */

	public TranslatedInventory(String title, InventoryRows rows) {
		super(title, rows);
	}

	/**
	 * Draws the content into the inventory.
	 */

	public abstract void draw();
}