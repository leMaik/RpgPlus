package de.craften.plugins.rpgplus.components.commands;


import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A handler for nested commands.
 */
class CommandDispatcher implements CommandHandler {
    private final Map<String, CommandHandler> handlers = new HashMap<>();

    @Override
    public final boolean onCommand(CommandSender sender, String command, List<String> args) {
        CommandHandler commandHandler = handlers.get(command.toLowerCase());
        if (commandHandler != null) {
            return commandHandler.onCommand(sender, args.get(0), args.subList(1, args.size()));
        } else {
            return false;
        }
    }

    void registerCommand(String command, CommandHandler commandHandler) {
        handlers.put(command, commandHandler);
    }

    void registerCommand(List<String> commandPath, CommandHandler commandHandler) {
        if (commandPath.size() == 1) {
            handlers.put(commandPath.get(0), commandHandler);
        } else {
            CommandHandler firstCommandHandler = handlers.get(commandPath.get(0));
            if (!(firstCommandHandler instanceof CommandDispatcher)) {
                firstCommandHandler = new CommandDispatcher();
                handlers.put(commandPath.get(0), firstCommandHandler);
            }
            ((CommandDispatcher) firstCommandHandler).registerCommand(commandPath.subList(1, commandPath.size()), commandHandler);
        }
    }
}
