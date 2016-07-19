package de.craften.plugins.rpgplus.components.storage;

import org.bukkit.OfflinePlayer;

import java.util.Map;

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
     * @throws StorageException if getting the value fails
     */
    String get(String key, String defaultValue) throws StorageException;

    /**
     * Get all subkeys and values for the given key.
     * I.e. returns <code>bar => value</code> if <code>foo.bar => value</code> and the given key if <code>foo</code>.
     *
     * @param key key to get all subkeys for
     * @return all subkeys and values for the given key
     * @throws StorageException if getting the values fails
     */
    Map<String, String> getAll(String key) throws StorageException;

    /**
     * Set the value for the given key, overwriting the existing value.
     *
     * @param key   key
     * @param value new value
     * @throws StorageException if setting the value fails
     */
    void set(String key, String value) throws StorageException;

    /**
     * Checks if the storage contains a value for the given key.
     *
     * @param key key
     * @return true if the storage contains a value for the given key, false if not
     * @throws StorageException if checking if the value exists fails
     */
    boolean contains(String key) throws StorageException;

    /**
     * Get the value for the given key for the given player.
     *
     * @param player       player
     * @param key          key
     * @param defaultValue default  value if no value exists
     * @return the value for the given kay of the given player or the default value
     * @throws StorageException if getting the value fails
     */
    String get(OfflinePlayer player, String key, String defaultValue) throws StorageException;

    /**
     * Get all subkeys and values for the given key and player.
     * I.e. returns <code>bar => value</code> if <code>foo.bar => value</code> and the given key if <code>foo</code>.
     *
     * @param player player
     * @param key    key to get all subkeys for
     * @return all subkeys and values for the given key and player
     * @throws StorageException if getting the values fails
     */
    Map<String, String> getAll(OfflinePlayer player, String key) throws StorageException;

    /**
     * Set the value for the given key and player, overwriting the existing value.
     *
     * @param player player
     * @param key    key
     * @param value  new value
     * @throws StorageException if setting the value fails
     */
    void set(OfflinePlayer player, String key, String value) throws StorageException;

    /**
     * Checks if the storage contains a value for the given key and player.
     *
     * @param player player
     * @param key    key
     * @return true if the storage contains a value for the given key and player, false if not
     * @throws StorageException if checking if the value exists fails
     */
    boolean contains(OfflinePlayer player, String key) throws StorageException;

    /**
     * Clears any cached storage values and reloads the storage.
     *
     * @throws StorageException if reloading the storage doesn't succeed
     */
    void reload() throws StorageException;
}
