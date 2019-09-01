package de.cosmiqglow.aves.item.util;

import org.bukkit.map.MapFont;

public class MapText {

    private int x;
    private int y;
    private MapFont font;
    private String string;

    public MapText(int x, int y, MapFont font, String string) {
        this.x = x;
        this.y = y;
        this.font = font;
        this.string = string;
    }

    public void setFont(MapFont font) {
        this.font = font;
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public MapFont getFont() {
        return font;
    }

    public String getString() {
        return string;
    }
}
