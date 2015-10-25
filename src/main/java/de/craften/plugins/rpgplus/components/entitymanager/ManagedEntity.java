package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * An entity that can be managed by the {@link EntityManager}.
 */
public interface ManagedEntity<T extends Entity> {
    /**
     * Get the location where the entity should be if it doesn't move. This may be updated for a
     * {@link MovingManagedEntity} if it should stand after walking a bit.
     *
     * @return location where the entity should be if it doesn't move
     */
    Location getLocalLocation();

    /**
     * Get the movement type of this entity.
     *
     * @return movement type of this entity
     */
    MovementType getMovementType();

    /**
     * Get the underlying entity.
     *
     * @return underlying entity
     */
    T getEntity();

    /**
     * Check if this entity may take damage.
     *
     * @return true if this entity may take damage, false if not
     */
    boolean isTakingDamage();

    /**
     * Teleport this entity to the given location.
     *
     * @param location target location
     */
    void moveTo(Location location);
}
