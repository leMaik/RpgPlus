package de.craften.plugins.rpgplus;

import de.craften.plugins.managedentities.EntityManager;
import de.craften.plugins.rpgplus.components.commands.CustomCommands;
import de.craften.plugins.rpgplus.components.dialogs.DialogComponent;
import de.craften.plugins.rpgplus.components.images.ImagesComponent;
import de.craften.plugins.rpgplus.components.pathfinding.PathfindingComponent;
import de.craften.plugins.rpgplus.components.storage.Storage;
import de.craften.plugins.rpgplus.components.storage.StorageComponent;
import de.craften.plugins.rpgplus.components.timer.TimerComponent;
import de.craften.plugins.rpgplus.scripting.ScriptErrorException;
import de.craften.plugins.rpgplus.scripting.ScriptingManager;
import de.craften.plugins.rpgplus.util.components.PluginComponent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Level;

/**
 * The main plugin class of RpgPlus.
 */
public class RpgPlus extends JavaPlugin {
    private ScriptingManager scriptingManager;
    private EntityManager entityManager;
    private CustomCommands commandManager;
    private TimerComponent timerManager;
    private StorageComponent storage;
    private PathfindingComponent pathfinding;
    private DialogComponent dialogs;
    private ImagesComponent images;

    @Override
    public void onEnable() {
        scriptingManager = new ScriptingManager();
        entityManager = new EntityManager(this);
        commandManager = new CustomCommands();
        timerManager = new TimerComponent();
        storage = new StorageComponent(new File(getDataFolder(), "storage"));
        pathfinding = new PathfindingComponent();
        dialogs = new DialogComponent();
        images = new ImagesComponent();

        scriptingManager.activateFor(this);
        commandManager.activateFor(this);
        timerManager.activateFor(this);
        storage.activateFor(this);
        pathfinding.activateFor(this);
        dialogs.activateFor(this);
        images.activateFor(this);

        try {
            scriptingManager.loadScript(new File(getDataFolder(), "main.lua"));
        } catch (ScriptErrorException e) {
            getLogger().log(Level.WARNING, "Could not run main script", e);
        }
    }

    @Override
    public void onDisable() {
        entityManager.removeAll();
    }

    public void addComponents(PluginComponent... components) {
        for (PluginComponent c : components) {
            c.activateFor(this);
        }
    }

    public ScriptingManager getScriptingManager() {
        return scriptingManager;
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
