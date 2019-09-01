package de.cosmiqglow.aves.item.util;

public enum CursorDirection {

    SOUTH(0),
    SOUTH_WEST_SOUTH(1),
    SOUTH_WEST(2),
    SOUTH_WEST_WEST(3),
    WEST(4),
    NORTH_WEST_WEST(5),
    NORTH_WEST(6),
    NORTH_WEST_NORTH(7),
    NORTH(8),
    NORTH_EAST_NORTH(9),
    NORTH_EAST(10),
    NORTH_EAST_EAST(11),
    EAST(12),
    SOUTH_EAST_EAST(13),
    SOUNT_EAST(14),
    SOUTH_EAST_SOUTH(15);

    private final int id;

    CursorDirection(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
