package de.craften.plugins.rpgplus.components.storage;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple in-memory storage.
 */
public class MemoryStorage implements Storage {
    private Map<String, String> storage;
    private Map<UUID, Map<String, String>> playerStorages;

    public MemoryStorage() {
        storage = new HashMap<>();
        playerStorages = new HashMap<>();
    }

    @Override
    public String get(String key, String defaultValue) {
        String value = storage.get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(String key, String value) {
        storage.put(key, value);
    }

    @Override
    public String get(OfflinePlayer player, String key, String defaultValue) {
        Map<String, String> playerStorage = playerStorages.get(player.getUniqueId());
        if (playerStorage == null) {
            return defaultValue;
        }
        String value = playerStorage.get(key);
        return value != null ? value : defaultValue;
    }

    @Override
    public void set(OfflinePlayer player, String key, String value) {
        Map<String, String> playerStorage = playerStorages.get(player.getUniqueId());
        if (playerStorage == null) {
            playerStorage = new HashMap<>();
            playerStorages.put(player.getUniqueId(), playerStorage);
        }
        playerStorage.put(key, value);
    }
}
