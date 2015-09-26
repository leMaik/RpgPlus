package de.craften.plugins.rpgplus.common.entity;


import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.MovementType;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

/**
 * Created by Marcel on 08.08.15.
 *
 * A class for every property a RPGVillager can have
 */
public class RPGVillager extends BasicEntity implements ManagedEntity<Villager> {

    //the points it should walk to
    private List<Waypoint> waypoints;

    //the text that will be show by interaction
    private String[] dialogText;
    //the array index from dialogText
    private int currentDialog;

    //if a player blocked the villager
    private boolean blocked;
    //what player blocked the villager
    private Player blockingPlayer;

    //the entity instance of the villager
    private Villager villager;

    public RPGVillager(String name, boolean visibleName, Location location, Vector velocity, double health,
                       boolean friendly, String[] dialogText) {

        this.name = name;
        this.visibleName = visibleName;
        this.location = location;
        this.velocity = velocity;
        this.health = health;
        this.currentHealth = health;
        this.friendly = friendly;
        this.type = EntityType.VILLAGER;

        this.dialogText = dialogText;
        this.currentDialog = 0;
    }

    /**
     * spawns the entity and setting up all properties
     */
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
        this.id = villager.getEntityId();

        setAlive(true);

    }

    /**
     * despawns the entity
     */
    @Override
    public void despawn() {
        villager.remove();
    }

    /**
     * TODO
     */
    @Override
    public void update() {

    }

    /**
     * do the stuff, when the player interacts with the villager
     * now only text will be shown
     * TODO
     *
     * @param event the PlayerInteractEntityEvent
     */
    @Override
    public void onPlayerInteract(PlayerInteractEntityEvent event) {

        if (blocked) {
            if (blockingPlayer == event.getPlayer()) {
                event.getPlayer().sendMessage(dialogText[currentDialog]);
                if (dialogText.length == currentDialog + 1) {
                    currentDialog = 0;
                    blocked = false;
                    blockingPlayer = null;
                } else {
                    currentDialog++;
                }
            } else {
                event.getPlayer().sendMessage("Villager is currently busy with " + blockingPlayer.getName() + "!");
            }
        } else {
            blockingPlayer = event.getPlayer();
            blocked = true;

            event.getPlayer().sendMessage(dialogText[currentDialog]);
            if (dialogText.length == currentDialog + 1) {
                currentDialog = 0;
                blocked = false;
                blockingPlayer = null;
            } else {
                currentDialog++;
            }
        }

        event.setCancelled(true);

    }

    /**
     * returns the villager instance
     *
     * @return villager
     */
    public Villager getVillager() {
        return villager;
    }

    /**
     * sets the villager instance
     * TODO update params in case it will be updated and is already spawned
     *
     * @param villager villager
     */
    public void setVillager(Villager villager) {
        this.villager = villager;
    }

    @Override
    public Location getLocalLocation() {
        return location;
    }

    @Override
    public MovementType getMovementType() {
        return MovementType.LOCAL;
    }

    @Override
    public Villager getEntity() {
        return villager;
    }

    @Override
    public boolean isTakingDamage() {
        return isTakingDamage;
    }
}