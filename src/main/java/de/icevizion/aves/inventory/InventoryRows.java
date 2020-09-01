package de.icevizion.aves.inventory;

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
}
