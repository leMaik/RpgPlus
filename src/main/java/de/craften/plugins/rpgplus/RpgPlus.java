package de.craften.plugins.rpgplus;

import de.craften.plugins.rpgplus.components.commands.CustomCommands;
import de.craften.plugins.rpgplus.components.entitymanager.EntityManager;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
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

    @Override
    public void onEnable() {
        scriptingManager = new ScriptingManager();
        scriptingManager.activateFor(this);

        entityManager = new EntityManager();
        entityManager.activateFor(this);

        commandManager = new CustomCommands();
        commandManager.activateFor(this);

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
}
