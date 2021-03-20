package de.icevizion.aves.inventory;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public enum InventoryRows {

	ONE(9, 1),
	TWO(18, 2),
	THREE(27, 3),
	FOUR(36, 4),
	FIVE(45, 5),
	SIX(54, 6);

	private static final InventoryRows[] VALUES = values();

	private final int size;

	private final int rowCount;

	InventoryRows(int size, int rowCount) {
		this.size = size;
		this.rowCount = rowCount;
	}

	public int getSize() {
		return size;
	}

	public int getRowCount() {
		return rowCount;
	}

	public static InventoryRows getRows(int size) {
		InventoryRows requiredRows = ONE;
		for (InventoryRows rows : VALUES) {
			if (rows.getSize() >= size) {
				requiredRows = rows;
				break;
			}
		}

		return requiredRows;
	}

	public static InventoryRows getRows(InventoryRows freeRows, int maxItemsPerRow, int items) {
		return getRows(freeRows.getSize() + 9 * (int) Math.ceil((double) items / maxItemsPerRow));
	}
}
