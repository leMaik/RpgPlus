package de.craften.plugins.rpgplus.components.entitymanager.traits;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;

import de.craften.plugins.rpgplus.util.EntityUtil;
import net.citizensnpcs.api.event.NPCDeathEvent;
import net.citizensnpcs.api.trait.Trait;

public class NameTagTrait extends Trait{

    private EntityType type;
    
    private boolean isNameVisible;
    private String name;
    private String secondName;
    private ArmorStand nametag;
    private ArmorStand secondNameTag;
    
	public NameTagTrait(EntityType type) {
		super("NameTag Trait");
		this.type = type;
	}
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
	
	@Override
	public void onSpawn() {
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
	}
	
	public String getFirstName() {
		return name;
	}
	
	public String getSecondName() {
		return secondName;
	}
	
	public void setFirstName(String name) {
		this.name = name;
		if (type != EntityType.PLAYER) {
			
			if (nametag != null) {
				nametag.setCustomName(name);
			}
	    	
		}
	}
	
	public void setSecondName(String name) {
		this.secondName = name;
		if (type != EntityType.PLAYER) {
			
			if (secondNameTag != null) {
				secondNameTag.setCustomName(name);
			}
	    	
		}
	}
	
	public boolean isVisible() {
		return isNameVisible;
	}
	
	public void setVisible(boolean visible) {
		this.isNameVisible = visible;
	}
	
}
