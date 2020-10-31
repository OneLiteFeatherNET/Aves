package de.icevizion.aves.item;

import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

public class MapViewBuilder {

    private final MapView mapView;

    public MapViewBuilder(ItemStack stack) {
        this.mapView = ((MapMeta) stack.getItemMeta()).getMapView();
    }

    public MapViewBuilder(MapView mapView) {
        this.mapView = mapView;
    }

    public MapViewBuilder setCenterX(int x) {
        mapView.setCenterX(x);
        return this;
    }

    public MapViewBuilder setCenterZ(int z) {
        mapView.setCenterX(z);
        return this;
    }

    public MapViewBuilder setLocked(boolean locked) {
        mapView.setLocked(locked);
        return this;
    }

    public MapViewBuilder setScale(MapView.Scale scale) {
        mapView.setScale(scale);
        return this;
    }

    public MapViewBuilder setPositionTacking(boolean positionTracking) {
        mapView.setTrackingPosition(positionTracking);
        return this;
    }

    public MapViewBuilder setUnlimitedTracking(boolean unlimited) {
        mapView.setUnlimitedTracking(unlimited);
        return this;
    }

    public MapViewBuilder setWorld(World world) {
        mapView.setWorld(world);
        return this;
    }

    public MapView getView() {
        return this.mapView;
    }
}
