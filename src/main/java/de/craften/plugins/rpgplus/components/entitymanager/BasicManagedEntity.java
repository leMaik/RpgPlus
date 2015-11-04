package de.craften.plugins.rpgplus.components.entitymanager;

import de.craften.plugins.rpgplus.util.EntityUtil;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;

import java.util.UUID;

/**
 * A basic managed entity without any special logic.
 */
class BasicManagedEntity<T extends Entity> implements ManagedEntity<T> {
    private final UUID id = UUID.randomUUID();
    private final Class<T> entityType;
    private final EntityManager manager;
    private T entity;
    private Location localLocation;
    private MovementType movementType;
    private boolean isTakingDamage;
    private String name;
    private ArmorStand nametag;
    private boolean isNameVisible;
    private String secondName;
    private ArmorStand secondNameTag;

    BasicManagedEntity(Class<T> entity, Location location, EntityManager manager) {
        this.entityType = entity;
        localLocation = location;
        setMovementType(MovementType.NORMAL);
        setIsTakingDamage(true);
        this.manager = manager;
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
        if (nametag != null) {
            Location nameTagLocation = localLocation.clone().add(0, EntityUtil.getEntityHeight(entity) + 0.05, 0);

            if (secondNameTag != null) {
                secondNameTag.teleport(nameTagLocation);
                nameTagLocation.add(0, EntityUtil.NAME_TAG_HEIGHT + 0.05, 0);
            }

            nametag.teleport(nameTagLocation);
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        if (name != null && !name.isEmpty()) {
            if (entity != null) {
                if (entity instanceof LivingEntity && isNameVisible) {
                    Location nameTagLocation = localLocation.clone().add(0, EntityUtil.getEntityHeight(entity) + EntityUtil.NAME_TAG_HEIGHT + 0.1, 0);

                    if (nametag == null) {
                        nametag = (ArmorStand) entity.getWorld().spawnEntity(nameTagLocation, EntityType.ARMOR_STAND);
                    }

                    nametag.teleport(nameTagLocation);
                    nametag.setCustomName(name);
                    nametag.setCustomNameVisible(true);
                    nametag.setVisible(false);
                    nametag.setMarker(true);
                    nametag.setCanPickupItems(false);
                    nametag.setGravity(false);

                } else {
                    entity.setCustomName(name);
                    entity.setCustomNameVisible(isNameVisible);
                }
            } else {
                if (nametag != null) {
                    nametag.remove();
                    nametag = null;
                }
            }
        }
    }

    @Override
    public boolean isNameVisible() {
        return isNameVisible;
    }

    @Override
    public void setNameVisible(boolean nameVisible) {
        this.isNameVisible = nameVisible;
    }

    @Override
    public String getSecondName() {
        return secondName;
    }

    @Override
    public void setSecondName(String secondName) {
        this.secondName = secondName;
        if (secondName != null && !secondName.isEmpty()) {
            if (entity != null && entity instanceof LivingEntity && isNameVisible) {
                Location nameTagLocation = localLocation.clone().add(0, EntityUtil.getEntityHeight(entity), 0);

                if (secondNameTag == null) {
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
        } else {
            if (secondNameTag != null) {
                secondNameTag.remove();
                secondNameTag = null;
            }
        }
    }

    @Override
    public void spawn() {
        if (entity != null) {
            despawn();
        }
        entity = localLocation.getWorld().spawn(localLocation, entityType);
        manager.registerEntity(this);

        setMovementType(movementType);
        setNameVisible(isNameVisible);
        setName(name);
        setSecondName(secondName);
    }

    @Override
    public void despawn() {
        if (entity != null && !entity.isDead()) {
            entity.remove();
            manager.unregisterEntity(entity);
            entity = null;
        }

        if (nametag != null) {
            nametag.remove();
            nametag = null;
        }
        if (secondNameTag != null) {
            secondNameTag.remove();
            secondNameTag = null;
        }
    }

    @Override
    public void kill() {
        if (entity instanceof Damageable) {
            ((Damageable) entity).setHealth(0);
            manager.unregisterEntity(entity);

            if (nametag != null) {
                nametag.remove();
                nametag = null;
            }
            if (secondNameTag != null) {
                secondNameTag.remove();
                secondNameTag = null;
            }
        } else {
            despawn();
        }
    }

    @Override
    public void onPlayerInteract(PlayerInteractEntityEvent event) {
    }

    @Override
    public void onLocationChanged() {
        if (nametag != null) {
            Location nameTagLocation = entity.getLocation().clone().add(0, EntityUtil.getEntityHeight(entity) + 0.05, 0);

            if (secondNameTag != null) {
                secondNameTag.teleport(nameTagLocation);
                nameTagLocation.add(0, EntityUtil.NAME_TAG_HEIGHT + 0.05, 0);
            }

            nametag.teleport(nameTagLocation);
        }
    }

    @Override
    public UUID getUniqueId() {
        return id;
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

    @Override
    public int hashCode() {
        return getUniqueId().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ManagedEntity && ((ManagedEntity) obj).getUniqueId().equals(getUniqueId());
    }
}
