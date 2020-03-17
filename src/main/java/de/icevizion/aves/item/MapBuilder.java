package de.icevizion.aves.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public final class MapBuilder extends ItemBuilder {

    public MapBuilder() {
        super(Material.MAP);
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
     * Override the default method to return another meta
     * @return The {@link MapMeta}
     */

    @Override
    protected MapMeta getItemMeta() {
        return (MapMeta) super.getItemMeta();
    }
}