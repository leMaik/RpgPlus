package de.craften.plugins.rpgplus.scripting.util;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.scripting.api.entities.EntityWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import java.util.UUID;

/**
 * Utility methods for implementing the Lua API.
 */
public class ScriptUtil {
    /**
     * Get the player that is represented by the given lua value.
     *
     * @param player lua value that represents a player (player name, UUID or player userdata)
     * @return player
     */
    public static Player getPlayer(LuaValue player) {
        if (player.isuserdata(Player.class)) {
            return (Player) CoerceLuaToJava.coerce(player, Player.class);
        } else if (player.isuserdata(UUID.class)) {
            return Bukkit.getPlayer((UUID) CoerceLuaToJava.coerce(player, UUID.class));
        } else {
            String playerString = player.optjstring("");
            if (playerString.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}")) {
                return Bukkit.getPlayer(UUID.fromString(playerString));
            } else {
                return Bukkit.getPlayer(playerString);
            }
        }
    }

    /**
     * Get the player that is represented by the given lua value.
     *
     * @param player lua value that represents a player (player name, UUID or player userdata)
     * @return player
     */
    public static OfflinePlayer getOfflinePlayer(LuaValue player) {
        if (player.isuserdata(OfflinePlayer.class)) {
            return (OfflinePlayer) CoerceLuaToJava.coerce(player, OfflinePlayer.class);
        } else if (player.isuserdata(UUID.class)) {
            return Bukkit.getOfflinePlayer((UUID) CoerceLuaToJava.coerce(player, UUID.class));
        } else {
            String playerString = player.optjstring("");
            if (playerString.matches("[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}")) {
                return Bukkit.getOfflinePlayer(UUID.fromString(playerString));
            } else {
                return Bukkit.getOfflinePlayer(playerString);
            }
        }
    }

    /**
     * Get the entity that is represented by the given lua value.
     *
     * @param entity lua value that represents an entity
     * @return entity
     */
    public static Entity getEntity(LuaValue entity) {
        if (entity.isuserdata(Entity.class)) {
            return (Entity) CoerceLuaToJava.coerce(entity, Entity.class);
        } else if (entity.isuserdata(ManagedEntity.class)) {
            return ((ManagedEntity) CoerceLuaToJava.coerce(entity, ManagedEntity.class)).getEntity();
        } else if (entity instanceof EntityWrapper) {
            return ((EntityWrapper) entity).getEntity().getEntity();
        }
        throw new LuaError(entity + " is not an entity");
    }

    /**
     * Get the managed entity that is represented by the given lua value.
     *
     * @param entity lua value that represents a managed entity
     * @param strict if true, a {@link LuaError} will be thrown if the entity is not a managed entity, if false, null will be returned
     * @return the managed entity or null
     * @throws LuaError if there are missing attributes or if strict is true and the entity is not a managed entity
     */
    public static ManagedEntity getManagedEntity(LuaValue entity, boolean strict) {
        if (entity.isuserdata(Entity.class)) {
            ManagedEntity managedEntity = RpgPlus.getPlugin(RpgPlus.class).getEntityManager().getEntity((Entity) CoerceLuaToJava.coerce(entity, Entity.class));
            if (managedEntity != null) {
                return managedEntity;
            }
        } else if (entity.isuserdata(ManagedEntity.class)) {
            return (ManagedEntity) CoerceLuaToJava.coerce(entity, ManagedEntity.class);
        } else if (entity instanceof EntityWrapper) {
            return ((EntityWrapper) entity).getEntity();
        }

        if (strict) {
            throw new LuaError(entity + " is not a managed entity");
        } else {
            return null;
        }
    }

    /**
     * Get the location (without pitch and yaw) that is represented by the given lua table.
     *
     * @param location lua table that represents a location
     * @return the location
     * @throws LuaError if there are missing attributes
     */
    public static Location getLocation(LuaTable location) {
        return new Location(
                Bukkit.getWorld(location.get("world").checkjstring()),
                location.get("x").checkdouble(),
                location.get("y").checkdouble(),
                location.get("z").checkdouble());
    }
}
