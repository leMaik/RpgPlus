package de.craften.plugins.rpgplus.components.entitymanager;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * A basic managed entity without any special logic.
 */
public class RpgPlusEntity<T extends Entity> {
    private final NPC npc;
    private Location location;
    private boolean isTakingDamage = true;

    public RpgPlusEntity(Location location, EntityType type) {
        this.location = location;
        npc = CitizensAPI.getNPCRegistry().createNPC(type, "");
        npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, true);
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
        return npc.getName();
    }

    public void setName(String name) {
        npc.setName(name);
    }

    public String getSecondName() {
        // TODO
        return "";
    }

    public void setSecondName(String secondName) {
        // TODO
    }

    public boolean isTakingDamage() {
        return isTakingDamage;
    }

    public void setTakingDamage(boolean isTakingDamage) {
        npc.setProtected(!isTakingDamage);
        this.isTakingDamage = isTakingDamage;
    }

    public boolean isNameVisible() {
        return npc.data().get(NPC.NAMEPLATE_VISIBLE_METADATA);
    }

    public void setNameVisible(boolean nameVisible) {
        npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, nameVisible);
    }

    /**
     * Gets the target of this entity. This only works if the underlying entity is a {@link LivingEntity}.
     *
     * @return the target of this entity or null if this entity has no target
     */
    public LivingEntity getTarget() {
        if (npc.isSpawned()) {
            Entity entity = npc.getEntity();
            if (entity instanceof Creature) {
                return ((Creature) entity).getTarget();
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
            Entity entity = npc.getEntity();
            if (entity instanceof Creature) {
                ((Creature) entity).setTarget(target);
            }
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
