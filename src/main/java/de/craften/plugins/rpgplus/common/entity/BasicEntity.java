package de.craften.plugins.rpgplus.common.entity;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

/**
 * Created by Marcel on 08.08.15.
 */
public abstract class BasicEntity {

    //the id of the entity
    protected int id;

    //the name of the entity
    protected String name;
    //should the name be showed to all?
    protected boolean visibleName;

    //the current location where the entity is and where it looks
    protected Location location;
    //the vector for the movement
    protected Vector velocity;

    //is the entity alive
    protected boolean isAlive;
    //is it allowed to take damage?
    protected boolean isTakingDamage;

    //the maximum health
    protected double health;
    //the current health
    protected double currentHealth;

    //is it hostile?
    protected boolean friendly;

    //what type of entity is it
    protected EntityType type;

    public abstract void spawn();

    public abstract void despawn();

    public abstract void update();

    public abstract void onPlayerInteract(PlayerInteractEntityEvent event);

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

    public boolean isTakingDamage() {
        return isTakingDamage;
    }

    public void setTakingDamage(boolean isTakingTamage) {
        this.isTakingDamage = isTakingTamage;
    }
}
