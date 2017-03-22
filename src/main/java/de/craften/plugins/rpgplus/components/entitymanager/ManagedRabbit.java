package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.rpgplus.components.entitymanager.traits.RabbitTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Rabbit;

/**
 * A managed rabbit.
 */
public class ManagedRabbit extends RpgPlusEntity<Rabbit> {
    public ManagedRabbit(Location location) {
        super(location, EntityType.RABBIT);
        getNpc().addTrait(new RabbitTrait());
    }
}
