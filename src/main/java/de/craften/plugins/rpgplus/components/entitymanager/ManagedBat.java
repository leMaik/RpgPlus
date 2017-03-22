package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.rpgplus.components.entitymanager.traits.BatTrait;
import org.bukkit.Location;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;

/**
 * A managed bat.
 */
public class ManagedBat extends RpgPlusEntity<Bat> {
    public ManagedBat(Location location) {
        super(location, EntityType.BAT);
        getNpc().addTrait(new BatTrait());
    }
}
