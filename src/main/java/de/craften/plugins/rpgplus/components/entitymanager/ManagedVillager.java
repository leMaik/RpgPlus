package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.rpgplus.components.entitymanager.traits.VillagerTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

/**
 * A managed villager.
 */
public class ManagedVillager extends RpgPlusEntity<Villager> {
    public ManagedVillager(Location location) {
    	super(location, EntityType.VILLAGER);
        getNpc().addTrait(new VillagerTrait());
    }
}
