package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.entity.Player;

/**
 * An entity that is aware of nearby players.
 */
public interface NearbyPlayerAware {
    /**
     * Get the radius in which this entity will detect players.
     *
     * @return radius
     */
    double getPlayerAwareRadius();

    /**
     * Gets called when a player enters the awareness radius or moves in it.
     *
     * @param player          player
     * @param justEntered     whether the player was near this entity before or not
     * @param distanceSquared squared distance of the player to this entity
     */
    void onPlayerNearby(Player player, boolean justEntered, double distanceSquared);

    /**
     * Gets called when a player leaves the awareness radius.
     *
     * @param player player
     */
    void onPlayerGone(Player player);
}
