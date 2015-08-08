package de.craften.plugins.rpgplus.common.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.util.Vector;

/**
 * Created by Marcel on 08.08.15.
 */
public abstract class BasicEntity {

    protected int id;

    protected String name;
    protected boolean visibleName;

    protected Location location;
    protected Vector velocity;

    protected boolean isAlive;

    protected double health;
    protected double currentHealth;

    protected boolean friendly;

    protected EntityType type;

    public abstract void spawn();

    public abstract void despawn();

    public abstract void update();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisibleName() {
        return visibleName;
    }

    public void setVisibleName(boolean visibleName) {
        this.visibleName = visibleName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector velocity) {
        this.velocity = velocity;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public boolean isFriendly() {
        return friendly;
    }

    public void setFriendly(boolean friendly) {
        this.friendly = friendly;
    }

    public EntityType getType() {
        return type;
    }

    public void setType(EntityType type) {
        this.type = type;
    }
}
