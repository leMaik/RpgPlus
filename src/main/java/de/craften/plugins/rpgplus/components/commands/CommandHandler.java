package de.craften.plugins.rpgplus.components.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A handler for a command.
 */
public interface CommandHandler {
    boolean onCommand(CommandSender sender, String command, List<String> args);
}
