package de.craften.plugins.rpgplus.components.entitymanager;

/**
 * Movement type of a {@link ManagedEntity}.
 */
public enum MovementType {
    /**
     * Normal entity-specific movement.
     */
    NORMAL,

    /**
     * Stay at the place but allow turning or moving the head.
     */
    LOCAL,

    /*
     * No movement at all.
     */
    FROZEN,

    /**
     * Move to the destination waypoint. Only works with {@link MovingManagedEntity}.
     */
    TRAVEL_WAYPOINTS
}
