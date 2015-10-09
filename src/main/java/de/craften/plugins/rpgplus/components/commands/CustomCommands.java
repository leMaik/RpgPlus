package de.craften.plugins.rpgplus.components.commands;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * A component to register commands directly.
 */
public class CustomCommands extends PluginComponentBase {
    private Map<String, CommandHandler> handlers;

    @Override
    protected void onActivated() {
        handlers = new HashMap<>();
    }

    public void registerCommand(String command, CommandHandler handler) {
        getCommandMap().register(command, new DispatchedComand(command));
        handlers.put(command, handler);
    }

    public void registerCommand(String[] commandPath, CommandHandler handler) {
        getCommandMap().register(commandPath[0], new DispatchedComand(commandPath[0]));
        CommandHandler firstCommandHandler = handlers.get(commandPath[0]);
        if (!(firstCommandHandler instanceof CommandDispatcher)) {
            firstCommandHandler = new CommandDispatcher();
            handlers.put(commandPath[0], firstCommandHandler);
        }
        ((CommandDispatcher) firstCommandHandler).registerCommand(Arrays.asList(commandPath).subList(1, commandPath.length), handler);
    }

    private class DispatchedComand extends Command {
        protected DispatchedComand(String name) {
            super(name);
        }

        @Override
        public boolean execute(CommandSender sender, String command, String[] args) {
            CommandHandler handler = handlers.get(command);
            if (handler != null) {
                return handler.onCommand(sender, command, Arrays.asList(args));
            }
            return false;
        }
    }

    private static CommandMap getCommandMap() {
        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            return (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new UnsupportedOperationException("Command could not be registered", e);
        }
    }
}