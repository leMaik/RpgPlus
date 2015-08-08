package de.craften.plugins.rpgplus.common.entity;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 * Created by Marcel on 08.08.15.
 * <p/>
 * A waypoint where to go and how
 * (just an idea for later)
 */
public class Waypoint {

    //The destination
    private Location destination;
    //The movementspeed towards the destination
    private Vector movementSpeedTowards;
    //The time to wait at this waypoint in seconds, -1 is forever
    private int destinationWaitTime;

    public Waypoint(Location destination, Vector movementSpeedTowards, int destinationWaitTime) {
        this.destination = destination;
        this.movementSpeedTowards = movementSpeedTowards;
        this.destinationWaitTime = destinationWaitTime;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public Vector getMovementSpeedTowards() {
        return movementSpeedTowards;
    }

    public void setMovementSpeedTowards(Vector movementSpeedTowards) {
        this.movementSpeedTowards = movementSpeedTowards;
    }

    public int getDestinationWaitTime() {
        return destinationWaitTime;
    }

    public void setDestinationWaitTime(int destinationWaitTime) {
        this.destinationWaitTime = destinationWaitTime;
    }
}
