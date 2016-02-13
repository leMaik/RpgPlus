package de.craften.plugins.rpgplus.components;

import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A component that manages weak maps for player data. Player maps are automatically removed when a player quits and are
 * not persisted.
 */
public class WeakPlayerMaps extends PluginComponentBase implements Listener {
    private Collection<Map> playerDataMap = new ArrayList<>();

    /**
     * Creates a new weak players map. Players will automatically be removed when they leave the server. This map should
     * be re-used across reloads.
     *
     * @param clazz class of the map values
     * @param <T>   type of the map values
     * @return a new weak player map
     */
    public <T> Map<Player, T> createMap(Class<T> clazz) {
        Map<Player, T> map = new HashMap<>();
        playerDataMap.add(map);
        return map;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (Map map : playerDataMap) {
            map.remove(event.getPlayer());
        }
    }

    /**
     * Clears all previously created weak player maps, as if every player left the server.
     */
    public void reset() {
        for (Map map : playerDataMap) {
            map.clear();
        }
    }
}
