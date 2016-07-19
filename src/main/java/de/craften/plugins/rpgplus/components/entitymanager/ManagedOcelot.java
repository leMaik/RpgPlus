package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Ocelot;

/**
 * A managed ocelot.
 */
public class ManagedOcelot extends RpgPlusEntity<Ocelot> {
    private Ocelot.Type type;

    public ManagedOcelot(Location location) {
        super(location);
    }

    public Ocelot.Type getType() {
        return type;
    }

    public void setType(Ocelot.Type type) {
        this.type = type;
    }

    @Override
    protected Ocelot spawnEntity(Location location) {
        Ocelot ocelot = location.getWorld().spawn(location, Ocelot.class);
        if (type != null) {
            ocelot.setCatType(type);
        } else {
            type = ocelot.getCatType();
        }
        return ocelot;
    }
}
