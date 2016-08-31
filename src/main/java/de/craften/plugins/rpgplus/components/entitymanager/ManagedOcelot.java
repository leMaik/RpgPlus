package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.rpgplus.components.entitymanager.traits.OcelotTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;

/**
 * A managed ocelot.
 */
public class ManagedOcelot extends RpgPlusEntity<Ocelot> {
    public ManagedOcelot(Location location) {
        super(location, EntityType.OCELOT);
        getNpc().addTrait(new OcelotTrait());
    }
}
