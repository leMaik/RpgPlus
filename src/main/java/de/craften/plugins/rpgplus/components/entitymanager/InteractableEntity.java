package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.event.player.PlayerInteractEntityEvent;

/**
 * An interactable entity.
 */
public interface InteractableEntity {
    /**
     * Invoked when a player interacts with this entity.
     *
     * @param event event
     */
    void onPlayerInteract(PlayerInteractEntityEvent event);
}
