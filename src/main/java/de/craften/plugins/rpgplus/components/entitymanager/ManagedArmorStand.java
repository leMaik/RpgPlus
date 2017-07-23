package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import de.craften.plugins.rpgplus.components.entitymanager.traits.ArmorStandTrait;

/**
 * A managed armor stand.
 */
public class ManagedArmorStand extends RpgPlusEntity<ArmorStand> {
    public ManagedArmorStand(Location location) {
        super(location, EntityType.ARMOR_STAND);
        getNpc().addTrait(new ArmorStandTrait());
    }
}
