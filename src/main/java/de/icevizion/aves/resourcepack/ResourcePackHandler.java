package de.icevizion.aves.resourcepack;

import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.event.player.PlayerResourcePackStatusEvent;
import net.minestom.server.resourcepack.ResourcePack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ResourcePackHandler {

    private final ResourcePack resourcePack;
    private ResourcePackCondition condition;
    private Consumer<PlayerResourcePackStatusEvent> eventConsumer;

    public ResourcePackHandler(ResourcePack resourcePack, ResourcePackCondition condition) {
        this.resourcePack = resourcePack;
        this.condition = condition;
        this.eventConsumer = handleResourcePackChange();
    }

    public ResourcePackHandler(ResourcePack resourcePack) {
        this.resourcePack = resourcePack;
        this.condition = null;
    }

    public ResourcePackHandler setCondition(ResourcePackCondition condition) {
        this.condition = condition;
        return this;
    }

    public void registerListener() {
        MinecraftServer.getGlobalEventHandler().addListener(PlayerResourcePackStatusEvent.class, eventConsumer);
    }

    public void set(@NotNull Player player) {
        if (resourcePack == null) return;
        player.setResourcePack(resourcePack);
    }

    private Consumer<PlayerResourcePackStatusEvent> handleResourcePackChange() {
        return event -> condition.handleStatus(event.getPlayer(), event.getStatus());
    }
}
