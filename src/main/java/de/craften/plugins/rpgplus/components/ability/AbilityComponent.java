package de.craften.plugins.rpgplus.components.ability;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;

import de.craften.plugins.playerdatastore.api.PlayerDataStore;
import de.craften.plugins.playerdatastore.api.PlayerDataStoreService;
import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;

public class AbilityComponent extends PluginComponentBase implements Listener {

	private final RpgPlus plugin;

	private final Map<String, Ability> abilities = new HashMap<String, Ability>();;
	private BukkitTask checkerTaskId;
	private final Multimap<Player, String> playerAbilities = MultimapBuilder.hashKeys().arrayListValues(3).build();
	private final Multimap<Player, String> pausedAbilities = MultimapBuilder.hashKeys().arrayListValues(3).build();

	public AbilityComponent(final RpgPlus plugin) {
		this.plugin = plugin;
	}

	@Override
	protected void onActivated() {

		checkerTaskId = Bukkit.getServer().getScheduler().runTaskTimer(plugin, () -> {
			final long now = new Date().getTime();
			new ArrayList<>(playerAbilities.keys()).forEach((player) -> {
				PlayerDataStore store = getStore(player);
				new ArrayList<>(playerAbilities.get(player)).forEach((abilityClass) -> {
					Ability ability = abilities.get(abilityClass);
					try {
						if (getEndTime(store, ability).get() < now && getEndTime(store, ability).get() != -1) {
							ability.removeFrom(player);
							ability.onExpired(player);
							removeActiveKey(store, ability);
							playerAbilities.get(player).remove(abilityClass);
						}
					} catch (InterruptedException | ExecutionException e) {
						// ignore
					}
				});
			});
		}, 20, 100);

	}
	
	public void registerAbility(Ability ability) {
		this.abilities.put(ability.getIdentifier(), ability);
	}

	public void giveAbility(Player player, String identifier, long durationSeconds) {

		Ability ability = abilities.get(identifier);

		if (ability == null) {
			throw new IllegalArgumentException("Tried to give unregistered ability " + identifier + " to a player");
		}

		final long now = new Date().getTime();
		AtomicBoolean renewed = new AtomicBoolean(false);
		getStore(player).update(getStoreKey(ability) + ".endTime", (oldEndTimeString) -> {
			if (durationSeconds != -1) {
				
				if (oldEndTimeString == null) {
					setActive(getStore(player), ability);
					return Long.toString(now + durationSeconds * 1000);
				} else {
					try {
						Long oldEndTime = Long.parseLong(oldEndTimeString);
						if (oldEndTime < now) {
							return Long.toString(now + durationSeconds * 1000);
						} else {
							renewed.set(true);
							return Long.toString(oldEndTime + durationSeconds * 1000);
						}					
					} catch (NumberFormatException e) {
						return Long.toString(now + durationSeconds * 1000);
					}
				}
			}else {
				return Long.toString(-1);
			}
		});

		if (!pausedAbilities.containsEntry(player, identifier)) {
			if (!playerAbilities.containsEntry(player, identifier)) {
				playerAbilities.put(player, identifier);
				ability.giveTo(player);
			}

			if (!renewed.get()) {
				ability.onActivated(player);
			}
		}
	}

	public boolean hasAbility(Player player, String identifier) {
		return playerAbilities.containsEntry(player, identifier) || pausedAbilities.containsEntry(player, identifier);
	}

	public void pauseAbility(Player player, String identifier) {
		Ability ability = abilities.get(identifier);
		pausedAbilities.put(player, identifier);
		setInactive(getStore(player), ability);
		
		if (playerAbilities.get(player).contains(identifier)) {
			ability.removeFrom(player);
			playerAbilities.get(player).remove(identifier);
		}
	}

	public void pauseAllAbilities(Player player) {
		abilities.keySet().forEach(ability -> pauseAbility(player, ability));
	}

	public void unpauseAbility(Player player, String identifier) {
		pausedAbilities.remove(player, identifier);
		Ability ability = abilities.get(identifier);
		PlayerDataStore store = getStore(player);
		setActive(store, ability);
		if (!playerAbilities.get(player).contains(identifier)) {
			final long now = new Date().getTime();
			try {
				if (getEndTime(store, ability).get() > now || getEndTime(store, ability).get() == -1) {
					ability.giveTo(player);
					playerAbilities.get(player).add(identifier);
				}
			} catch (InterruptedException | ExecutionException e) {
				// ignore
			}
		}
	}

	public void unpauseAllAbilities(Player player) {
		pausedAbilities.removeAll(player).forEach(ability -> unpauseAbility(player, ability));
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		final long now = new Date().getTime();
		PlayerDataStore store = getStore(event.getPlayer());
		abilities.values().parallelStream().forEach((ability) -> getEndTime(store, ability).thenAccept((endTime) -> {
			if (endTime > now || endTime == -1) {
				Bukkit.getScheduler().runTask(plugin, () -> {
					if (event.getPlayer().isOnline()) {
						try {
							if (isActive(store, ability)) {
								playerAbilities.put(event.getPlayer(), ability.getIdentifier());
								ability.giveTo(event.getPlayer());
							} else {
								pausedAbilities.put(event.getPlayer(), ability.getIdentifier());
							}
						} catch (Exception e) {
							//ignore
						}
					}
				});
			} else {
				removeActiveKey(store, ability);
			}
		}));
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		playerAbilities.removeAll(event.getPlayer()).stream().map(abilities::get).forEach((ability -> ability.removeFrom(event.getPlayer())));
		pausedAbilities.removeAll(event.getPlayer());
	}

	private static CompletableFuture<Long> getEndTime(PlayerDataStore store, Ability ability) {
		return store.getAsync(getStoreKey(ability) + ".endTime").thenApply((oldEndTimeString) -> {
			try {
				return oldEndTimeString == null ? 0 : Long.parseLong(oldEndTimeString);
			} catch (NumberFormatException e) {
				return 0L;
			}
		});
	}

	private static boolean isActive(PlayerDataStore store, Ability ability) {
		try {
			String result = store.get(getStoreKey(ability) + ".active");
			if (result == null) {
				result = "false";
			}
			return Boolean.parseBoolean(result);
		} catch (Exception e) {
			throw new RuntimeException("Could not check if ability is active", e);
		}
	}
	
	private static void setActive(PlayerDataStore store, Ability ability) {
		store.put(getStoreKey(ability) + ".active", "true");
	}
	
	private static void setInactive(PlayerDataStore store, Ability ability) {
		store.put(getStoreKey(ability) + ".active", "false");
	}
	
	private static void removeActiveKey(PlayerDataStore store, Ability ability) {
		store.put(getStoreKey(ability) + ".active", null);
	}
	
	private static String getStoreKey(Ability ability) {
		return "abilitymanager." + ability.getIdentifier();
	}
	
	private static PlayerDataStore getStore(OfflinePlayer player) {
		return Bukkit.getServicesManager().getRegistration(PlayerDataStoreService.class).getProvider().getStore(player);
	}
	
	public void reset() {
		//nothing to do here
	}

}
