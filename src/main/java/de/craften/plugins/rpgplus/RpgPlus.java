package de.craften.plugins.rpgplus;

import de.craften.plugins.rpgplus.components.villagerdialogs.VillagerDialogs;
import de.craften.plugins.rpgplus.components.welcome.WelcomeComponent;
import de.craften.plugins.rpgplus.util.components.PluginComponent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * The main plugin class of RpgPlus.
 */
public class RpgPlus extends JavaPlugin {
    @Override
    public void onEnable() {
        new WelcomeComponent().activateFor(this);
        new VillagerDialogs().activateFor(this);
    }

    public void addComponents(PluginComponent... components) {
        for (PluginComponent c : components){
            c.activateFor(this);}
    }
}
