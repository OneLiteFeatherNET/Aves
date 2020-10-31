package de.icevizion.aves.item;

import org.bukkit.Material;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public final class MapBuilder extends ItemBuilder {

    private final MapMeta mapMeta;

    public MapBuilder() {
        super(Material.MAP);
        this.mapMeta = getItemMeta();
    }

    /**
     * Sets if this map is scaling or not
     * @param scaling True to scale
     * @return
     */

    public MapBuilder setScaling(boolean scaling) {
        mapMeta.setScaling(scaling);
        stack.setItemMeta(mapMeta);
        return this;
    }

    /**
     * Add a renderer to this map
     * @param mapRenderer The renderer to add
     * @return
     */

    public MapBuilder addRenderer(MapRenderer mapRenderer) {
        MapView mapView = (MapView) mapMeta;
        mapView.addRenderer(mapRenderer);
        stack.setItemMeta(mapMeta);
        return this;
    }

    public MapBuilder removeRenderer(MapRenderer renderer) {
        MapView mapView = (MapView) mapMeta;
        mapView.removeRenderer(renderer);
        stack.setItemMeta(mapMeta);
        return this;
    }

    public MapBuilder setMapView(MapViewBuilder viewBuilder) {
        mapMeta.setMapView(viewBuilder.getView());
        stack.setItemMeta(mapMeta);
        return this;
    }

    public MapBuilder setMapView(MapView mapView) {
        mapMeta.setMapView(mapView);
        stack.setItemMeta(itemMeta);
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