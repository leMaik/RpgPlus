package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.rpgplus.components.entitymanager.traits.HorseTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;

/**
 * A managed horse.
 */
public class ManagedHorse extends RpgPlusEntity<Horse> {
    public ManagedHorse(Location location) {
        super(location, EntityType.HORSE);
        getNpc().addTrait(new HorseTrait());
    }
}
