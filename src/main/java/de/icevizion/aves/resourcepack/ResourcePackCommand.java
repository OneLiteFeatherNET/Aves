package de.icevizion.aves.resourcepack;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/

public class ResourcePackCommand extends Command {

    private final ResourcePackHandler resourcePackHandler;

    public ResourcePackCommand(ResourcePackHandler resourcePackHandler) {
        super("rsp", "resource");
        this.resourcePackHandler = resourcePackHandler;


        var argumentType = ArgumentType.String("args");

        argumentType.setCallback((commandSender, e) -> commandSender.sendMessage("Wrong Input: " + e.getInput()));

        setDefaultExecutor((commandSender, commandContext) -> {
            commandSender.sendMessage("§cPlease type: /rsp <reload, load>");
        });

        addSyntax((commandSender, commandContext) -> {
            var input = (String) commandContext.get("args");

            var player = (Player) commandSender;

            switch (input) {
                case "load" -> loadPack(player);
                case "reload" -> player.sendMessage("load");
            }
        });
    }

    private void reloadPack(@NotNull Player player) {

    }

    private void loadPack(@NotNull Player player) {
        if (!resourcePackHandler.setPack(player)) {
            player.sendMessage("Unable to set pack");
            return;
        }

        player.sendMessage("RessourcePack gesetzt");
    }
}
