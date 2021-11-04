package de.icevizion.aves.resourcepack;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since
 **/

public class ResourcePackCommand extends Command {

    public ResourcePackCommand() {
        super("rsp", "resourcepack");


        var argumentType = ArgumentType.String("args");

        argumentType.setCallback((commandSender, e) -> commandSender.sendMessage("Wrong Input: " + e.getInput()));

        setDefaultExecutor((commandSender, commandContext) -> {
            commandSender.sendMessage("§cPlease type: /rsp <reload, load>");
        });

        addSyntax((commandSender, commandContext) -> {
            var input = (String) commandContext.get("args");

            var player = (Player) commandSender;

            switch (input) {
                case "reload" -> player.sendMessage("Reloading pack");
                case "load" -> player.sendMessage("load");
            }
        });
    }

    private void reloadPack(@NotNull Player player) {

    }

    private void loadPack(@NotNull Player player) {

    }
}
