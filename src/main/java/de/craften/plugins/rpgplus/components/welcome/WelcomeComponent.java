package de.craften.plugins.rpgplus.components.welcome;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * A simple components that sends a welcome message to players.
 */
public class WelcomeComponent extends PluginComponentBase implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        //As this component implements Listener, @EventHandlers are automatically registered
        event.getPlayer().sendMessage("Welcome, " + event.getPlayer().getDisplayName());
    }
}
