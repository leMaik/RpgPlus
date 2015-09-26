package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

/**
 * An entity that can walk to waypoints.
 */
public interface MovingManagedEntity<T extends Entity> extends ManagedEntity<T> {
    /**
     * Get the current destination of this entity.
     *
     * @return current destination of this entity
     */
    Location getDestination();

    /**
     * Get the speed of the entity.
     *
     * @return speed
     */
    double getSpeed();

    /**
     * Callback that is invoked when this entity arrived at its current destination.
     *
     * @param destination current destination
     * @see #getDestination()
     */
    void onArrival(Location destination);
}
