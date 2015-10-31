package de.craften.plugins.rpgplus.components.entitymanager;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

/**
 * A basic managed entity without any special logic.
 */
public class BasicManagedEntity<T extends Entity> implements ManagedEntity<T> {
    private final Class<T> entityType;
    private T entity;
    private Location localLocation;
    private MovementType movementType;
    private boolean isTakingDamage;
    private String name;
    private ArmorStand nametag;
    private boolean nameVisible;
    private String secondName;
    private ArmorStand secondNameTag;
    
    public BasicManagedEntity(Class<T> entity, Location location) {
        this.entityType = entity;
        localLocation = location;
        setMovementType(MovementType.NORMAL);
        setIsTakingDamage(true);
    }

    @Override
    public Location getLocalLocation() {
        return localLocation;
    }

    @Override
    public void moveTo(Location location) {
        Location dest = location.clone();
        dest.setDirection(dest.toVector().subtract(localLocation.toVector()));
        localLocation = dest;
        if(nametag != null){
            Location nameTagLocation = localLocation.clone().add(0, 1.85, 0);
            
            if(secondNameTag != null){
                secondNameTag.teleport(nameTagLocation);
                nameTagLocation = nameTagLocation.add(0, 0.15, 0);
            }
            
            nametag.teleport(location.clone().add(0, 1.85, 0));
        }
        
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        if (entity != null) {
            if(entity instanceof LivingEntity && !name.equals("") && nameVisible){
                Location nameTagLocation = localLocation.clone().add(0, 2, 0);
                
                if(nametag == null){
                    nametag = (ArmorStand) entity.getWorld().spawnEntity(nameTagLocation, EntityType.ARMOR_STAND);
                }
                
                nametag.teleport(nameTagLocation);
                nametag.setCustomName(name);
                nametag.setCustomNameVisible(true);
                nametag.setVisible(false);
                nametag.setMarker(true);
                nametag.setCanPickupItems(false);
                nametag.setGravity(false);
                
            }else {
                entity.setCustomName(name);
                entity.setCustomNameVisible(nameVisible);
            }
        }
    }
    
    @Override
    public boolean getNameVisible() {
        return nameVisible;
    }
    
    @Override
    public void setNameVisible(boolean nameVisible) {
        this.nameVisible = nameVisible;
    }
    
    @Override
    public String getSecondName() {
        return secondName;
    }
    
    @Override
    public void setSecondName(String secondName) {
        this.secondName = secondName;
        if(entity != null && entity instanceof LivingEntity && !secondName.equals("") && nameVisible){
            Location nameTagLocation = localLocation.clone().add(0, 1.75, 0);
            
            if(secondNameTag == null){
                secondNameTag = (ArmorStand) entity.getWorld().spawnEntity(nameTagLocation, EntityType.ARMOR_STAND);
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
    
    @Override
    public void spawn() {
        if (entity != null) {
            despawn();
        }
        entity = localLocation.getWorld().spawn(localLocation, entityType);
        setMovementType(movementType);
        setNameVisible(nameVisible);
        setName(name);
        setSecondName(secondName);
    }

    @Override
    public void despawn() {
        if (entity != null) {
            entity.remove();
        }
        
        if(nametag != null){
            nametag.remove();
        }
        if(secondNameTag != null){
            secondNameTag.remove();
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
    }

    @Override
    public MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementType movementType) {
        this.movementType = movementType;
        if (entity != null) {
            switch (movementType) {
                case LOCAL:
                case FROZEN:
                    entity.setVelocity(new Vector(0, 0, 0));
                    break;
            }
        }
    }

    @Override
    public T getEntity() {
        return entity;
    }

    @Override
    public boolean isTakingDamage() {
        return isTakingDamage;
    }

    public void setIsTakingDamage(boolean isTakingDamage) {
        this.isTakingDamage = isTakingDamage;
    }
}
