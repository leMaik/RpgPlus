package de.craften.plugins.rpgplus.components.commands;


import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A handler for nested commands.
 */
class CommandDispatcher implements CommandHandler {
    protected final Map<String, CommandHandler> handlers = new HashMap<>();
    private CommandHandler thisCommand;

    private CommandDispatcher(CommandHandler thisCommand) {
        this.thisCommand = thisCommand;
    }

    @Override
    public boolean onCommand(CommandSender sender, String command, List<String> args) {
        if (!args.isEmpty()) {
            CommandHandler commandHandler = handlers.get(args.get(0));
            if (commandHandler != null) {
                return commandHandler.onCommand(sender, args.get(0), args.subList(1, args.size()));
            }
        }

        if (thisCommand != null) {
            return thisCommand.onCommand(sender, command, args);
        } else {
            return false;
        }
    }

    public void registerCommand(String command, CommandHandler commandHandler) {
        CommandHandler oldHandler = handlers.get(command);
        if (oldHandler instanceof CommandDispatcher) {
            ((CommandDispatcher) oldHandler).thisCommand = commandHandler;
        } else {
            handlers.put(command, commandHandler);
        }
    }

    public void registerCommand(List<String> commandPath, CommandHandler commandHandler) {
        if (commandPath.size() == 1) {
            handlers.put(commandPath.get(0), commandHandler);
        } else {
            CommandHandler firstCommandHandler = handlers.get(commandPath.get(0));
            if (!(firstCommandHandler instanceof CommandDispatcher)) {
                firstCommandHandler = new CommandDispatcher(firstCommandHandler);
                handlers.put(commandPath.get(0), firstCommandHandler);
            }
            ((CommandDispatcher) firstCommandHandler).registerCommand(commandPath.subList(1, commandPath.size()), commandHandler);
        }
    }

    public static class Root extends CommandDispatcher {
        public Root() {
            super(null);
        }

        public boolean onCommand(CommandSender sender, String command, List<String> args) {
            CommandHandler handler = handlers.get(command);
            if (handler != null) {
                return handler.onCommand(sender, command, args);
            }
            return false;
        }
    }
}
