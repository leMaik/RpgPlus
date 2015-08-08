package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A manager for entities.
 */
public class EntityManager extends PluginComponentBase implements Listener {
    private Map<UUID, ManagedEntity> entities;

    @Override
    protected void onActivated() {
        entities = new HashMap<>();

        runTaskTimer(new Runnable() {
            @Override
            public void run() {
                for (ManagedEntity entity : new ArrayList<ManagedEntity>(entities.values())) {
                    switch (entity.getMovementType()) {
                        case FROZEN:
                            entity.getEntity().teleport(entity.getLocalLocation());
                            entity.getEntity().setVelocity(new Vector(0, 0, 0));
                            break;
                        case LOCAL:
                            Location currentLocation = entity.getEntity().getLocation();
                            Location local = entity.getLocalLocation();
                            currentLocation.setX(local.getX());
                            currentLocation.setY(local.getY());
                            currentLocation.setZ(local.getZ());
                            entity.getEntity().teleport(currentLocation);
                            entity.getEntity().setVelocity(new Vector(0, 0, 0));
                            break;
                        case TRAVEL_WAYPOINTS:
                            if (entity instanceof MovingManagedEntity) {
                                moveEntity((MovingManagedEntity) entity);
                            } else {
                                getLogger().warning("Entity " + entity.getEntity().getUniqueId() +
                                        " should travel waypoints but is not a MovingManagedEntity.");
                            }
                            break;
                    }
                }
            }
        }, 0, 1);
    }

    private void moveEntity(MovingManagedEntity entity) {
        //TODO implement moving
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        ManagedEntity entity = getEntity(event.getEntity().getUniqueId());
        if (entity != null && !entity.isTakingDamage()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        unregisterEntity(event.getEntity());
    }

    /**
     * Register the given entity.
     *
     * @param entity entity to register
     */
    public void registerEntity(ManagedEntity entity) {
        entities.put(entity.getEntity().getUniqueId(), entity);
    }

    /**
     * Unregister the given entity.
     *
     * @param entity entity to unregister
     */
    public void unregisterEntity(ManagedEntity entity) {
        entities.remove(entity.getEntity().getUniqueId());
    }

    /**
     * Unregister the given entity.
     *
     * @param entity entity to unregister
     */
    public void unregisterEntity(Entity entity) {
        entities.remove(entity.getUniqueId());
    }

    /**
     * Get a managed entity by its ID.
     *
     * @param id ID of the entity
     * @return managed entity with the given ID or null if no such entity is registered
     */
    public ManagedEntity getEntity(UUID id) {
        return entities.get(id);
    }

    /**
     * Get a managed entity of an unmanaged entity.
     *
     * @param entity unmanaged entity
     * @return managed entity of the given entity or null if no such entity is registered
     */
    @SuppressWarnings("unchecked")
    public <T extends Entity> ManagedEntity<T> getEntity(T entity) {
        return entities.get(entity.getUniqueId());
    }
}