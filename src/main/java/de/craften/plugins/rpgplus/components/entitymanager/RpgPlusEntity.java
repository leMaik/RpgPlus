package de.craften.plugins.rpgplus.components.entitymanager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.TargetType;
import net.citizensnpcs.api.npc.NPC;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.craften.plugins.rpgplus.components.entitymanager.traits.NameTagTrait;

/**
 * A basic managed entity without any special logic.
 */
public class RpgPlusEntity<T extends Entity> {
    private final NPC npc;
    private Location location;
    private boolean isTakingDamage = true;
    
    private EntityType type;
    
    public RpgPlusEntity(Location location, EntityType type) {
        this.location = location;
        this.type = type;
        npc = CitizensAPI.getNPCRegistry().createNPC(type, "");
        
        npc.addTrait(new NameTagTrait(type));
        
        if (type == EntityType.PLAYER) {
        	npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, true);
        } else {
        	npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);
        }
        
    }

    public T spawn() {
        // TODO support custom skins
        // npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, "leMaik");
    	
        npc.spawn(location);
        
        if (npc.getEntity() instanceof LivingEntity && !isTakingDamage) {
            npc.setProtected(true);
        }
        
        return (T) npc.getEntity();
    }

    public NPC getNpc() {
        return npc;
    }

    public String getName() {
        return getNameTagTrait().getFirstName();
    }

    public void setName(String name) {
    	getNameTagTrait().setFirstName(name);
    	if (type == EntityType.PLAYER) {
    	   npc.setName(name);
    	}
    }

    public String getSecondName() {
        return getNameTagTrait().getSecondName();
    }

    public void setSecondName(String secondName) {
    	getNameTagTrait().setSecondName(secondName);
    }
    
    public boolean isTakingDamage() {
        return isTakingDamage;
    }

    public void setTakingDamage(boolean isTakingDamage) {
        npc.setProtected(!isTakingDamage);
        this.isTakingDamage = isTakingDamage;
    }

    public boolean isNameVisible() {
        return getNameTagTrait().isVisible();
    }

    public void setNameVisible(boolean nameVisible) {
      	getNameTagTrait().setVisible(nameVisible);
    }

    /**
     * Gets the target of this entity. This only works if the underlying entity is a {@link LivingEntity}.
     *
     * @return the target of this entity or null if this entity has no target
     */
    public LivingEntity getTarget() {
        if (npc.isSpawned()) {
        	if (npc.getNavigator().getTargetType() == TargetType.ENTITY) {
        		return npc.getNavigator().getEntityTarget().getTarget();
        	}
        }
    	return null;
    }

    /**
     * Sets the target of this entity. This only works if the underlying entity is a {@link LivingEntity}.
     *
     * @param target target of this entity
     */
    public void setTarget(LivingEntity target) {
        if (npc.isSpawned()) {
        	npc.getNavigator().setTarget(target, true);
        }
        //TODO remember the target if the entity is not spawned yet
    }

    public void teleport(Location location) {
        npc.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
        this.location = location;
    }

    public T getEntity() {
        if (npc.isSpawned()) {
            return (T) npc.getEntity();
        }
        return null;
    }
    
    private NameTagTrait getNameTagTrait() {
        return npc.getTrait(NameTagTrait.class);
    }
}
