package de.craften.plugins.rpgplus.common.entity;


import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.util.Vector;

/**
 * Created by Marcel on 08.08.15.
 */
public class RPGVillager extends BasicEntity {

    private List<Waypoint> waypoints;

    private boolean blocked;
    private Player blockingPlayer;

    private Villager villager;

    public RPGVillager(int id, String name, boolean visibleName, Location location, Vector velocity, double health,
                       boolean friendly) {

        this.id = id;
        this.name = name;
        this.visibleName = visibleName;
        this.location = location;
        this.velocity = velocity;
        this.health = health;
        this.currentHealth = health;
        this.friendly = friendly;
        this.type = EntityType.VILLAGER;
    }

    @Override
    public void spawn() {

        villager = (Villager) location.getWorld().spawn(location, this.getType().getEntityClass());

        //villager.setProfession(null);
        villager.setCustomName(name);
        villager.setCustomNameVisible(visibleName);
        villager.setMaxHealth(health);
        villager.setHealth(currentHealth);
        villager.setVelocity(velocity);
        villager.setAdult();


        setAlive(true);

    }

    @Override
    public void despawn() {
        villager.remove();
    }

    @Override
    public void update() {

    }

    public Villager getVillager() {
        return villager;
    }

    public void setVillager(Villager villager) {
        this.villager = villager;
    }
}
