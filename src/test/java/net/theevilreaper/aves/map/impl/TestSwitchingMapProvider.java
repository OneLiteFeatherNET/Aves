package net.theevilreaper.aves.map.impl;

import net.minestom.server.instance.InstanceContainer;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.map.provider.SwitchingMapProvider;
import net.theevilreaper.aves.util.functional.PathFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public final class TestSwitchingMapProvider extends SwitchingMapProvider {

    public TestSwitchingMapProvider(@NotNull Path path, @NotNull FileHandler fileHandler, @NotNull PathFilter<MapEntry> mapFilter) {
        super(fileHandler, mapFilter);
        this.loadMapEntries(path);
    }

    public void doSwitch(InstanceContainer instance, BaseMap map) {
        this.switchTo(instance, map);
    }

    public @Nullable InstanceContainer currentPreviousInstance() {
        return this.previousInstance;
    }

    public @Nullable InstanceContainer currentActiveInstance() {
        return this.activeInstance;
    }

    public @Nullable BaseMap currentActiveMap() {
        return this.activeMap;
    }

    @Override
    public void saveMap(@NotNull Path path, @NotNull BaseMap baseMap) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
