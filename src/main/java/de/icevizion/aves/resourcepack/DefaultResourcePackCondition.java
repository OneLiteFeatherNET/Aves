package de.icevizion.aves.resourcepack;

import net.minestom.server.entity.Player;
import net.minestom.server.resourcepack.ResourcePack;
import net.minestom.server.resourcepack.ResourcePackStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Generic implementation to handle the status when the player accepts or decline the resource pack request.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class DefaultResourcePackCondition implements ResourcePackCondition {

    private static final String ERROR_DOWNLOAD_MESSAGE =
            "§cFailed to download resource pack. Please report the issue and try /rsp load in a few minutes";
    private static final String KICK_MESSAGE =
            "§cYou must accept the resource pack to play on the server";
    private final Set<UUID> cache;

    /**
     * Creates a new instance of the condition class
     * @param cache contains all player with his id.
     */
    public DefaultResourcePackCondition(@NotNull Set<UUID> cache) {
        this.cache = cache;
    }

    /**
     * Handles the status change when the player declined the resource pack or if the download fails.
     * @param player The player who deals with the {@link ResourcePack}
     * @param resourcePackStatus The {@link ResourcePackStatus} from the event
     */
    @Override
    public void handleStatus(@NotNull Player player, @NotNull ResourcePackStatus resourcePackStatus) {
        switch (resourcePackStatus) {
            case DECLINED -> {
                player.kick(KICK_MESSAGE);
                cache.remove(player.getUuid());
            }
            case FAILED_DOWNLOAD -> {
                player.sendMessage(ERROR_DOWNLOAD_MESSAGE);
                cache.remove(player.getUuid());
            }
        }
    }
}
