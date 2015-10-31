package de.craften.plugins.rpgplus.scripting.api.entities.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;


/**
 * An event manager for entity events.
 */
public class EntityEventManager extends EntityEventManagerImpl implements Listener {
    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        callHandlers("interact", event);
    }
}
