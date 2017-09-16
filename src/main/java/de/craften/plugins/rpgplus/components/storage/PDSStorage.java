package de.craften.plugins.rpgplus.components.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import de.craften.plugins.playerdatastore.api.PlayerDataStoreService;

/**
 * A storage that uses the PlayerDataStore.
 */
public class PDSStorage implements Storage{
	
	private final static UUID GLOBAL_STORE_UUID = new UUID(0, 0);
	
	private PlayerDataStoreService getPDSService() {
		return Bukkit.getServer().getServicesManager().getRegistration(PlayerDataStoreService.class).getProvider();
	}
	
	private PlayerDataStore getStore(OfflinePlayer player) {
		return getPDSService().getStore(player);
	}
	
	private PlayerDataStore getStore(UUID uuid) {
		return getPDSService().getStore(uuid);
	}
	
	private PlayerDataStore getGlobalStore() {
		return getStore(GLOBAL_STORE_UUID);
	}
	
	@Override
	public String get(String key, String defaultValue) throws StorageException {
		String value = getGlobalStore().get(key);
		return value == null ? defaultValue : value;
	}

	@Override
	public Map<String, String> getAll(String key) throws StorageException {
		Map<String, String> values = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : getGlobalStore().getAll().entrySet()) {
			if (entry.getKey().startsWith(key)) {
				values.put(entry.getKey().replace(key + ".", ""), entry.getValue());
			}
		}
		
		return values;
	}

	@Override
	public void set(String key, String value) throws StorageException {
		getGlobalStore().put(key, value);
	}

	@Override
	public boolean contains(String key) throws StorageException {
		return getGlobalStore().get(key) != null;
	}

	@Override
	public String get(OfflinePlayer player, String key, String defaultValue) throws StorageException {
		String value = getStore(player).get(key);
		return value == null ? defaultValue : value;
	}

	@Override
	public Map<String, String> getAll(OfflinePlayer player, String key) throws StorageException {
		Map<String, String> values = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : getStore(player).getAll().entrySet()) {
			if (entry.getKey().startsWith(key)) {
				values.put(entry.getKey().replace(key + ".", ""), entry.getValue());
			}
		}
		
		return values;
	}

	@Override
	public void set(OfflinePlayer player, String key, String value) throws StorageException {
		getStore(player).put(key, value);
	}

	@Override
	public boolean contains(OfflinePlayer player, String key) throws StorageException {
		return getStore(player).get(key) != null;
	}
	
	@Override
	public void clear() {
		getGlobalStore().clear();
	}
	
	@Override
	public void clear(OfflinePlayer player) {
		getStore(player).clear();
	}
	
	@Override
	public void reload() throws StorageException {
		//nothing to do there.
	}

}
