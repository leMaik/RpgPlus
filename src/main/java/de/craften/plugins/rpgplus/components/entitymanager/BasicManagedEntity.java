package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

/**
 * A basic managed entity without any special logic.
 */
public class BasicManagedEntity<T extends Entity> implements ManagedEntity<T> {
    private final Class<T> entityType;
    private T entity;
    private Location localLocation;
    private MovementType movementType;
    private boolean isTakingDamage;
    private String name;

    public BasicManagedEntity(Class<T> entity, Location location) {
        this.entityType = entity;
        localLocation = location;
        setMovementType(MovementType.NORMAL);
        setIsTakingDamage(true);
    }

    @Override
    public Location getLocalLocation() {
        return localLocation;
    }

    @Override
    public void moveTo(Location location) {
        Location dest = location.clone();
        dest.setDirection(dest.toVector().subtract(localLocation.toVector()));
        localLocation = dest;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        if (entity != null) {
            entity.setCustomName(name);
        }
    }

    @Override
    public void spawn() {
        if (entity != null) {
            despawn();
        }
        entity = localLocation.getWorld().spawn(localLocation, entityType);
        setMovementType(movementType);
        setName(name);
    }

    @Override
    public void despawn() {
        if (entity != null) {
            entity.remove();
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
    }

    @Override
    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
        switch (movementType) {
            case LOCAL:
            case FROZEN:
                entity.setVelocity(new Vector(0, 0, 0));
                break;
        }
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public boolean isTakingDamage() {
        return isTakingDamage;
    }

    public void setIsTakingDamage(boolean isTakingDamage) {
        this.isTakingDamage = isTakingDamage;
    }
}
