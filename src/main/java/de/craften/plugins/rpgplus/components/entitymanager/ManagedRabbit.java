package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Rabbit;

/**
 * A managed rabbit.
 */
public class ManagedRabbit extends RpgPlusEntity<Rabbit> {
    private Rabbit.Type type;

    public ManagedRabbit(Location location) {
        super(location);
    }

    public Rabbit.Type getType() {
        return type;
    }

    public void setType(Rabbit.Type type) {
        this.type = type;
    }

    @Override
    protected Rabbit spawnEntity(Location location) {
        Rabbit rabbit = location.getWorld().spawn(location, Rabbit.class);
        if (type != null) {
            rabbit.setRabbitType(type);
        } else {
            type = rabbit.getRabbitType();
        }
        return rabbit;
    }
}
