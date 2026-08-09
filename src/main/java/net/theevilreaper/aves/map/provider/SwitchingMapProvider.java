package net.theevilreaper.aves.map.provider;

import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.InstanceContainer;
import net.theevilreaper.aves.file.FileHandler;
import net.theevilreaper.aves.map.BaseMap;
import net.theevilreaper.aves.map.MapEntry;
import net.theevilreaper.aves.util.functional.PathFilter;
import org.jetbrains.annotations.Nullable;

/**
 * Extends {@link AbstractMapProvider} with the ability to switch the active map/instance to a new one while
 * keeping the previous instance registered until it can safely be released.
 * <p>
 * Minestom refuses to unregister an instance that still holds online players, so {@link #switchTo} only moves
 * the active references; callers must move players out of the previous instance before calling
 * {@link #releasePreviousInstance()}.
 * <p>
 * Use this as the base for providers with more than one map "slot" (e.g. a lobby that switches into a game
 * map). Providers where the active map never changes should extend {@link AbstractMapProvider} directly.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.17.0
 */
public abstract class SwitchingMapProvider extends AbstractMapProvider {

    protected @Nullable InstanceContainer previousInstance;

    protected SwitchingMapProvider(FileHandler fileHandler, PathFilter<MapEntry> mapFilter) {
        super(fileHandler, mapFilter);
    }

    /**
     * Switches the active map/instance to the given ones. The previously active instance is kept registered
     * as {@link #previousInstance} until {@link #releasePreviousInstance()} is called.
     *
     * @param instance the instance to become active
     * @param map      the map to become active
     */
    protected void switchTo(InstanceContainer instance, BaseMap map) {
        this.previousInstance = this.activeInstance;
        this.activeInstance = instance;
        this.activeMap = map;
    }

    /**
     * Unregisters the instance that was active before the last {@link #switchTo} call.
     * <p>
     * Calling this more than once, or without a previous {@link #switchTo} call, does nothing.
     */
    public void releasePreviousInstance() {
        if (this.previousInstance == null) return;
        MinecraftServer.getInstanceManager().unregisterInstance(this.previousInstance);
        this.previousInstance = null;
    }
}
