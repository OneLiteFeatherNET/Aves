package de.icevizion.aves.resourcepack;

import net.kyori.adventure.resource.ResourcePackInfo;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerResourcePackStatusEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The class can be used to manage the allocation of the resource pack to the players
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.1
 **/
public class ResourcePackHandler {

    private final ResourcePackInfo resourcePack;
    private final Set<UUID> resourcePackCache;
    private ResourcePackCondition condition;
    private Consumer<PlayerResourcePackStatusEvent> eventConsumer;

    /**
     * Creates a new instance from the {@link ResourcePackHandler} with the given parameters.
     * @param resourcePack An instance from a {@link ResourcePackInfo}
     */
    public ResourcePackHandler(@NotNull ResourcePackInfo resourcePack) {
        this.resourcePack = resourcePack;
        this.resourcePackCache = new HashSet<>();
        this.condition = null;
    }

    /**
     * Creates a new instance from the {@link ResourcePackHandler} with the given parameters.
     * @param resourcePack An instance from a {@link ResourcePackInfo}
     * @param condition The given {@link ResourcePackCondition}
     */
    public ResourcePackHandler(@NotNull ResourcePackInfo resourcePack, @NotNull ResourcePackCondition condition) {
        this.resourcePack = resourcePack;
        this.resourcePackCache = new HashSet<>();
        this.condition = condition;
        this.eventConsumer = handleResourcePackChange();
    }

    /**
     * Add a condition what happen when the {@link PlayerResourcePackStatusEvent} is called from the server.
     * The handling musst includes all parameters from {@link ResourcePackCondition}
     * @param condition The condition to set
     */
    public @NotNull ResourcePackHandler setCondition(ResourcePackCondition condition) {
        this.condition = condition;
        this.eventConsumer = handleResourcePackChange();
        return this;
    }

    /**
     * Add a pre-defined {@link ResourcePackCondition} to the handler.
     */
    public @NotNull ResourcePackHandler setDefaultCondition() {
        this.condition = new DefaultResourcePackCondition(resourcePackCache);
        this.eventConsumer = handleResourcePackChange();
        return this;
    }

    /**
     * Add some listener to handle the resource pack handling.
     */
    public @NotNull ResourcePackHandler withListener() {
        var eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addListener(PlayerResourcePackStatusEvent.class, eventConsumer);
        eventHandler.addListener(PlayerDisconnectEvent.class, event -> invalidateId(event.getPlayer().getUuid()));
        return this;
    }

    /**
     * Set the resource pack to a given player.
     * @param player The player who receives the pack
     * @return true when the player can receive the pack otherwise false
     */
    public boolean setPack(@NotNull Player player) {
        if (resourcePack == null || resourcePackCache.contains(player.getUuid())) return false;
        player.sendResourcePacks(resourcePack);
        resourcePackCache.add(player.getUuid());
        return true;
    }

    /**
     * Invalidates an uuid (player uuid) in the underlying cache.
     * @param uuid the uuid from the user
     */
    public void invalidateId(@NotNull UUID uuid) {
        this.resourcePackCache.remove(uuid);
        final Player player = MinecraftServer.getConnectionManager().getOnlinePlayerByUuid(uuid);
        if (player == null) return;
        player.removeResourcePacks(resourcePack.id());
    }

    /**
     * Creates the consumer for the {@link PlayerResourcePackStatusEvent}.
     * @return the created consumer
     */
    @Contract(pure = true)
    private @NotNull Consumer<PlayerResourcePackStatusEvent> handleResourcePackChange() {
        if (condition == null) {
            throw new IllegalStateException("Can't register the handler because the 'ResourcePackCondition' is null");
        }
        return event -> condition.handleStatus(event.getPlayer(), event.getStatus());
    }
}
