package de.craften.plugins.rpgplus.scripting.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.LightningStrikeEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * Manager for event callbacks that scripts may register using <code>rpgplus.on()</code> and unregister using
 * <code>rpgplus.off()</code>.
 */
public class ScriptEventManager implements Listener {
    Multimap<String, LuaFunction> eventHandlers = ArrayListMultimap.create();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        callHandlers("playerJoin", event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        callHandlers("playerQuit", event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        callHandlers("playerMove", event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        callHandlers("playerDeath", event);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        callHandlers("playerRespawn", event);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        callHandlers("playerDropItem", event);
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        callHandlers("playerItemBreak", event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        callHandlers("playerInteract", event);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        callHandlers("playerKick", event);
    }

    @EventHandler
    public void onPlayerBucket(PlayerBucketEvent event) {
        callHandlers("playerBucket", event);
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        callHandlers("playerBucketFill", event);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        callHandlers("playerBucketEmpty", event);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        callHandlers("playerChat", event);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        callHandlers("playerBedEnter", event);
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        callHandlers("playerLeave", event);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        callHandlers("playerChangedWorld", event);
    }

    @EventHandler
    public void onPlayerChatTabComplete(PlayerChatTabCompleteEvent event) {
        callHandlers("playerChatTabComplete", event);
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        callHandlers("playerEditBook", event);
    }

    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        callHandlers("playerEggThrow", event);
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        callHandlers("playerExpChange", event);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        callHandlers("playerFish", event);
    }

    @EventHandler
    public void onPlayerGamemodeChange(PlayerGameModeChangeEvent event) {
        callHandlers("playerGamemodeChange", event);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        callHandlers("playerInteractAtEntity", event);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        callHandlers("playerInteractEntity", event);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        callHandlers("playerItemConsume", event);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        callHandlers("playerItemHeld", event);
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        callHandlers("playerLevelChange", event);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        callHandlers("playerLogin", event);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        callHandlers("playerPickupItem", event);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        callHandlers("playerPortal", event);
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        callHandlers("playerShearEntity", event);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        callHandlers("playerTeleport", event);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        callHandlers("playerToggleFlight", event);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        callHandlers("playerToggleSneak", event);
    }

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        callHandlers("playerToggleSprint", event);
    }

    @EventHandler
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        callHandlers("playerUnleashEntity", event);
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        callHandlers("playerVelocity", event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        callHandlers("blockBreak", event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        callHandlers("blockPlace", event);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        callHandlers("blockBurn", event);
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event) {
        callHandlers("enchant", event);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        callHandlers("creatureSpawn", event);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        callHandlers("entityDamageByEntity", event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        callHandlers("entityDamage", event);
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        callHandlers("entityDamageByBlock", event);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        callHandlers("entityDeath", event);
    }

    @EventHandler
    public void onFoodLevelChanges(FoodLevelChangeEvent event) {
        callHandlers("foodLevelChanges", event);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        callHandlers("itemSpawn", event);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        callHandlers("itemDespawn", event);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        callHandlers("projectileLaunch", event);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        callHandlers("projectileHit", event);
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        callHandlers("entityTeleport", event);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        callHandlers("entityTarget", event);
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event) {
        callHandlers("entityTame", event);
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        callHandlers("entityInteract", event);
    }

    @EventHandler
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
        callHandlers("entityPortalEnter", event);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        callHandlers("entityPortal", event);
    }

    @EventHandler
    public void onEntityPortalExit(EntityPortalExitEvent event) {
        callHandlers("entityPortalExit", event);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        callHandlers("entityRegainHealth", event);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        callHandlers("entityExplode", event);
    }

    @EventHandler
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        callHandlers("playerLeashEntity", event);
    }

    @EventHandler
    public void onEntityUnleash(EntityUnleashEvent event) {
        callHandlers("entityUnleash", event);
    }

    @EventHandler
    public void onExpBottle(ExpBottleEvent event) {
        callHandlers("expBottle", event);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        callHandlers("potionSplash", event);
    }

    @EventHandler
    public void onHorseJump(HorseJumpEvent event) {
        callHandlers("horseJump", event);
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent event) {
        callHandlers("inventoryOpen", event);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        callHandlers("inventoryClick", event);
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        callHandlers("inventoryClose", event);
    }

    @EventHandler
    public void onInvInteract(InventoryInteractEvent event) {
        callHandlers("inventoryInteract", event);
    }

    @EventHandler
    public void onInvMoveItem(InventoryMoveItemEvent event) {
        callHandlers("inventoryMoveItem", event);
    }

    @EventHandler
    public void onInvPickupItem(InventoryPickupItemEvent event) {
        callHandlers("inventoryPickupItem", event);
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent event) {
        callHandlers("inventoryDrag", event);
    }

    @EventHandler
    public void onInvCReative(InventoryCreativeEvent event) {
        callHandlers("inventoryCreative", event);
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        callHandlers("furnaceBurn", event);
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        callHandlers("furnaceExtract", event);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        callHandlers("furnaceSmelt", event);
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        callHandlers("brew", event);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        callHandlers("craftItem", event);
    }

    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        callHandlers("hangingPlace", event);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        callHandlers("hangingBreak", event);
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        callHandlers("hangingBreakByEntity", event);
    }

    @EventHandler
    public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
        callHandlers("vehicleBlockCollision", event);
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        callHandlers("vehicleCreate", event);
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        callHandlers("vehicleDamage", event);
    }

    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        callHandlers("vehicleDestroy", event);
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        callHandlers("vehicleEnter", event);
    }

    @EventHandler
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        callHandlers("vehicleEntityCollision", event);
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        callHandlers("vehicleExit", event);
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        callHandlers("vehicleMove", event);
    }

    @EventHandler
    public void onVehicleUpdate(VehicleUpdateEvent event) {
        callHandlers("vehicleUpdate", event);
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        callHandlers("lightningStrike", event);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        callHandlers("thunderChange", event);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        callHandlers("weatherChange", event);
    }

    //TODO Handle more/all events

    private void callHandlers(String eventName, Event event) {
        for (LuaFunction callback : eventHandlers.get(eventName)) {
            callback.invoke(CoerceJavaToLua.coerce(event));
        }
    }

    public void installOn(LuaTable object) {
        object.set("on", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue eventName, LuaValue callback) {
                eventHandlers.put(eventName.checkjstring(), callback.checkfunction());
                return LuaValue.NIL;
            }
        });

        object.set("off", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue eventName, LuaValue callback) {
                if (callback.isnil()) {
                    return LuaValue.valueOf(!eventHandlers.removeAll(eventName.checkjstring()).isEmpty());
                } else {
                    return LuaFunction.valueOf(eventHandlers.remove(eventName.checkjstring(), callback.checkfunction()));
                }
            }
        });
    }
}
