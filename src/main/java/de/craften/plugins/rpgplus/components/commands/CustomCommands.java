package de.craften.plugins.rpgplus.components.commands;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * A component to register commands directly.
 */
public class CustomCommands extends PluginComponentBase {
    private CommandDispatcher.Root dispatcher;

    @Override
    protected void onActivated() {
        dispatcher = new CommandDispatcher.Root();
    }

    public void registerCommand(String command, CommandHandler handler) {
        if (getCommandMap().getCommand(command) == null) {
            getCommandMap().register(command, new DispatchedComand(command));
        }
        dispatcher.registerCommand(command, handler);
    }

    public void registerCommand(String[] commandPath, CommandHandler handler) {
        if (getCommandMap().getCommand(commandPath[0]) == null) {
            getCommandMap().register(commandPath[0], new DispatchedComand(commandPath[0]));
        }
        dispatcher.registerCommand(Arrays.asList(commandPath), handler);
    }

    /**
     * Remove all registered commands.
     */
    public void removeAll() {
        dispatcher.removeAll();
    }

    private class DispatchedComand extends Command {
        protected DispatchedComand(String name) {
            super(name);
        }

        @Override
        public boolean execute(CommandSender sender, String command, String[] args) {
            return dispatcher.onCommand(sender, command, Arrays.asList(args));
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