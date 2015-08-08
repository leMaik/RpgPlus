package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A manager for entities.
 */
public class EntityManager extends PluginComponentBase {
    private Map<UUID, ManagedEntity> entities;
    private Map<UUID, ManagedEntity> newEntities; //second map so we don't need to copy before iterating

    @Override
    protected void onActivated() {
        entities = new HashMap<>();
        newEntities = new HashMap<>();

        runTaskTimer(new Runnable() {
            @Override
            public void run() {
                entities.putAll(newEntities);
                newEntities.clear();

                for (ManagedEntity entity : entities.values()) {
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

    /**
     * Register the given entity.
     *
     * @param entity entity to register
     */
    public void registerEntity(ManagedEntity entity) {
        newEntities.put(entity.getEntity().getUniqueId(), entity);
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
}