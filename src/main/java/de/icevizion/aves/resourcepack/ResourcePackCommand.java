package de.icevizion.aves.resourcepack;

import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

/**
 * //TODO: Finish command
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class ResourcePackCommand extends Command {

    public ResourcePackCommand(ResourcePackHandler resourcePackHandler) {
        super("rsp", "resource");

        var argumentType = ArgumentType.String("args");

        argumentType.setCallback((commandSender, e) -> commandSender.sendMessage("Wrong Input: " + e.getInput()));

        setDefaultExecutor((commandSender, commandContext) -> {
            commandSender.sendMessage("§cPlease type: /rsp <reload, load>");
        });

        addSyntax((commandSender, commandContext) -> {
            var input = (String) commandContext.get("args");
            if ("load".equals(input)) {
                if (resourcePackHandler.setPack((Player)commandSender)) {

                } else {

                }
            }
        });
    }
}
