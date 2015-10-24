package de.craften.plugins.rpgplus.scripting.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
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
}
