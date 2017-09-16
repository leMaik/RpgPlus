package de.craften.plugins.rpgplus.components.storage;

import org.bukkit.OfflinePlayer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Map<String, String> getAll(String key) {
        return storage.entrySet().stream()
                .filter(e -> e.getKey().startsWith(key + "."))
                .collect(Collectors.toMap(e -> e.getKey().substring(key.length() + 1), Map.Entry::getValue));
    }

    @Override
    public void set(String key, String value) {
        if (value == null) {
            storage.remove(key);
        } else {
            storage.put(key, value);
        }
    }

    @Override
    public boolean contains(String key) {
        return storage.containsKey(key);
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
    public Map<String, String> getAll(OfflinePlayer player, String key) {
        Map<String, String> playerStorage = playerStorages.get(player.getUniqueId());
        if (playerStorage == null) {
            return Collections.emptyMap();
        }

        return playerStorage.entrySet().stream()
                .filter(e -> e.getKey().startsWith(key + "."))
                .collect(Collectors.toMap(e -> e.getKey().substring(key.length() + 1), Map.Entry::getValue));
    }

    @Override
    public void set(OfflinePlayer player, String key, String value) {
        Map<String, String> playerStorage = playerStorages.computeIfAbsent(player.getUniqueId(), k -> new HashMap<>());
        if (value == null) {
            playerStorage.remove(key);
        } else {
            playerStorage.put(key, value);
        }
    }

    @Override
    public boolean contains(OfflinePlayer player, String key) {
        Map<String, String> playerStorage = playerStorages.get(player.getUniqueId());
        return playerStorage != null && playerStorage.containsKey(key);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void clear(OfflinePlayer player) {
        playerStorages.get(player.getUniqueId()).clear();
    }

    /**
     * Clears the memory storage.
     */
    @Override
    public void reload() {
        storage.clear();
        playerStorages.clear();
    }
}
