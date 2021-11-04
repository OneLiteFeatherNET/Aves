package de.icevizion.aves.resourcepack;

import net.minestom.server.entity.Player;
import net.minestom.server.resourcepack.ResourcePackStatus;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public record DefaultResourcePackCondition(HashSet<Integer> cache) implements ResourcePackCondition {

    @Override
    public void handleStatus(@NotNull Player player, @NotNull ResourcePackStatus resourcePackStatus) {
        switch (resourcePackStatus) {
            case DECLINED -> {
                player.kick("§cYou must accept the resource pack to play on the server");
                cache.remove(player.getEntityId());
            }
            case FAILED_DOWNLOAD -> {
                player.sendMessage("§cFailed to download the resource pack. Please report the issue and try /rsp load in a few minutes");
                cache.remove(player.getEntityId());
            }
        }
    }
}
