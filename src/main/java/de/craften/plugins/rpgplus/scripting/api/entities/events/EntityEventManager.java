package de.craften.plugins.rpgplus.scripting.api.entities.events;

import net.citizensnpcs.api.event.EntityTargetNPCEvent;
import net.citizensnpcs.api.event.NPCAddTraitEvent;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCCollisionEvent;
import net.citizensnpcs.api.event.NPCCombustByBlockEvent;
import net.citizensnpcs.api.event.NPCCombustByEntityEvent;
import net.citizensnpcs.api.event.NPCCombustEvent;
import net.citizensnpcs.api.event.NPCCreateEvent;
import net.citizensnpcs.api.event.NPCDamageByBlockEvent;
import net.citizensnpcs.api.event.NPCDamageByEntityEvent;
import net.citizensnpcs.api.event.NPCDamageEvent;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.event.NPCDespawnEvent;
import net.citizensnpcs.api.event.NPCEnderTeleportEvent;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCPushEvent;
import net.citizensnpcs.api.event.NPCRemoveEvent;
import net.citizensnpcs.api.event.NPCRemoveTraitEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.event.NPCSelectEvent;
import net.citizensnpcs.api.event.NPCSpawnEvent;
import net.citizensnpcs.api.event.NPCTraitCommandAttachEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import de.craften.plugins.rpgplus.scripting.util.SafeInvoker;

/**
 * An event manager for entity events.
 */
public class EntityEventManager extends EntityEventManagerImpl implements Listener {
    public EntityEventManager(SafeInvoker invoker) {
        super(invoker);
    }
    
    @EventHandler
    public void onClick(NPCClickEvent event) {
        callHandlers("entity.click", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onPlayerInteractEntity(NPCRightClickEvent event) {
        callHandlers("entity.click.right", event, event.getNPC().getEntity());
    }

    @EventHandler
    public void onLeftClick(NPCLeftClickEvent event) {
        callHandlers("entity.click.left", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onTraitAdd(NPCAddTraitEvent event) {
        callHandlers("entity.addTrait", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onCollision(NPCCollisionEvent event) {
        callHandlers("entity.collision", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onCombust(NPCCombustEvent event) {
        callHandlers("entity.combust", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onCombustByBlock(NPCCombustByBlockEvent event) {
        callHandlers("entity.combust.byBlock", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onCombustByEntity(NPCCombustByEntityEvent event) {
        callHandlers("entity.combust.byEntity", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onCreate(NPCCreateEvent event) {
        callHandlers("entity.combust.byBlock", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onDamage(NPCDamageEvent event) {
        callHandlers("entity.damage", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onDamageByBlock(NPCDamageByBlockEvent event) {
        callHandlers("entity.damage.byBlock", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onDamageByEntity(NPCDamageByEntityEvent event) {
        callHandlers("entity.damage.byEntity", event, event.getNPC().getEntity());
    }

    @EventHandler
    public void onDeath(NPCDeathEvent event) {
        callHandlers("entity.death", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onDespawn(NPCDespawnEvent event) {
        callHandlers("entity.despawn", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onEnderTeleport(NPCEnderTeleportEvent event) {
        callHandlers("entity.ender.teleport", event, event.getNPC().getEntity());
    }

    @EventHandler
    public void onPush(NPCPushEvent event) {
        callHandlers("entity.push", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onRemove(NPCRemoveEvent event) {
        callHandlers("entity.remove", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onRemoveTrait(NPCRemoveTraitEvent event) {
        callHandlers("entity.removeTrait", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onSelect(NPCSelectEvent event) {
        callHandlers("entity.select", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onSpawn(NPCSpawnEvent event) {
        callHandlers("entity.spawn", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onTraitAttach(NPCTraitCommandAttachEvent event) {
        callHandlers("entity.attachTrait", event, event.getNPC().getEntity());
    }
    
    @EventHandler
    public void onTarget(EntityTargetNPCEvent event) {
        callHandlers("entity.target", event, event.getNPC().getEntity());
    }
}
