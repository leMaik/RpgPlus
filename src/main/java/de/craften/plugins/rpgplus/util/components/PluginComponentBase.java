package de.craften.plugins.rpgplus.util.components;

import org.bukkit.Server;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.Configuration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.logging.Logger;

/**
 * A base for plugin components that makes many things easier.
 * <p/>
 * If the extending class implements {@link org.bukkit.event.Listener}, it is automatically registered.
 */
public abstract class PluginComponentBase implements PluginComponent {
    private JavaPlugin plugin;

    @Override
    public final void activateFor(JavaPlugin craftenServerPlugin) {
        plugin = craftenServerPlugin;

        if (this instanceof Listener) {
            registerEvents((Listener) this);
        }

        onActivated();
    }

    /**
     * Registers the given listener.
     *
     * @param listener listener to register
     */
    public final void registerEvents(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Gets a logger for this component.
     *
     * @return logger for this component
     */
    public final Logger getLogger() {
        return plugin.getLogger();
    }

    /**
     * Registers an executor for a command. The command must be declared in the plugin.yml in order to work.
     *
     * @param command  command to register
     * @param executor executor for that command
     */
    public final void registerCommand(String command, CommandExecutor executor) {
        PluginCommand cmd = plugin.getCommand(command);
        if (cmd != null) {
            cmd.setExecutor(executor);
        } else {
            getLogger().warning("Command '" + command + "' is not declared in the plugin.yml so it is not available.");
        }
    }

    /**
     * Gets the plugin configuration
     *
     * @return plugin configuration
     */
    public final Configuration getConfig() {
        return plugin.getConfig();
    }

    /**
     * Saves the plugin configuration.
     */
    public final void saveConfig() {
        plugin.saveConfig();
    }

    /**
     * Gets the plugin's data folder.
     *
     * @return the plugin's data folder
     */
    public final File getDataFolder() {
        return plugin.getDataFolder();
    }

    /**
     * Gets the server.
     *
     * @return currently running server
     */
    public Server getServer() {
        return plugin.getServer();
    }

    /**
     * Runs the given runnable in the main thread, delayed.
     *
     * @param runnable the task to run
     */
    public void scheduleSyncDelayedTask(Runnable runnable) {
        getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable);
    }

    /**
     * Returns a task that will repeatedly run until cancelled, starting after the specified number of server ticks.
     *
     * @param task   the task to be run
     * @param delay  the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     */
    public final BukkitTask runTaskTimer(Runnable task, long delay, long period) {
        return getServer().getScheduler().runTaskTimer(plugin, task, delay, period);
    }

    /**
     * Returns a task that will be run asynchronously.
     *
     * @param task the task to be run
     * @return a BukkitTask that contains the id number
     */
    public BukkitTask runTaskAsynchronously(Runnable task) {
        return getServer().getScheduler().runTaskAsynchronously(plugin, task);
    }

    /**
     * Returns a task that will repeatedly run asynchronously until cancelled, starting after the specified number of
     * server ticks.
     *
     * @param task   the task to be run
     * @param delay  the ticks to wait before running the task
     * @param period the ticks to wait between runs
     * @return a BukkitTask that contains the id number
     */
    public final BukkitTask runTaskTimerAsynchronously(Runnable task, long delay, long period) {
        return getServer().getScheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
    }

    /**
     * Method that is called after this component was activated.
     */
    protected void onActivated() {
    }
}
