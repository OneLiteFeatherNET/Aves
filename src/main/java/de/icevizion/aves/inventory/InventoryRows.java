package de.icevizion.aves.inventory;

import net.minestom.server.inventory.InventoryType;

/**
 * @author Nico (JumpingPxl) Middendorf
 */

public enum InventoryRows {

	ONE(9, 1, InventoryType.CHEST_1_ROW),
	TWO(18, 2, InventoryType.CHEST_2_ROW),
	THREE(27, 3, InventoryType.CHEST_3_ROW),
	FOUR(36, 4, InventoryType.CHEST_4_ROW),
	FIVE(45, 5, InventoryType.CHEST_5_ROW),
	SIX(54, 6, InventoryType.CHEST_6_ROW);

	private static final InventoryRows[] VALUES = values();

	private final int size;
	private final int rowCount;
	private final InventoryType type;

	InventoryRows(int size, int rowCount, InventoryType type) {
		this.size = size;
		this.rowCount = rowCount;
		this.type = type;
	}

	public int getSize() {
		return size;
	}

	public int getRowCount() {
		return rowCount;
	}

	public InventoryType getType() {
		return type;
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
