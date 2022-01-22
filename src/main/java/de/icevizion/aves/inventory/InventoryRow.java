package de.icevizion.aves.inventory;

import net.minestom.server.inventory.InventoryType;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @version 1.0.0
 * @since 1.0.12
 */
public enum InventoryRow {

	ONE(9, 1, InventoryType.CHEST_1_ROW),
	TWO(18, 2, InventoryType.CHEST_2_ROW),
	THREE(27, 3, InventoryType.CHEST_3_ROW),
	FOUR(36, 4, InventoryType.CHEST_4_ROW),
	FIVE(45, 5, InventoryType.CHEST_5_ROW),
	SIX(54, 6, InventoryType.CHEST_6_ROW);

	private static final InventoryRow[] VALUES = values();

	private final int size;
	private final int rowCount;
	private final InventoryType type;

	/**
	 * Creates a new {@link InventoryRow} with the given value.
	 * @param size The size of the row
	 * @param rowCount The amount of row
	 * @param type The type for the row
	 */
	InventoryRow(int size, int rowCount, InventoryType type) {
		this.size = size;
		this.rowCount = rowCount;
		this.type = type;
	}

	/**
	 * Returns the size of the {@link InventoryRow}.
	 * @return the given size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Returns the amount of rows from the {@link InventoryRow}.
	 * @return the given amount of rows
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Returns the type of the inventory from the {@link InventoryRow}.
	 * @return the given type
	 */
	public InventoryType getType() {
		return type;
	}

	/**
	 * Determines the InventoryRow element based on the specified size.
	 * @param size The size to determine the row
	 * @return The determined row.
	 */
	public static InventoryRow getRows(int size) {
		InventoryRow requiredRows = ONE;
		for (InventoryRow rows : VALUES) {
			if (rows.getSize() >= size) {
				requiredRows = rows;
				break;
			}
		}
		return requiredRows;
	}

	public static InventoryRow getRows(InventoryRow freeRows, int maxItemsPerRow, int items) {
		return getRows(freeRows.getSize() + 9 * (int) Math.ceil((double) items / maxItemsPerRow));
	}
}
