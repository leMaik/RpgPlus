package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.enchantment.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.weather.*;
import org.bukkit.event.world.*;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Manager for event callbacks that scripts may register using <code>rpgplus.on()</code> and unregister using
 * <code>rpgplus.off()</code>.
 */
public class ScriptEventManager implements Listener {
    Multimap<String, LuaFunction> eventHandlers = ArrayListMultimap.create();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        callHandlers("player.join", event);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        callHandlers("player.join", event);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        callHandlers("player.move", event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        callHandlers("player.death", event);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        callHandlers("player.respawn", event);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        callHandlers("player.item.drop", event);
    }

    @EventHandler
    public void onPlayerItemBreak(PlayerItemBreakEvent event) {
        callHandlers("player.item.break", event);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        callHandlers("player.interact", event);
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        callHandlers("player.kick", event);
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event) {
        callHandlers("player.bucket.fill", event);
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        callHandlers("player.bucket.empty", event);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        callHandlers("player.chat", event);
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        callHandlers("player.bed.enter", event);
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        callHandlers("player.bed.leave", event);
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        callHandlers("player.changedWorld", event);
    }

    @EventHandler
    public void onPlayerChatTabComplete(PlayerChatTabCompleteEvent event) {
        callHandlers("player.chatTabComplete", event);
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event) {
        callHandlers("player.editBook", event);
    }

    @EventHandler
    public void onPlayerEggThrow(PlayerEggThrowEvent event) {
        callHandlers("player.eggThrow", event);
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        callHandlers("player.expChange", event);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        callHandlers("player.fish", event);
    }

    @EventHandler
    public void onPlayerGamemodeChange(PlayerGameModeChangeEvent event) {
        callHandlers("player.gamemode.change", event);
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        callHandlers("player.interact.atEntity", event);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        callHandlers("player.interact.entity", event);
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        callHandlers("player.item.consume", event);
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        callHandlers("player.item.held", event);
    }

    @EventHandler
    public void onPlayerLevelChange(PlayerLevelChangeEvent event) {
        callHandlers("player.levelChange", event);
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        callHandlers("player.login", event);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        callHandlers("player.item.pickup", event);
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        callHandlers("player.portal", event);
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent event) {
        callHandlers("player.shearEntity", event);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        callHandlers("player.teleport", event);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        callHandlers("player.toggle.flight", event);
    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        callHandlers("player.toggle.sneak", event);
    }

    @EventHandler
    public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
        callHandlers("player.toggle.sprint", event);
    }

    @EventHandler
    public void onPlayerUnleashEntity(PlayerUnleashEntityEvent event) {
        callHandlers("player.unleashEntity", event);
    }

    @EventHandler
    public void onPlayerVelocity(PlayerVelocityEvent event) {
        callHandlers("player.velocity", event);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        callHandlers("block.break", event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        callHandlers("block.place", event);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        callHandlers("block.burn", event);
    }
    
    @EventHandler
    public void onBlockDamage(BlockDamageEvent event){
        callHandlers("block.damage", event);
    }

    @EventHandler
    public void onBlockExp(BlockExpEvent event){
        callHandlers("block.exp", event);
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent event){
        callHandlers("block.fade", event);
    }

    @EventHandler
    public void onBlockForm(BlockFormEvent event){
        callHandlers("block.form", event);
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event){
        callHandlers("block.grow", event);
    }

    @EventHandler
    public void onBlockMultiplace(BlockMultiPlaceEvent event){
        callHandlers("block.multiplace", event);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent event){
        callHandlers("block.physics", event);
    }

    @EventHandler
    public void onBlockPiston(BlockPistonEvent event){
        callHandlers("block.piston", event);
    }

    @EventHandler
    public void onBlockPistonExtend(BlockPistonExtendEvent event){
        callHandlers("block.piston.extend", event);
    }

    @EventHandler
    public void onBlockPistonRetract(BlockPistonRetractEvent event){
        callHandlers("block.piston.retract", event);
    }

    @EventHandler
    public void onBlockRedstone(BlockRedstoneEvent event){
        callHandlers("block.redstone", event);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event){
        callHandlers("block.spread", event);
    }

    @EventHandler
    public void onBlockEntityForm(EntityBlockFormEvent event){
        callHandlers("block.form.entity", event);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event){
        callHandlers("block.leavesdecay", event);
    }

    @EventHandler
    public void onNotePlay(NotePlayEvent event){
        callHandlers("block.noteplay", event);
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event){
        callHandlers("block.signchange", event);
    }
    
    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event){
        callHandlers("block.damage", event);
    }
    
    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event){
        callHandlers("block.dispense", event);
    }
    
    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event) {
        callHandlers("player.enchant", event);
    }

    @EventHandler
    public void onPlayerPrepareEnchant(PrepareItemEnchantEvent event){
        callHandlers("player.enchant.prepare", event);
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
        callHandlers("player.command.preprocess", event);
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event){
        callHandlers("player.prelogin", event);
    }

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event){
        callHandlers("player.achievement.awarded", event);
    }

    @EventHandler
    public void onPlayerAnimation(PlayerAnimationEvent event){
        callHandlers("player.animation", event);
    }

    
    
    @EventHandler
    public void onCreeperPower(CreeperPowerEvent event) {
        callHandlers("entity.creeperpower", event);
    }

    @EventHandler
    public void onEntityBreakDoor(EntityBreakDoorEvent event) {
        callHandlers("entity.breakdoor", event);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        callHandlers("entity.changeblock", event);
    }

    @EventHandler
    public void onEntityCombustByBlock(EntityCombustByBlockEvent event) {
        callHandlers("entity.combust.byblock", event);
    }
    
    @EventHandler
    public void onEntityCombustByEntity(EntityCombustByEntityEvent event) {
        callHandlers("entity.combust.byentity", event);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        callHandlers("entity.spawn", event);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        callHandlers("entity.combust", event);
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        callHandlers("entity.damage.byEntity", event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        callHandlers("entity.damage", event);
    }

    @EventHandler
    public void onEntityDamageByBlock(EntityDamageByBlockEvent event) {
        callHandlers("entity.damage.byBlock", event);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        callHandlers("entity.death", event);
    }

    @EventHandler
    public void onFoodLevelChanges(FoodLevelChangeEvent event) {
        callHandlers("entity.foodLevelChange", event);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        callHandlers("item.spawn", event);
    }

    @EventHandler
    public void onItemDespawn(ItemDespawnEvent event) {
        callHandlers("item.despawn", event);
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        callHandlers("projectile.launch", event);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        callHandlers("projectile.hit", event);
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        callHandlers("entity.teleport", event);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        callHandlers("entity.target", event);
    }

    @EventHandler
    public void onEntityTargetLivingEntity(EntityTargetLivingEntityEvent event) {
        callHandlers("entity.target.livingentity", event);
    }

    @EventHandler
    public void onEntityTame(EntityTameEvent event) {
        callHandlers("entity.tame", event);
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        callHandlers("entity.interact", event);
    }

    @EventHandler
    public void onEntityPortalEnter(EntityPortalEnterEvent event) {
        callHandlers("entity.portal.enter", event);
    }

    @EventHandler
    public void onEntityPortal(EntityPortalEvent event) {
        callHandlers("entity.portal", event);
    }

    @EventHandler
    public void onEntityExplosionPrime(ExplosionPrimeEvent event) {
        callHandlers("entity.explosion.prime", event);
    }

    @EventHandler
    public void onEntityPortalExit(EntityPortalExitEvent event) {
        callHandlers("entity.portal.exit", event);
    }

    @EventHandler
    public void onEntityPortalCreate(EntityCreatePortalEvent event) {
        callHandlers("entity.portal.create", event);
    }

    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        callHandlers("entity.health.regain", event);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        callHandlers("entity.explode", event);
    }

    @EventHandler
    public void onPlayerLeashEntity(PlayerLeashEntityEvent event) {
        callHandlers("player.leashEntity", event);
    }

    @EventHandler
    public void onEntityUnleash(EntityUnleashEvent event) {
        callHandlers("entity.unleash", event);
    }

    @EventHandler
    public void onEntityShootBow(EntityShootBowEvent event) {
        callHandlers("entity.shootbow", event);
    }

    @EventHandler
    public void onExpBottle(ExpBottleEvent event) {
        callHandlers("entity.expBottle", event);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        callHandlers("entity.potionSplash", event);
    }

    @EventHandler
    public void onHorseJump(HorseJumpEvent event) {
        callHandlers("entity.horseJump", event);
    }

    @EventHandler
    public void onSheepRegrowWool(SheepRegrowWoolEvent event) {
        callHandlers("entity.sheep.regrowwool", event);
    }

    @EventHandler
    public void onSheepDyeWool(SheepDyeWoolEvent event) {
        callHandlers("entity.sheep.dyewool", event);
    }

    @EventHandler
    public void onSlimeSplit(SlimeSplitEvent event) {
        callHandlers("entity.slime.split", event);
    }

    @EventHandler
    public void onInvOpen(InventoryOpenEvent event) {
        callHandlers("inventory.open", event);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        callHandlers("inventory.click", event);
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        callHandlers("inventory.close", event);
    }

    @EventHandler
    public void onInvInteract(InventoryInteractEvent event) {
        callHandlers("inventory.interact", event);
    }

    @EventHandler
    public void onInvMoveItem(InventoryMoveItemEvent event) {
        callHandlers("inventory.item.move", event);
    }

    @EventHandler
    public void onInvPickupItem(InventoryPickupItemEvent event) {
        callHandlers("inventory.item.pickup", event);
    }

    @EventHandler
    public void onInvDrag(InventoryDragEvent event) {
        callHandlers("inventory.drag", event);
    }

    @EventHandler
    public void onInvCReative(InventoryCreativeEvent event) {
        callHandlers("inventory.creative", event);
    }

    @EventHandler
    public void onFurnaceBurn(FurnaceBurnEvent event) {
        callHandlers("furnace.burn", event);
    }

    @EventHandler
    public void onFurnaceExtract(FurnaceExtractEvent event) {
        callHandlers("furnace.extract", event);
    }

    @EventHandler
    public void onFurnaceSmelt(FurnaceSmeltEvent event) {
        callHandlers("furnace.smelt", event);
    }

    @EventHandler
    public void onBrew(BrewEvent event) {
        callHandlers("brew", event);
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        callHandlers("inventory.craft", event);
    }
    
    @EventHandler
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        callHandlers("inventory.craft.prepare", event);
    }
    
    @EventHandler
    public void onHangingPlace(HangingPlaceEvent event) {
        callHandlers("hanging.place", event);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        callHandlers("hanging.break", event);
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        callHandlers("hanging.break.byEntity", event);
    }

    @EventHandler
    public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
        callHandlers("vehicle.collision.block", event);
    }

    @EventHandler
    public void onVehicleCreate(VehicleCreateEvent event) {
        callHandlers("vehicle.create", event);
    }

    @EventHandler
    public void onVehicleDamage(VehicleDamageEvent event) {
        callHandlers("vehicle.damage", event);
    }

    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        callHandlers("vehicle.destroy", event);
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        callHandlers("vehicle.enter", event);
    }

    @EventHandler
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        callHandlers("vehicle.collision.entity", event);
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        callHandlers("vehicle.exit", event);
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event) {
        callHandlers("vehicle.move", event);
    }

    @EventHandler
    public void onVehicleUpdate(VehicleUpdateEvent event) {
        callHandlers("vehicle.update", event);
    }

    @EventHandler
    public void onLightningStrike(LightningStrikeEvent event) {
        callHandlers("weather.lightningStrike", event);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        callHandlers("weather.thunderChange", event);
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        callHandlers("weather.change", event);
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        callHandlers("world.chunk.load", event);
    }

    @EventHandler
    public void onChunkPopulate(ChunkPopulateEvent event) {
        callHandlers("world.chunk.populate", event);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        callHandlers("world.chunk.unload", event);
    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        callHandlers("world.createportal", event);
    }
    
    @EventHandler
    public void onSpawnChange(SpawnChangeEvent event) {
        callHandlers("world.spawnchange", event);
    }
    
    @EventHandler
    public void onWorldInit(WorldInitEvent event) {
        callHandlers("world.init", event);
    }
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        callHandlers("world.load", event);
    }
    
    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        callHandlers("world.save", event);
    }
    
    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        callHandlers("world.unload", event);
    }
    
    @EventHandler
    public void onMapInit(MapInitializeEvent event) {
        callHandlers("server.map.init", event);
    }
    
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        callHandlers("server.plugin.enable", event);
    }
    
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        callHandlers("sever.plugin.disable", event);
    }
    
    @EventHandler
    public void onRemoteServerCommand(RemoteServerCommandEvent event) {
        callHandlers("server.command.remote", event);
    }
    
    @EventHandler
    public void onServerCommand(ServerCommandEvent event) {
        callHandlers("server.command", event);
    }
    
    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        callHandlers("server.listping", event);
    }
    
    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent event) {
        callHandlers("server.service.register", event);
    }
    
    @EventHandler
    public void onServiceUnregister(ServiceUnregisterEvent event) {
        callHandlers("server.service.unregister", event);
    }
    
    //TODO Handle more/all events

    protected void callHandlers(String eventName, Event event) {
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

    /*public static void main(String[] args) throws Exception {
        //to use this program, bukkit needs to be in the classpath
        //it generates a list of all events as required by luadoc

        final AtomicReference<String> eventName = new AtomicReference<>();

        ScriptEventManager printEventManager = new ScriptEventManager() {
            @Override
            protected void callHandlers(String name, Event event) {
                eventName.set(name);
            }
        };
        for (Method method : printEventManager.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                method.invoke(printEventManager, new Object[]{null});
                System.out.printf("-- * `\"%s\"` - [%s](https://hub.spigotmc.org/javadocs/bukkit/index.html?%s.html)\n",
                        eventName.get(),
                        method.getParameterTypes()[0].getSimpleName(),
                        method.getParameterTypes()[0].getName().replace(".", "/"));
            }
        }
    }*/
}
