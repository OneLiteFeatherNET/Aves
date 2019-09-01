package de.cosmiqglow.aves.item.util;

public enum CursorType {

    WHITE_POINTER(0),
    GREEN_POINTER(1),
    RED_POINTER(2),
    BLUE_POINTER(3),
    WHITE_CLOVER(4),
    RED_BOLD_POINTER(5),
    WHITE_DOT(6),
    LIGHT_BLUE_SQUARE(7);

    private final int id;

    CursorType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}
