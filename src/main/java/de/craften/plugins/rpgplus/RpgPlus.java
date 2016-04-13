package de.craften.plugins.rpgplus;

import de.craften.plugins.managedentities.EntityManager;
import de.craften.plugins.rpgplus.components.WeakPlayerMaps;
import de.craften.plugins.rpgplus.components.commands.CustomCommands;
import de.craften.plugins.rpgplus.components.dialogs.DialogComponent;
import de.craften.plugins.rpgplus.components.images.ImagesComponent;
import de.craften.plugins.rpgplus.components.pathfinding.PathfindingComponent;
import de.craften.plugins.rpgplus.components.storage.Storage;
import de.craften.plugins.rpgplus.components.storage.StorageComponent;
import de.craften.plugins.rpgplus.components.timer.TimerComponent;
import de.craften.plugins.rpgplus.scripting.ScriptErrorException;
import de.craften.plugins.rpgplus.scripting.ScriptingManager;
import de.craften.plugins.rpgplus.scripting.api.*;
import de.craften.plugins.rpgplus.scripting.api.actionbar.ActionBarModule;
import de.craften.plugins.rpgplus.scripting.api.entities.EntityModule;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.api.events.ScriptEventManager;
import de.craften.plugins.rpgplus.scripting.api.images.ImageModule;
import de.craften.plugins.rpgplus.scripting.api.inventory.InventoryModule;
import de.craften.plugins.rpgplus.scripting.api.inventory.events.InventoryEventManager;
import de.craften.plugins.rpgplus.scripting.api.storage.StorageModule;
import de.craften.plugins.rpgplus.scripting.api.title.TitlesModule;
import de.craften.plugins.rpgplus.scripting.util.Pastebin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * The main plugin class of RpgPlus.
 */
public class RpgPlus extends JavaPlugin {
    private final ScriptingManager scriptingManager;
    private final WeakPlayerMaps weakPlayerMaps;
    private EntityManager entityManager;
    private final CustomCommands commandManager;
    private final TimerComponent timerManager;
    private final StorageComponent storage;
    private final PathfindingComponent pathfinding;
    private final DialogComponent dialogs;
    private final ImagesComponent images;
    private final ScriptEventManager scriptEventManager;
    private final EntityEventManager entityEventManager;
    private final InventoryEventManager inventoryEventManager;

    public RpgPlus() {
        weakPlayerMaps = new WeakPlayerMaps();
        commandManager = new CustomCommands();
        timerManager = new TimerComponent();
        storage = new StorageComponent(new File(getDataFolder(), "storage"));
        pathfinding = new PathfindingComponent();
        dialogs = new DialogComponent();
        images = new ImagesComponent();

        Path scriptDirectory = Paths.get(getDataFolder().getAbsolutePath()).resolve(getConfig().getString("scriptDirectory", ""));
        ScriptingManager.StrictModeOption strictMode;
        if (getConfig().isBoolean("strictMode")) {
            strictMode = getConfig().getBoolean("strictMode") ? ScriptingManager.StrictModeOption.ERROR : ScriptingManager.StrictModeOption.DISABLED;
        } else {
            strictMode = ScriptingManager.StrictModeOption.valueOf(getConfig().getString("strictMode", "disabled").toUpperCase());
        }
        scriptingManager = new ScriptingManager(scriptDirectory.toFile(), strictMode) {
            @Override
            protected void reportScriptError(final Exception exception) {
                getServer().broadcast("[RpgPlus] " + ChatColor.RED + "An error occurred while executing the script: " + exception.getMessage(), "rpgplus.scripting.notifyerrors");
                for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
                    getServer().broadcast("[RpgPlus] " + stackTraceElement.toString(), "rpgplus.scripting.notifyerrors");
                }
                getLogger().severe("An error occurred while executing the script: " + exception.getMessage());

                final String devKey = getConfig().getString("pastebinDevKey");
                if (devKey != null) {
                    getServer().getScheduler().runTaskAsynchronously(RpgPlus.this, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String pasteUrl = Pastebin.createStacktracePaste(devKey, "RpgPlus Error - " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), exception);
                                getServer().broadcast("The full stacktrace is available at: " + pasteUrl, "rpgplus.scripting.notifyerrors");
                            } catch (IOException e) {
                                getLogger().severe("Posting the stack trace of a script error to Pastebin failed");
                            }
                        }
                    });
                }
            }

            @Override
            public void reportScriptWarning(String warning) {
                getLogger().warning(warning);
                
                //manually broadcast so that the message is not displayed twice in the console
                for (Player player : getServer().getOnlinePlayers()) {
                    if (player.hasPermission("rpgplus.scripting.notifyerrors")) {
                        player.sendMessage("[RpgPlus] " + ChatColor.GOLD + warning);
                    }
                }
            }
        };

        scriptEventManager = new ScriptEventManager(scriptingManager);
        entityEventManager = new EntityEventManager(scriptingManager);
        inventoryEventManager = new InventoryEventManager(scriptingManager);
    }

    @Override
    public void onEnable() {
        entityManager = new EntityManager(this);
        RpgPlusObject rpgPlusObject = new RpgPlusObject(this);
        scriptEventManager.installOn(rpgPlusObject);
        getServer().getPluginManager().registerEvents(scriptEventManager, this);

        getServer().getPluginManager().registerEvents(entityEventManager, this);

        getServer().getPluginManager().registerEvents(inventoryEventManager, this);

        weakPlayerMaps.activateFor(this);
        commandManager.activateFor(this);
        timerManager.activateFor(this);
        storage.activateFor(this);
        pathfinding.activateFor(this);
        dialogs.activateFor(this);
        images.activateFor(this);

        scriptingManager.registerModule("rpgplus", rpgPlusObject);
        scriptingManager.registerModule("rpgplus.image", new ImageModule(this));
        scriptingManager.registerModule("rpgplus.scheduler", new Scheduler(this));
        scriptingManager.registerModule("rpgplus.trading", new Trading(this));
        scriptingManager.registerModule("rpgplus.timer", new ScriptTimedEventManager(this));
        scriptingManager.registerModule("rpgplus.sound", new Sound(this));
        scriptingManager.registerModule("rpgplus.storage", new StorageModule(storage.getStorage()));
        scriptingManager.registerModule("rpgplus.inventory", new InventoryModule(inventoryEventManager));
        scriptingManager.registerModule("rpgplus.actionbar", new ActionBarModule(this));
        scriptingManager.registerModule("rpgplus.titles", new TitlesModule());
        scriptingManager.registerModule("rpgplus.entities", new EntityModule(entityManager, entityEventManager));

        executeMainScript();
    }

    private void reload() {
        getLogger().info("Reloading...");
        reset();
        executeMainScript();
        getLogger().info("Reloaded");
    }

    private void reset() {
        scriptingManager.reset();
        scriptEventManager.reset();
        entityEventManager.reset();
        inventoryEventManager.reset();
        entityManager.removeAll();
        commandManager.removeAll();
        timerManager.removeAll();
        dialogs.reset();
        weakPlayerMaps.reset();
    }

    private void executeMainScript() {
        if (getConfig().getBoolean("enabled", true)) {
            try {
                scriptingManager.executeScript(new File(scriptingManager.getScriptDirectory(), "main.lua"));
            } catch (ScriptErrorException e) {
                getLogger().log(Level.WARNING, "Could not run main script", e);
            }
        } else {
            getLogger().warning("Scripts are currently disabled, use /rpgplus enable to enable them");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("rpgplus")) {
            if (args.length == 1) {
                switch (args[0]) {
                    case "reload":
                        reloadConfig();
                        reload();
                        sender.sendMessage("RpgPlus reloaded.");
                        return true;
                    case "disable":
                        getConfig().set("enabled", false);
                        saveConfig();
                        reset();
                        sender.sendMessage("Scripts disabled.");
                        return true;
                    case "enable":
                        getConfig().set("enabled", true);
                        saveConfig();
                        reload();
                        sender.sendMessage("Scripts enabled and reloaded");
                        return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        entityManager.removeAll();
    }

    public ScriptingManager getScriptingManager() {
        return scriptingManager;
    }

    public WeakPlayerMaps getWeakPlayerMaps() {
        return weakPlayerMaps;
    }

    /**
     * Get the entity manager.
     *
     * @return the entity manager
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * Gets the manager for custom commands.
     *
     * @return the custom command manager
     */
    public CustomCommands getCommandManager() {
        return commandManager;
    }

    /**
     * Gets the manager for timers.
     *
     * @return the timer manager
     */
    public TimerComponent getTimerManager() {
        return timerManager;
    }

    /**
     * Gets the storage.
     *
     * @return the storage
     */
    public Storage getStorage() {
        return storage.getStorage();
    }

    /**
     * Gets the pathfinding component.
     *
     * @return the pathfinding component
     */
    public PathfindingComponent getPathfinding() {
        return pathfinding;
    }

    /**
     * Gets the dialog component.
     *
     * @return the dialog component
     */
    public DialogComponent getDialogs() {
        return dialogs;
    }

    /**
     * Gets the images component.
     *
     * @return the images component
     */
    public ImagesComponent getImages() {
        return images;
    }
}
