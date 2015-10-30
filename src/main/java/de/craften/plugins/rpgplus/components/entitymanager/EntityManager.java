package de.craften.plugins.rpgplus.components.entitymanager;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * A manager for entities.
 */
public class EntityManager extends PluginComponentBase implements Listener {
    private Map<UUID, ManagedEntity> entities;
    private Multimap<UUID, Player> nearbyPlayers;

    protected void onActivated() {
        entities = new HashMap<>();
        nearbyPlayers = MultimapBuilder.hashKeys().arrayListValues().build();

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
        ManagedEntity entity = getEntity(event.getEntity());
        unregisterEntity(entity);

        if (entity instanceof CustomDrops) {
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                event.setDroppedExp(((CustomDrops) entity).getExp((Player) killer));

                Location location = event.getEntity().getLocation();
                World world = location.getWorld();
                for (ItemStack items : ((CustomDrops) entity).getDrops((Player) killer)) {
                    world.dropItemNaturally(location, items);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        for (ManagedEntity entity : entities.values()) {
            if (entity instanceof NearbyPlayerAware) {
                handleNearbyPlayers(entity, event);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (ManagedEntity entity : entities.values()) {
            //remove all references to this player
            nearbyPlayers.remove(entity.getEntity().getUniqueId(), event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        getEntity(event.getRightClicked().getUniqueId()).onPlayerInteract(event);
    }

    private void handleNearbyPlayers(ManagedEntity entity, PlayerMoveEvent event) {
        assert entity instanceof NearbyPlayerAware;
        NearbyPlayerAware npaEntity = (NearbyPlayerAware) entity;
        Location playerLocation = event.getTo();
        Location entityLocation = entity.getEntity().getLocation();

        if (playerLocation.getWorld().equals(entityLocation.getWorld())) {
            double distanceSquared = playerLocation.distanceSquared(entityLocation);
            double radiusSquared = npaEntity.getPlayerAwareRadius() * npaEntity.getPlayerAwareRadius();
            boolean justEntered = !nearbyPlayers.containsKey(entity.getEntity().getUniqueId());

            if (distanceSquared <= radiusSquared) {
                npaEntity.onPlayerNearby(event.getPlayer(), justEntered, distanceSquared);
                nearbyPlayers.put(entity.getEntity().getUniqueId(), event.getPlayer());
            } else {
                nearbyPlayers.remove(entity.getEntity().getUniqueId(), event.getPlayer());
                if (!justEntered) {
                    npaEntity.onPlayerGone(event.getPlayer());
                }
            }
        }
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
        nearbyPlayers.removeAll(entity.getEntity().getUniqueId());
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
     * Get all entities.
     *
     * @return all entities
     */
    public Collection<ManagedEntity> getEntities() {
        return entities.values();
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