package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * An entity that can be managed by the {@link EntityManager}.
 */
public interface ManagedEntity<T extends Entity> {
    /**
     * Get the location where the entity should be if it doesn't move on its own.
     *
     * @return location where the entity should be if it doesn't move
     */
    Location getLocalLocation();

    /**
     * Get the movement type of this entity.
     *
     * @return movement type of this entity
     */
    MovementType getMovementType();

    /**
     * Set the movement type of this entity.
     *
     * @param movementType movement type of this entity
     */
    void setMovementType(MovementType movementType);

    /**
     * Get the underlying entity.
     *
     * @return underlying entity
     */
    T getEntity();

    /**
     * Check if this entity may take damage.
     *
     * @return true if this entity may take damage, false if not
     */
    boolean isTakingDamage();

    /**
     * Set if this entity takes any damage.
     *
     * @param isTakingDamage true if this entity should take damage, false if not
     */
    void setIsTakingDamage(boolean isTakingDamage);

    /**
     * Move this entity to the given location.
     *
     * @param location target location
     */
    void moveTo(Location location);

    /**
     * Get the display name of this entity.
     *
     * @return display name of this entity
     */
    String getName();

    /**
     * Set the display name of this entity.
     *
     * @param name display name
     */
    void setName(String name);

    /**
     * Get the visibility of the display name of this entity.
     *
     * @return visible of the display name of this entity
     */
    boolean isNameVisible();

    /**
     * Set the visible of the display name of this entity.
     *
     * @param nameVisible visible of the display name
     */
    void setNameVisible(boolean nameVisible);

    /**
     * Get the second name of this entity. This name is displayed under the name of it is set visible.
     *
     * @return second name
     */
    String getSecondName();

    /**
     * Set the second name of this entity. This name is displayed under the name of it is set visible.
     *
     * @param secondName second name
     */
    void setSecondName(String secondName);

    /**
     * Spawn this entity.
     */
    void spawn();

    /**
     * Despawn this entity.
     */
    void despawn();

    /**
     * Callback for player interact events with this entity.
     *
     * @param event event
     */
    void onPlayerInteract(PlayerInteractEntityEvent event);
}
