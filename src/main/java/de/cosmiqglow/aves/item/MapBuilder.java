package de.cosmiqglow.aves.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public final class MapBuilder extends ItemBuilder {

    public MapBuilder(final MapType type) {
        super(type.getMaterial());
    }

    /**
     * Sets the map color
     * @param color The color to set
     * @return
     */

    public MapBuilder setColor(Color color) {
        MapMeta meta = getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the location name
     * @param name The name to set
     * @return
     */

    public MapBuilder setLocationName(String name) {
        MapMeta meta = getItemMeta();
        meta.setLocationName(name);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets if this map is scaling or not
     * @param scaling True to scale
     * @return
     */

    public MapBuilder setScaling(boolean scaling) {
        MapMeta meta = getItemMeta();
        meta.setScaling(scaling);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Add a renderer to this map
     * @param mapRenderer The renderer to add
     * @return
     */

    public MapBuilder addRenderer(MapRenderer mapRenderer) {
        MapMeta meta = getItemMeta();
        MapView mapView = (MapView) meta;
        mapView.addRenderer(mapRenderer);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Whether the map will show a smaller position cursor (true),
     * or no position cursor (false) when cursor is outside of map's range.
     * @return tracking state
     */

    public MapBuilder setUnlimitedScaling(boolean value) {
        MapMeta meta = getItemMeta();
        MapView mapView = (MapView) meta;
        mapView.setUnlimitedTracking(value);
        stack.setItemMeta(meta);
        return this;
    }

    /**
     * Override the default method to return another meta
     * @return The {@link MapMeta}
     */

    @Override
    protected MapMeta getItemMeta() {
        return (MapMeta) super.getItemMeta();
    }

    /**
     * The enum is a wrapper for the existing map items in spigot
     */

    public enum MapType {

        MAP(Material.MAP),
        FILLED_MAP(Material.FILLED_MAP);

        final Material material;

        MapType(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}