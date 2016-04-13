package de.craften.plugins.rpgplus.scripting.util;

import de.craften.plugins.managedentities.ManagedEntity;
import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.components.inventory.ItemMatcher;
import de.craften.plugins.rpgplus.scripting.api.entities.EntityWrapper;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import java.util.ArrayList;
import java.util.List;
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
     * Get the target from entity as LuaValue
     *
     * @param entity entity
     * @return lua value that represents the entity target
     */
    public static LuaValue getTarget(RpgPlusEntity entity) {
        return CoerceJavaToLua.coerce(entity.getTarget());
    }

    ;

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
        } else if (entity.isuserdata(RpgPlusEntity.class)) {
            return ((RpgPlusEntity) CoerceLuaToJava.coerce(entity, RpgPlusEntity.class)).getEntity();
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
    public static RpgPlusEntity getManagedEntity(LuaValue entity, boolean strict) {
        if (entity.isuserdata(Entity.class)) {
            ManagedEntity managedEntity = RpgPlus.getPlugin(RpgPlus.class).getEntityManager().getEntity((Entity) CoerceLuaToJava.coerce(entity, Entity.class));
            if (managedEntity instanceof RpgPlusEntity) {
                return (RpgPlusEntity) managedEntity;
            }
        } else if (entity.isuserdata(RpgPlusEntity.class)) {
            return (RpgPlusEntity) CoerceLuaToJava.coerce(entity, RpgPlusEntity.class);
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
     * Get the location that is represented by the given lua value. If that value already is a
     * {@link Location} userdata object, that object is returned
     *
     * @param location lua table that represents a location or a {@link Location} userdata object
     * @return the location
     * @throws LuaError if there are missing attributes
     */
    public static Location getLocation(LuaValue location) {
        if (location.isuserdata(Location.class)) {
            return (Location) location.checkuserdata(Location.class);
        } else {
            return new Location(
                    Bukkit.getWorld(location.get("world").checkjstring()),
                    location.get("x").checkdouble(),
                    location.get("y").checkdouble(),
                    location.get("z").checkdouble(),
                    (float) location.get("yaw").optdouble(0),
                    (float) location.get("pitch").optdouble(0));
        }
    }

    /**
     * Get a Lua location table from the given location.
     *
     * @param location location
     * @return location table
     */
    public static LuaTable getLocation(Location location) {
        LuaTable locationTable = new LuaTable();
        locationTable.set("world", LuaValue.valueOf(location.getWorld().getName()));
        locationTable.set("x", LuaValue.valueOf(location.getX()));
        locationTable.set("y", LuaValue.valueOf(location.getY()));
        locationTable.set("z", LuaValue.valueOf(location.getZ()));
        locationTable.set("yaw", LuaValue.valueOf(location.getYaw()));
        locationTable.set("pitch", LuaValue.valueOf(location.getPitch()));
        return locationTable;
    }

    /**
     * Creates an item matcher bases on the given itemstack table.
     *
     * @param itemstack table that represents an itemstack
     * @return item matcher for the represented itemstack
     */
    public static ItemMatcher createItemMatcher(LuaTable itemstack) {
        ItemMatcher.Builder builder = ItemMatcher.builder();
        builder.type(Material.valueOf(itemstack.get("type").checkjstring().toUpperCase()));
        if (!itemstack.get("data").isnil()) {
            builder.data(itemstack.get("data").checkint());
        }
        if (!itemstack.get("amount").isnil()) {
            builder.amount(itemstack.get("amount").checkint());
        }
        if (!itemstack.get("name").isnil()) {
            builder.name(ChatColor.translateAlternateColorCodes('&', itemstack.get("name").checkjstring()));
        }
        if (!itemstack.get("lore").isnil()) {
            LuaTable luaLore = itemstack.get("lore").checktable();
            List<String> lore = new ArrayList<>(luaLore.length());
            for (int i = 1; i <= luaLore.length(); i++) {
                lore.add(ChatColor.translateAlternateColorCodes('&', luaLore.get(i).checkjstring()));
            }
            builder.lore(lore);
        }
        if (itemstack.get("unbreakable").optboolean(false)) {
            builder.unbreakable(true);
        }
        return builder.build();
    }

    /**
     * Creates an {@link ItemMatcher} for the given item stack.
     *
     * @param itemstack item stack
     * @return item matcher for the given item stack
     */
    public static ItemMatcher createItemMatcher(LuaValue itemstack) {
        if (itemstack.isstring()) {
            return ItemMatcher.fromString(itemstack.checkjstring());
        } else if (itemstack.istable()) {
            return createItemMatcher(itemstack.checktable());
        } else {
            throw new LuaError("Expected string or table to represent an itemstack.");
        }
    }

    /**
     * Creates a table from the given list, just as {@link LuaValue#listOf(LuaValue[])} does for arrays.
     *
     * @param list a list
     * @return the given list as Lua table
     */
    public static LuaTable tableOf(List<? extends LuaValue> list) {
        LuaTable table = new LuaTable();
        table.presize(list.size());
        for (int i = 0; i < list.size(); i++) {
            table.rawset(i + 1, list.get(i));
        }
        return table;
    }
}
