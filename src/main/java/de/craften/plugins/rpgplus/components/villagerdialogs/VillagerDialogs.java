package de.craften.plugins.rpgplus.components.villagerdialogs;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * A component to interact with villager
 */
public class VillagerDialogs extends PluginComponentBase implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {

        if (event.getRightClicked() instanceof Villager) {
            event.getPlayer().sendMessage("Du hast einen Villager angeklickt.");
        }
    }
}
