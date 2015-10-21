package de.craften.plugins.rpgplus.components.storage;

import org.bukkit.OfflinePlayer;

/**
 * A storage.
 */
public interface Storage {
    /**
     * Get the value for the given key.
     *
     * @param key          key
     * @param defaultValue default value if no value exists
     * @return the value for the given key or the default value
     */
    String get(String key, String defaultValue);

    /**
     * Set the value for the given key, overwriting the existing value.
     *
     * @param key   key
     * @param value new value
     */
    void set(String key, String value);

    /**
     * Checks if the storage contains a value for the given key.
     *
     * @param key key
     * @return true if the storage contains a value for the given key, false if not
     */
    boolean contains(String key);

    /**
     * Get the value for the given key for the given player.
     *
     * @param player       player
     * @param key          key
     * @param defaultValue default  value if no value exists
     * @return the value for the given kay of the given player or the default value
     */
    String get(OfflinePlayer player, String key, String defaultValue);

    /**
     * Set the value for the given key and player, overwriting the existing value.
     *
     * @param player player
     * @param key    key
     * @param value  new value
     */
    void set(OfflinePlayer player, String key, String value);

    /**
     * Checks if the storage contains a value for the given key and player.
     *
     * @param player player
     * @param key    key
     * @return true if the storage contains a value for the given key and player, false if not
     */
    boolean contains(OfflinePlayer player, String key);
}
