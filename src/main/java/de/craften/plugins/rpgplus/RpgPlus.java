package de.craften.plugins.rpgplus;

import de.craften.plugins.rpgplus.components.commands.CustomCommands;
import de.craften.plugins.rpgplus.components.entitymanager.EntityManager;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
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

    @Override
    public void onEnable() {
        scriptingManager = new ScriptingManager();
        entityManager = new EntityManager();
        commandManager = new CustomCommands();
        timerManager = new TimerComponent();
        storage = new StorageComponent(new File(getDataFolder(), "storage"));
        pathfinding = new PathfindingComponent();

        scriptingManager.activateFor(this);
        entityManager.activateFor(this);
        commandManager.activateFor(this);
        timerManager.activateFor(this);
        storage.activateFor(this);
        pathfinding.activateFor(this);

        try {
            scriptingManager.loadScript(new File(getDataFolder(), "demo.lua"));
        } catch (ScriptErrorException e) {
            getLogger().log(Level.WARNING, "Could not run demo script", e);
        }
    }

    @Override
    public void onDisable() {
        for (ManagedEntity entity : entityManager.getEntities()) {
            entity.getEntity().remove();
        }
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
}
