package de.craften.plugins.rpgplus;

import de.craften.plugins.rpgplus.components.welcome.WelcomeComponent;
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

    @Override
    public void onEnable() {
        scriptingManager = new ScriptingManager();
        scriptingManager.activateFor(this);

        new WelcomeComponent().activateFor(this);

        try {
            scriptingManager.loadScript(new File(getDataFolder(), "demo.lua"));
        } catch (ScriptErrorException e) {
            getLogger().log(Level.WARNING, "Could not run demo script", e);
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
}
