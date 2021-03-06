--- Main module for the scripting API.
-- @module rpgplus

--- Writes a message into the log.
-- @param[opt="info"] level level of the message, must be `info`, `warn` or `error`
-- @param message message to log
--
function log(level, message) end

--- **Deprecated**, use @{rpgplus.entities.spawn} instead.
--
-- Registers and creates a managed entity.
-- @param type the type of the entity to spawn
-- @param options a table with options for the entity
-- @treturn Entity the spawned entity
--
function spawn(type, options) end

--- Registers a command.
-- @param command the command, either a string or a list of the command and all nested commands
-- @param handler a function that handles the command, it is invoked with the sender, the command and the arguments.
-- Note that if the command is a nested command, the command parameter is the last command and the arguments are
-- anything the sender entered after that command
--
function command(command, handler) end

--- Registers a command that can only be executed by a player (not by the console or by a command block).
-- @param command the command, either a string or a list of the command and all nested commands
-- @param handler a function that handles the command, it is invoked with the sender, the command and the arguments.
-- Note that if the command is a nested command, the command parameter is the last command and the arguments are
-- anything the sender entered after that command
--
function playercommand(command, handler) end

--- Unregisters the given command. Also works with Minecraft/Bukkit/Spigot commands.
-- @param the command to unregister as string
function unregisterCommand(command) end

--- Sends a message to the given players.
-- @param players a list of players to send the message to, if it is only one player, no list is required
-- @param ... messages to send to the given players
--
function sendMessage(players, ...) end

--- Broadcasts a message to all players.
-- @param ... messages to send to all players
--
function broadcastMessage(...) end

--- Drops the given items at the given location.
-- @param location location to drop the items at
-- @param ... @{rpgplus.types.itemstack}s to drop
--
function dropItems(location, ...) end

--- Add an event handler.
-- @param event event to add a handler to, possible events are:
--
-- * `"player.join"` - [PlayerJoinEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerJoinEvent.html)
-- * `"player.respawn"` - [PlayerRespawnEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerRespawnEvent.html)
-- * `"player.quit"` - [PlayerQuitEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerQuitEvent.html)
-- * `"player.item.drop"` - [PlayerDropItemEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerDropItemEvent.html)
-- * `"player.death"` - [PlayerDeathEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/PlayerDeathEvent.html)
-- * `"player.item.break"` - [PlayerItemBreakEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerItemBreakEvent.html)
-- * `"player.interact"` - [PlayerInteractEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerInteractEvent.html)
-- * `"player.kick"` - [PlayerKickEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerKickEvent.html)
-- * `"player.bucket.fill"` - [PlayerBucketFillEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerBucketFillEvent.html)
-- * `"player.bucket.empty"` - [PlayerBucketEmptyEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerBucketEmptyEvent.html)
-- * `"player.chat"` - [AsyncPlayerChatEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/AsyncPlayerChatEvent.html)
-- * `"player.bed.enter"` - [PlayerBedEnterEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerBedEnterEvent.html)
-- * `"player.bed.leave"` - [PlayerBedLeaveEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerBedLeaveEvent.html)
-- * `"player.shearEntity"` - [PlayerShearEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerShearEntityEvent.html)
-- * `"player.teleport"` - [PlayerTeleportEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerTeleportEvent.html)
-- * `"player.toggle.flight"` - [PlayerToggleFlightEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerToggleFlightEvent.html)
-- * `"player.toggle.sneak"` - [PlayerToggleSneakEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerToggleSneakEvent.html)
-- * `"player.toggle.sprint"` - [PlayerToggleSprintEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerToggleSprintEvent.html)
-- * `"player.unleashEntity"` - [PlayerUnleashEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerUnleashEntityEvent.html)
-- * `"player.velocity"` - [PlayerVelocityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerVelocityEvent.html)
-- * `"block.break"` - [BlockBreakEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockBreakEvent.html)
-- * `"block.place"` - [BlockPlaceEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockPlaceEvent.html)
-- * `"block.burn"` - [BlockBurnEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockBurnEvent.html)
-- * `"block.damage"` - [BlockDamageEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockDamageEvent.html)
-- * `"block.exp"` - [BlockExpEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockExpEvent.html)
-- * `"block.fade"` - [BlockFadeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockFadeEvent.html)
-- * `"block.form"` - [BlockFormEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockFormEvent.html)
-- * `"block.grow"` - [BlockGrowEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockGrowEvent.html)
-- * `"block.multiplace"` - [BlockMultiPlaceEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockMultiPlaceEvent.html)
-- * `"block.physics"` - [BlockPhysicsEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockPhysicsEvent.html)
-- * `"block.piston"` - [BlockPistonEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockPistonEvent.html)
-- * `"block.piston.extend"` - [BlockPistonExtendEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockPistonExtendEvent.html)
-- * `"block.piston.retract"` - [BlockPistonRetractEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockPistonRetractEvent.html)
-- * `"block.redstone"` - [BlockRedstoneEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockRedstoneEvent.html)
-- * `"block.spread"` - [BlockSpreadEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockSpreadEvent.html)
-- * `"block.form.entity"` - [EntityBlockFormEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/EntityBlockFormEvent.html)
-- * `"block.leavesdecay"` - [LeavesDecayEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/LeavesDecayEvent.html)
-- * `"block.noteplay"` - [NotePlayEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/NotePlayEvent.html)
-- * `"block.signchange"` - [SignChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/SignChangeEvent.html)
-- * `"block.damage"` - [BlockIgniteEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockIgniteEvent.html)
-- * `"block.dispense"` - [BlockDispenseEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/BlockDispenseEvent.html)
-- * `"player.enchant"` - [EnchantItemEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/enchantment/EnchantItemEvent.html)
-- * `"player.enchant.prepare"` - [PrepareItemEnchantEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/enchantment/PrepareItemEnchantEvent.html)
-- * `"player.command.preprocess"` - [PlayerCommandPreprocessEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerCommandPreprocessEvent.html)
-- * `"player.prelogin"` - [AsyncPlayerPreLoginEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/AsyncPlayerPreLoginEvent.html)
-- * `"player.achievement.awarded"` - [PlayerAchievementAwardedEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerAchievementAwardedEvent.html)
-- * `"player.animation"` - [PlayerAnimationEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerAnimationEvent.html)
-- * `"entity.creeperpower"` - [CreeperPowerEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/CreeperPowerEvent.html)
-- * `"entity.breakdoor"` - [EntityBreakDoorEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityBreakDoorEvent.html)
-- * `"entity.changeblock"` - [EntityChangeBlockEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityChangeBlockEvent.html)
-- * `"entity.combust.byblock"` - [EntityCombustByBlockEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityCombustByBlockEvent.html)
-- * `"entity.combust.byentity"` - [EntityCombustByEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityCombustByEntityEvent.html)
-- * `"entity.spawn"` - [CreatureSpawnEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/CreatureSpawnEvent.html)
-- * `"entity.combust"` - [EntityCombustEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityCombustEvent.html)
-- * `"entity.damage.byEntity"` - [EntityDamageByEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityDamageByEntityEvent.html)
-- * `"entity.damage"` - [EntityDamageEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityDamageEvent.html)
-- * `"entity.damage.byBlock"` - [EntityDamageByBlockEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityDamageByBlockEvent.html)
-- * `"entity.death"` - [EntityDeathEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityDeathEvent.html)
-- * `"entity.foodLevelChange"` - [FoodLevelChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/FoodLevelChangeEvent.html)
-- * `"item.spawn"` - [ItemSpawnEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/ItemSpawnEvent.html)
-- * `"item.despawn"` - [ItemDespawnEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/ItemDespawnEvent.html)
-- * `"projectile.launch"` - [ProjectileLaunchEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/ProjectileLaunchEvent.html)
-- * `"projectile.hit"` - [ProjectileHitEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/ProjectileHitEvent.html)
-- * `"entity.teleport"` - [EntityTeleportEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityTeleportEvent.html)
-- * `"entity.target"` - [EntityTargetEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityTargetEvent.html)
-- * `"entity.target.livingentity"` - [EntityTargetLivingEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityTargetLivingEntityEvent.html)
-- * `"entity.tame"` - [EntityTameEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityTameEvent.html)
-- * `"entity.interact"` - [EntityInteractEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityInteractEvent.html)
-- * `"entity.portal.enter"` - [EntityPortalEnterEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityPortalEnterEvent.html)
-- * `"entity.portal"` - [EntityPortalEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityPortalEvent.html)
-- * `"entity.explosion.prime"` - [ExplosionPrimeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/ExplosionPrimeEvent.html)
-- * `"entity.portal.exit"` - [EntityPortalExitEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityPortalExitEvent.html)
-- * `"entity.portal.create"` - [EntityCreatePortalEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityCreatePortalEvent.html)
-- * `"entity.health.regain"` - [EntityRegainHealthEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityRegainHealthEvent.html)
-- * `"entity.explode"` - [EntityExplodeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityExplodeEvent.html)
-- * `"player.leashEntity"` - [PlayerLeashEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/PlayerLeashEntityEvent.html)
-- * `"entity.unleash"` - [EntityUnleashEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityUnleashEvent.html)
-- * `"player.move"` - [PlayerMoveEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerMoveEvent.html)
-- * `"player.changedWorld"` - [PlayerChangedWorldEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerChangedWorldEvent.html)
-- * `"player.chatTabComplete"` - [PlayerChatTabCompleteEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerChatTabCompleteEvent.html)
-- * `"player.editBook"` - [PlayerEditBookEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerEditBookEvent.html)
-- * `"player.eggThrow"` - [PlayerEggThrowEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerEggThrowEvent.html)
-- * `"player.expChange"` - [PlayerExpChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerExpChangeEvent.html)
-- * `"player.fish"` - [PlayerFishEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerFishEvent.html)
-- * `"player.gamemode.change"` - [PlayerGameModeChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerGameModeChangeEvent.html)
-- * `"player.interact.atEntity"` - [PlayerInteractAtEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerInteractAtEntityEvent.html)
-- * `"player.interact.entity"` - [PlayerInteractEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerInteractEntityEvent.html)
-- * `"player.item.consume"` - [PlayerItemConsumeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerItemConsumeEvent.html)
-- * `"player.item.held"` - [PlayerItemHeldEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerItemHeldEvent.html)
-- * `"player.levelChange"` - [PlayerLevelChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerLevelChangeEvent.html)
-- * `"player.login"` - [PlayerLoginEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerLoginEvent.html)
-- * `"player.item.pickup"` - [PlayerPickupItemEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerPickupItemEvent.html)
-- * `"player.portal"` - [PlayerPortalEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerPortalEvent.html)
-- * `"entity.shootbow"` - [EntityShootBowEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityShootBowEvent.html)
-- * `"entity.expBottle"` - [ExpBottleEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/ExpBottleEvent.html)
-- * `"entity.potionSplash"` - [PotionSplashEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/PotionSplashEvent.html)
-- * `"entity.horseJump"` - [HorseJumpEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/HorseJumpEvent.html)
-- * `"entity.sheep.regrowwool"` - [SheepRegrowWoolEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/SheepRegrowWoolEvent.html)
-- * `"entity.sheep.dyewool"` - [SheepDyeWoolEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/SheepDyeWoolEvent.html)
-- * `"entity.slime.split"` - [SlimeSplitEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/SlimeSplitEvent.html)
-- * `"inventory.open"` - [InventoryOpenEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/InventoryOpenEvent.html)
-- * `"inventory.click"` - [InventoryClickEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/InventoryClickEvent.html)
-- * `"inventory.close"` - [InventoryCloseEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/InventoryCloseEvent.html)
-- * `"inventory.interact"` - [InventoryInteractEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/InventoryInteractEvent.html)
-- * `"inventory.item.move"` - [InventoryMoveItemEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/InventoryMoveItemEvent.html)
-- * `"inventory.item.pickup"` - [InventoryPickupItemEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/InventoryPickupItemEvent.html)
-- * `"inventory.drag"` - [InventoryDragEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/InventoryDragEvent.html)
-- * `"inventory.creative"` - [InventoryCreativeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/InventoryCreativeEvent.html)
-- * `"furnace.burn"` - [FurnaceBurnEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/FurnaceBurnEvent.html)
-- * `"furnace.extract"` - [FurnaceExtractEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/FurnaceExtractEvent.html)
-- * `"furnace.smelt"` - [FurnaceSmeltEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/FurnaceSmeltEvent.html)
-- * `"brew"` - [BrewEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/BrewEvent.html)
-- * `"inventory.craft"` - [CraftItemEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/CraftItemEvent.html)
-- * `"inventory.craft.prepare"` - [PrepareItemCraftEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/inventory/PrepareItemCraftEvent.html)
-- * `"hanging.place"` - [HangingPlaceEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/hanging/HangingPlaceEvent.html)
-- * `"hanging.break"` - [HangingBreakEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/hanging/HangingBreakEvent.html)
-- * `"hanging.break.byEntity"` - [HangingBreakByEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/hanging/HangingBreakByEntityEvent.html)
-- * `"vehicle.collision.block"` - [VehicleBlockCollisionEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleBlockCollisionEvent.html)
-- * `"vehicle.create"` - [VehicleCreateEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleCreateEvent.html)
-- * `"vehicle.damage"` - [VehicleDamageEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleDamageEvent.html)
-- * `"vehicle.destroy"` - [VehicleDestroyEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleDestroyEvent.html)
-- * `"vehicle.enter"` - [VehicleEnterEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleEnterEvent.html)
-- * `"vehicle.collision.entity"` - [VehicleEntityCollisionEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleEntityCollisionEvent.html)
-- * `"vehicle.exit"` - [VehicleExitEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleExitEvent.html)
-- * `"vehicle.move"` - [VehicleMoveEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleMoveEvent.html)
-- * `"vehicle.update"` - [VehicleUpdateEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleUpdateEvent.html)
-- * `"weather.lightningStrike"` - [LightningStrikeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/weather/LightningStrikeEvent.html)
-- * `"weather.thunderChange"` - [ThunderChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/weather/ThunderChangeEvent.html)
-- * `"weather.change"` - [WeatherChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/weather/WeatherChangeEvent.html)
-- * `"world.chunk.load"` - [ChunkLoadEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/ChunkLoadEvent.html)
-- * `"world.chunk.populate"` - [ChunkPopulateEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/ChunkPopulateEvent.html)
-- * `"world.chunk.unload"` - [ChunkUnloadEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/ChunkUnloadEvent.html)
-- * `"world.createportal"` - [PortalCreateEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/PortalCreateEvent.html)
-- * `"world.spawnchange"` - [SpawnChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/SpawnChangeEvent.html)
-- * `"world.init"` - [WorldInitEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/WorldInitEvent.html)
-- * `"world.load"` - [WorldLoadEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/WorldLoadEvent.html)
-- * `"world.save"` - [WorldSaveEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/WorldSaveEvent.html)
-- * `"world.unload"` - [WorldUnloadEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/world/WorldUnloadEvent.html)
-- * `"server.map.init"` - [MapInitializeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/server/MapInitializeEvent.html)
-- * `"server.plugin.enable"` - [PluginEnableEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/server/PluginEnableEvent.html)
-- * `"sever.plugin.disable"` - [PluginDisableEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/server/PluginDisableEvent.html)
-- * `"server.command.remote"` - [RemoteServerCommandEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/server/RemoteServerCommandEvent.html)
-- * `"server.command"` - [ServerCommandEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/server/ServerCommandEvent.html)
-- * `"server.listping"` - [ServerListPingEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/server/ServerListPingEvent.html)
-- * `"server.service.register"` - [ServiceRegisterEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/server/ServiceRegisterEvent.html)
-- * `"server.service.unregister"` - [ServiceUnregisterEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/server/ServiceUnregisterEvent.html)
--
-- @param handler a handler function that is invoked whenever the event happens, gets the event as first parameter
--
function on(event, handler) end

--- Register an event handler that is only called once.
-- @param event event to add a one-time handler to
-- @param handler a handler function that is invoked whenever the event happens, gets the event as first parameter
--
function once(event, handler) end

--- Remove an event handler.
-- @param event event to remove an event handler from
-- @param[opt] handler the handler function to remove as previously added with @{on}; if not specified, all handlers of this event will be removed
-- @return `true` if any event handler was removed, `false` if not
--
function off(event, handler) end

--- Teleports a player to a location.
-- @param player player to teleport
-- @param teleport location
--
function teleport(player, location) end

--- Returns a list of all online players.
-- @return a table of all online players
--
function getOnlinePlayers() end

--- Get an online player.
-- @param the name of the player
-- @return the player
--
function getPlayer(playername) end
