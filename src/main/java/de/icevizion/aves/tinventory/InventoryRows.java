package de.icevizion.aves.tinventory;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public enum InventoryRows {

	ONE(9),
	TWO(18),
	THREE(27),
	FOUR(36),
	FIVE(45),
	SIX(54);

	private static final InventoryRows[] VALUES = values();

	private final int size;

	InventoryRows(int size) {
		this.size = size;
	}

	public int getSize() {
		return size;
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
