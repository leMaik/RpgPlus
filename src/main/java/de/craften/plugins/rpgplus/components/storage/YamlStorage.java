package de.craften.plugins.rpgplus.components.storage;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import de.craften.plugins.rpgplus.util.ConfigurationSectionUtil;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * A storage that uses yaml files.
 */
public class YamlStorage implements Storage {
    private final File storageDirectory;
    private final File storageFile;
    private final FileConfiguration storage;
    private final LoadingCache<UUID, FileConfiguration> playerStorages;

    public YamlStorage(File storageDirectory) {
        this.storageDirectory = storageDirectory;
        storageFile = new File(storageDirectory, "global.yml");

        YamlConfiguration storage = new YamlConfiguration();
        if (storageFile.exists()) {
            try {
                storage.load(storageFile);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException("Storage corrupted", e);
            }
        }
        this.storage = storage;

        playerStorages = CacheBuilder.newBuilder().maximumSize(25).expireAfterWrite(1, TimeUnit.HOURS)
                .build(new CacheLoader<UUID, FileConfiguration>() {
                    @Override
                    public FileConfiguration load(UUID uuid) throws Exception {
                        File storageFile = getFile(uuid);
                        YamlConfiguration playerStorage = new YamlConfiguration();
                        if (storageFile.exists()) {
                            try {
                                playerStorage.load(storageFile);
                            } catch (IOException | InvalidConfigurationException e) {
                                throw new IOException("Storage corrupted", e);
                            }
                        }
                        return playerStorage;
                    }
                });
    }

    private File getFile(UUID uuid) {
        return Paths.get(storageDirectory.getAbsolutePath(), "players",
                uuid.toString().substring(0, 1), uuid.toString() + ".yml").toFile();
    }

    @Override
    public String get(String key, String defaultValue) {
        return storage.getString(key, defaultValue);
    }

    @Override
    public Map<String, String> getAll(String key) {
        Map<String, String> result = new HashMap<>();
        if (storage.isConfigurationSection(key)) {
            ConfigurationSection section = storage.getConfigurationSection(key);
            return ConfigurationSectionUtil.flatten(section);
        }
        return result;
    }

    @Override
    public void set(String key, String value) {
        storage.set(key, value);
        try {
            storage.save(storageFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean contains(String key) {
        return storage.contains(key);
    }

    @Override
    public String get(OfflinePlayer player, String key, String defaultValue) {
        try {
            ConfigurationSection playerStorage = playerStorages.get(player.getUniqueId());
            if (playerStorage == null) {
                return defaultValue;
            }
            return playerStorage.getString(key, defaultValue);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, String> getAll(OfflinePlayer player, String key) {
        try {
            ConfigurationSection playerStorage = playerStorages.get(player.getUniqueId());
            return ConfigurationSectionUtil.flatten(playerStorage);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void set(OfflinePlayer player, String key, String value) {
        try {
            FileConfiguration playerStorage = playerStorages.get(player.getUniqueId());
            playerStorage.set(key, value);
            playerStorage.save(getFile(player.getUniqueId()));
        } catch (ExecutionException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean contains(OfflinePlayer player, String key) {
        try {
            ConfigurationSection playerStorage = playerStorages.get(player.getUniqueId());
            return playerStorage != null && playerStorage.contains(key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
