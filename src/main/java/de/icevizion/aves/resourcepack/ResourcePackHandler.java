package de.icevizion.aves.resourcepack;

import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerDisconnectEvent;
import net.minestom.server.event.player.PlayerResourcePackStatusEvent;
import net.minestom.server.resourcepack.ResourcePack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The class can be used to manage the allocation of the resource pack to the players
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.1
 **/
public class ResourcePackHandler {

    private final ResourcePack resourcePack;
    private final HashSet<UUID> resourcePackCache;

    private ResourcePackCondition condition;
    private Consumer<PlayerResourcePackStatusEvent> eventConsumer;
    private ResourcePackCommand command;

    /**
     * Creates a new instance from the {@link ResourcePackHandler} with the given parameters.
     * @param resourcePack An instance from a {@link ResourcePack}
     */
    public ResourcePackHandler(@NotNull ResourcePack resourcePack) {
        this.resourcePack = resourcePack;
        this.resourcePackCache = new HashSet<>();
        this.condition = null;
    }

    /**
     * Creates a new instance from the {@link ResourcePackHandler} with the given parameters.
     * @param resourcePack An instance from a {@link ResourcePack}
     * @param condition The given {@link ResourcePackCondition}
     */
    public ResourcePackHandler(@NotNull ResourcePack resourcePack, @NotNull ResourcePackCondition condition) {
        this.resourcePack = resourcePack;
        this.resourcePackCache = new HashSet<>();
        this.condition = condition;
        this.eventConsumer = handleResourcePackChange();
    }

    /**
     * Creates a new instance from the {@link ResourcePackHandler} with the given parameters.
     * @param url The url to the pack
     * @param hash The hash value
     * @param force If the pack should be forced or not
     */
    public ResourcePackHandler(@NotNull String url, @Nullable String hash, boolean force) {
        this.resourcePack = force ? ResourcePack.forced(url, hash) : ResourcePack.optional(url, hash);
        this.resourcePackCache = new HashSet<>();
        this.condition = null;
    }

    /**
     * Creates a new instance from the {@link ResourcePackHandler} with the given parameters.
     * @param url The url to the pack
     * @param hash The hash value
     * @param forceMessage The message which the player see when he accepts the resource pack
     */
    public ResourcePackHandler(@NotNull String url, @Nullable String hash, @Nullable Component forceMessage) {
        this.resourcePack = ResourcePack.forced(url, hash, forceMessage);
        this.resourcePackCache = new HashSet<>();
        this.condition = null;
    }

    /**
     * Add a condition what happen when the {@link PlayerResourcePackStatusEvent} is called from the server.
     * The handling musst includes all parameters from {@link ResourcePackCondition}
     * @param condition The condition to set
     */
    public ResourcePackHandler setCondition(ResourcePackCondition condition) {
        this.condition = condition;
        this.eventConsumer = handleResourcePackChange();
        return this;
    }

    /**
     * Add a pre defined {@link ResourcePackCondition} to the handler.
     */
    public ResourcePackHandler setDefaultCondition() {
        this.condition = new DefaultResourcePackCondition(resourcePackCache);
        this.eventConsumer = handleResourcePackChange();
        return this;
    }

    /**
     * Add some listener to handle the resource pack handling.
     */
    public ResourcePackHandler withListener() {
        var eventHandler = MinecraftServer.getGlobalEventHandler();
        eventHandler.addListener(PlayerResourcePackStatusEvent.class, eventConsumer);
        eventHandler.addListener(PlayerDisconnectEvent.class, event -> invalidateId(event.getPlayer().getUuid()));
        return this;
    }

    /**
     * Register a command which allow that players can load or reload the resource pack
     */
    public ResourcePackHandler withCommand() {
        if (command == null) {
            command = new ResourcePackCommand(this);
            MinecraftServer.getCommandManager().register(new ResourcePackCommand(this));
        }
        return this;
    }

    /**
     * Set the resource pack to a given player.
     * @param player The player who receives the pack
     * @return true when the player can receive the pack otherwise false
     */
    public boolean setPack(@NotNull Player player) {
        if (resourcePack == null || resourcePackCache.contains(player.getUuid())) return false;
        player.setResourcePack(resourcePack);
        resourcePackCache.add(player.getUuid());
        return true;
    }

    /**
     * Unregisters the command.
     */
    public void unregisterCommand() {
        if (command == null) return;
        MinecraftServer.getCommandManager().unregister(command);
    }

    /**
     * Invalidates an uuid (player uuid) in the underlying cache.
     * @param uuid the uuid from the user
     */
    public void invalidateId(UUID uuid) {
        this.resourcePackCache.remove(uuid);
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
