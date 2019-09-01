package de.cosmiqglow.aves.item;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class MapBuilder extends ItemBuilder {

    public MapBuilder(final MapType type) {
        super(type.getMaterial());
    }

    public MapBuilder setColor(Color color) {
        MapMeta meta = getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return this;
    }

    public MapBuilder setLocationName(String name) {
        MapMeta meta = getItemMeta();
        meta.setLocationName(name);
        stack.setItemMeta(meta);
        return this;
    }

    public MapBuilder setScaling(boolean scaling) {
        MapMeta meta = getItemMeta();
        meta.setScaling(scaling);
        stack.setItemMeta(meta);
        return this;
    }

    public MapBuilder addRenderer(MapRenderer mapRenderer) {
        MapMeta meta = getItemMeta();
        MapView mapView = (MapView) meta;
        mapView.addRenderer(mapRenderer);
        stack.setItemMeta(meta);
        return this;
    }

    public MapBuilder setUnlimitedScaling() {
        MapMeta meta = getItemMeta();
        MapView mapView = (MapView) meta;
        mapView.setUnlimitedTracking(true);
        stack.setItemMeta(meta);
        return this;
    }

    @Override
    protected MapMeta getItemMeta() {
        return (MapMeta) super.getItemMeta();
    }

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