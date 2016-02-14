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
import de.craften.plugins.rpgplus.scripting.api.entities.EntitySpawner;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.api.events.ScriptEventManager;
import de.craften.plugins.rpgplus.scripting.api.images.ImageModule;
import de.craften.plugins.rpgplus.scripting.api.storage.StorageModule;
import de.craften.plugins.rpgplus.scripting.util.Pastebin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

/**
 * The main plugin class of RpgPlus.
 */
public class RpgPlus extends JavaPlugin {
    private ScriptingManager scriptingManager;
    private WeakPlayerMaps weakPlayerMaps;
    private EntityManager entityManager;
    private CustomCommands commandManager;
    private TimerComponent timerManager;
    private StorageComponent storage;
    private PathfindingComponent pathfinding;
    private DialogComponent dialogs;
    private ImagesComponent images;
    private ScriptEventManager scriptEventManager;
    private EntityEventManager entityEventManager;

    @Override
    public void onEnable() {
        scriptingManager = new ScriptingManager(getDataFolder()) {
            @Override
            protected void reportScriptError(final Exception exception) {
                getServer().broadcast("rpgplus.scripting.notifyerrors", "[RpgPlus] An error occurred while executing the script: " + exception.getMessage());
                getLogger().severe("An error occurred while executing the script: " + exception.getMessage());

                final String devKey = getConfig().getString("pastebinDevKey");
                if (devKey != null) {
                    getServer().getScheduler().runTaskAsynchronously(RpgPlus.this, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String pasteUrl = Pastebin.createStacktracePaste(devKey, "RpgPlus Error - " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), exception);
                                getServer().broadcast("rpgplus.scripting.notifyerrors", "The full stacktrace is available at: " + pasteUrl);
                            } catch (IOException e) {
                                getLogger().severe("Posting the stack trace of a script error to Pastebin failed");
                            }
                        }
                    });
                }
            }
        };

        weakPlayerMaps = new WeakPlayerMaps();
        entityManager = new EntityManager(this);
        commandManager = new CustomCommands();
        timerManager = new TimerComponent();
        storage = new StorageComponent(new File(getDataFolder(), "storage"));
        pathfinding = new PathfindingComponent();
        dialogs = new DialogComponent();
        images = new ImagesComponent();

        RpgPlusObject rpgPlusObject = new RpgPlusObject(this);
        scriptEventManager = new ScriptEventManager();
        scriptEventManager.installOn(rpgPlusObject);
        getServer().getPluginManager().registerEvents(scriptEventManager, this);

        entityEventManager = new EntityEventManager();
        getServer().getPluginManager().registerEvents(entityEventManager, this);
        EntitySpawner entitySpawner = new EntitySpawner(entityEventManager);
        entitySpawner.installOn(rpgPlusObject);

        scriptingManager.registerModule("rpgplus", rpgPlusObject);
        scriptingManager.registerModule("rpgplus.image", new ImageModule(this));
        scriptingManager.registerModule("rpgplus.scheduler", new Scheduler(this));
        scriptingManager.registerModule("rpgplus.trading", new Trading(this));
        scriptingManager.registerModule("rpgplus.timer", new ScriptTimedEventManager(this));
        scriptingManager.registerModule("rpgplus.sound", new Sound(this));
        scriptingManager.registerModule("rpgplus.storage", new StorageModule());
        scriptingManager.registerModule("rpgplus.inventory", new InventoryModule());
        scriptingManager.registerModule("rpgplus.actionbar", new ActionBarModule(this));

        weakPlayerMaps.activateFor(this);
        commandManager.activateFor(this);
        timerManager.activateFor(this);
        storage.activateFor(this);
        pathfinding.activateFor(this);
        dialogs.activateFor(this);
        images.activateFor(this);

        try {
            scriptingManager.executeScript(new File(getDataFolder(), "main.lua"));
        } catch (ScriptErrorException e) {
            getLogger().log(Level.WARNING, "Could not run main script", e);
        }
    }

    public void reload() {
        getLogger().info("Reloading...");
        scriptingManager.reset();
        scriptEventManager.reset();
        entityEventManager.reset();
        entityManager.removeAll();
        commandManager.removeAll();
        timerManager.removeAll();
        dialogs.reset();
        weakPlayerMaps.reset();

        try {
            scriptingManager.executeScript(new File(getDataFolder(), "main.lua"));
        } catch (ScriptErrorException e) {
            getLogger().log(Level.WARNING, "Could not run main script", e);
        }
        getLogger().info("Reloaded");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("rpgplus")) {
            if (args.length == 1 && args[0].equals("reload")) {
                reloadConfig();
                reload();
                sender.sendMessage("RpgPlus reloaded.");
                return true;
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
