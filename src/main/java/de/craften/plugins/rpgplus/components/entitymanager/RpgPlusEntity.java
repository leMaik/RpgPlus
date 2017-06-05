package de.craften.plugins.rpgplus.components.entitymanager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.TargetType;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerTeleportEvent;

import de.craften.plugins.rpgplus.util.EntityUtil;

/**
 * A basic managed entity without any special logic.
 */
public class RpgPlusEntity<T extends Entity> {
    private final NPC npc;
    private Location location;
    private boolean isTakingDamage = true;
    
    private EntityType type;
    
    private boolean isNameVisible;
    private String name;
    private String secondName;
    private ArmorStand nametag;
    private ArmorStand secondNameTag;
    
    public RpgPlusEntity(Location location, EntityType type) {
        this.location = location;
        this.type = type;
        npc = CitizensAPI.getNPCRegistry().createNPC(type, "");
        
        if (type == EntityType.PLAYER) {
        	npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, true);
        } else {
        	npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, false);
        }
        
    }

    public T spawn() {
        // TODO support custom skins
        // npc.data().setPersistent(NPC.PLAYER_SKIN_UUID_METADATA, "leMaik");

        npc.addTrait(new Trait("NameTagTrait") {
        	@Override
        	public void run() {
        		if (nametag != null) {
                   
        			nametag.teleport(npc.getEntity().getLocation().clone().add(0, EntityUtil.getEntityHeight(npc.getEntity()), 0));
        		}
        		if (secondNameTag != null) {

                    secondNameTag.teleport(npc.getEntity().getLocation().clone().add(0, EntityUtil.getEntityHeight(npc.getEntity()) + EntityUtil.NAME_TAG_HEIGHT + 0.1, 0));
        		}
        	}
        	
        	@Override
        	public void onRemove() {
                if (nametag != null) {
                    nametag.remove();
                }
                if (secondNameTag != null) {
                    secondNameTag.remove();
                }
        	}
        	
        	@Override
        	public void onDespawn() {
                if (nametag != null) {
                    nametag.remove();
                }
                if (secondNameTag != null) {
                    secondNameTag.remove();
                }
        	}
        	
        	@EventHandler
        	public void onDeath(NPCDeathEvent event) {
        		if (event.getNPC().getUniqueId().equals(npc.getUniqueId())) {
        			if (nametag != null) {
                        nametag.remove();
                    }
                    if (secondNameTag != null) {
                        secondNameTag.remove();
                    }
        		}
        	}
        });
        
        npc.spawn(location);

        if (npc.getEntity() instanceof LivingEntity && !isTakingDamage) {
            npc.setProtected(true);
        }
        

        if (type != EntityType.PLAYER) {
        	if (npc.getEntity() instanceof LivingEntity && !name.isEmpty() && isNameVisible) {
        		Location nameTagLocation = npc.getEntity().getLocation().clone().add(0, EntityUtil.getEntityHeight(npc.getEntity()), 0);
        		
            	if (nametag == null) {
            		nametag = npc.getEntity().getWorld().spawn(nameTagLocation, ArmorStand.class);
            	}
            	
            	nametag.teleport(nameTagLocation);
            	nametag.setCustomName(name);
            	nametag.setCustomNameVisible(true);
            	nametag.setVisible(false);
            	nametag.setMarker(true);
            	nametag.setCanPickupItems(false);
            	nametag.setGravity(false);
            	
        	}
        }
        
        if (npc.getEntity() != null && npc.getEntity() instanceof LivingEntity && !secondName.isEmpty() && isNameVisible) {
            Location nameTagLocation = npc.getEntity().getLocation().clone().add(0, EntityUtil.getEntityHeight(npc.getEntity()) + EntityUtil.NAME_TAG_HEIGHT + 0.1, 0);
            
            if (secondNameTag == null) {
                secondNameTag = npc.getEntity().getWorld().spawn(nameTagLocation, ArmorStand.class);
            }

            secondNameTag.teleport(nameTagLocation);
            secondNameTag.setCustomName(secondName);
            secondNameTag.setCustomNameVisible(true);
            secondNameTag.setVisible(false);
            secondNameTag.setMarker(true);
            secondNameTag.setCanPickupItems(false);
            secondNameTag.setGravity(false);

        }
        
        return (T) npc.getEntity();
    }

    public NPC getNpc() {
        return npc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
    	this.name = name;
    	if (type == EntityType.PLAYER) {
    	   npc.setName(name);
       } else {
    	   if (nametag != null) {
    		   nametag.setCustomName(name);
    	   }
       }
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
        if (secondNameTag != null) {
        	secondNameTag.setCustomName(name);
 	   	}
    }
    
    public boolean isTakingDamage() {
        return isTakingDamage;
    }

    public void setTakingDamage(boolean isTakingDamage) {
        npc.setProtected(!isTakingDamage);
        this.isTakingDamage = isTakingDamage;
    }

    public boolean isNameVisible() {
        return isNameVisible;
    }

    public void setNameVisible(boolean nameVisible) {
       this.isNameVisible = nameVisible;
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
}
