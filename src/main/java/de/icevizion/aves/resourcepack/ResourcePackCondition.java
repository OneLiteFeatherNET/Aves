package de.icevizion.aves.resourcepack;

import net.minestom.server.entity.Player;
import net.minestom.server.resourcepack.ResourcePackStatus;
import org.jetbrains.annotations.NotNull;

/**
 * The interface allow a developer to set a custom condition to manage the {@link ResourcePackStatus}
 * from a {@link net.minestom.server.event.player.PlayerResourcePackStatusEvent}.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@FunctionalInterface
public interface ResourcePackCondition {

    /**
     * Handles what happen when the player accepts or declined the {@link net.minestom.server.resourcepack.ResourcePack}.
     * @param player The player who deals with the {@link net.minestom.server.resourcepack.ResourcePack}
     * @param resourcePackStatus The {@link ResourcePackStatus} from the event
     */
    void handleStatus(@NotNull Player player, @NotNull ResourcePackStatus resourcePackStatus);
}
