package de.craften.plugins.rpgplus.components.villagerdialogs;

import de.craften.plugins.rpgplus.common.entity.RPGVillager;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

/**
 * A component to interact with villager
 */
public class VillagerDialogs extends PluginComponentBase implements Listener {

    ArrayList<RPGVillager> rpgVillagerList = new ArrayList<RPGVillager>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {


        Location location = event.getRightClicked().getLocation();
        RPGVillager rpgVillager = new RPGVillager(1, "Rainer Zufall", true, location, new Vector(0, 0, 0), 10.0, true);
        rpgVillager.spawn();

//        if (event.getRightClicked() instanceof Villager) {
//            event.getPlayer().sendMessage("Du hast einen RPGVillager angeklickt.");
//        }

    }


    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {


    }
}
