package de.icevizion.aves.resourcepack;

import net.kyori.adventure.resource.ResourcePackInfo;
import net.kyori.adventure.resource.ResourcePackStatus;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

/**
 * Generic implementation to handle the status when the player accepts or decline the resource pack request.
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
@ApiStatus.NonExtendable
public final class DefaultResourcePackCondition implements ResourcePackCondition {

    private static final Component ERROR_DOWNLOAD_MESSAGE =
            Component.text("§cFailed to download resource pack. Please report the issue and try /rsp load in a few minutes", NamedTextColor.RED);
    private static final Component KICK_MESSAGE = Component.text("You must accept the resource pack to play on the serve", NamedTextColor.RED);
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
     * @param player The player who deals with the {@link ResourcePackInfo}
     * @param resourcePackStatus The {@link ResourcePackStatus} from the event
     */
    @Override
    public void handleStatus(@NotNull Player player, @NotNull ResourcePackStatus resourcePackStatus) {
        if (resourcePackStatus == ResourcePackStatus.DECLINED) {
            this.handleDeclined(player);
            return;
        }

        if (resourcePackStatus == ResourcePackStatus.FAILED_DOWNLOAD) {
            this.handleDownloadFail(player);
        }

    }

    /**
     * Handles what happen if a player declined the {@link ResourcePackInfo}.
     * @param player the player who is involved
     */
    private void handleDeclined(@NotNull Player player) {
        player.kick(KICK_MESSAGE);
        cache.remove(player.getUuid());
    }

    /**
     * Handles what happen the download of the {@link ResourcePackInfo} failed.
     * @param player the player who is involved
     */
    private void handleDownloadFail(@NotNull Player player) {
        player.sendMessage(ERROR_DOWNLOAD_MESSAGE);
        cache.remove(player.getUuid());
    }
}
