package de.craften.plugins.rpgplus.components.villagerdialogs;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.common.entity.RPGVillager;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

/**
 * A component to interact with villager
 */
public class VillagerDialogs extends PluginComponentBase implements Listener {

    //Save all villager in an array
    ArrayList<RPGVillager> rpgVillagerList = new ArrayList<RPGVillager>();

    /**
     * On first interact with an entity it will spawn a rpgvillager
     * Then you can interact with that created player and the method from the villager will be called
     *
     * @param event the PlayerInteractEntityEvent
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEntityEvent event) {

        //first call, the list is empty
        if (rpgVillagerList.size() == 0) {
            Location location = event.getRightClicked().getLocation();
            RPGVillager rpgVillager = new RPGVillager("Rainer Zufall", true, location, new Vector(0, 0, 0), 10.0, true, new String[]{"Moin, erste Nachricht.", "Zweite Nachricht."});
            rpgVillager.spawn();
            rpgVillagerList.add(rpgVillager);

            RpgPlus.getPlugin(RpgPlus.class).getEntityManager().registerEntity(rpgVillager);
        }
        //second and so on call
        else {
            int id = event.getRightClicked().getEntityId();
            for (RPGVillager rpgVillager : rpgVillagerList) {
                if (rpgVillager.getId() == id) {
                    rpgVillager.onPlayerInteract(event);
                }
            }
        }


//        if (event.getRightClicked() instanceof Villager) {
//            event.getPlayer().sendMessage("Du hast einen RPGVillager angeklickt.");
//        }

    }

}
