--- An entity.
-- @classmod Entity

--- Health of the entity. This field is writable.
health = _

--- Maximum health of the entity. This field is writable.
maxHealth = _

--- Name of the entity. This field is writable.
name = _

--- Second name of the entity. This field is writable.
secondName = _

--- Specifies whether the name of the entity is displayed above it. This field is writable.
nameVisible = _

--- Specifies whether the entity is invulnerable. This field is writable.
invulnerable = _

--- Only for villagers: The profession of this villager. This field is writable.
profession = _

--- Navigates to the given location, using A*.
-- @param destination destination, a table with `x`, `y` and `z` fields
-- @param[opt] options a table with options for pathing, options are `openDoors` (boolean), `openFenceGates` (boolean) and `speed` (int, in meters per second)
-- @param[opt] callback callback that is invoked when the entity has reached the destination
-- @return `true` if the navigation was possible, `false` if no path was found
--
function navigateTo(destination, options, callback) end

--- Teleports the entity to the given location.
-- @param destination destination, a table with `x`, `y` and `z` fields and an optional `world` field (defaults to current world)
function teleportTo(destination) end

--- Tell a message to a player.
-- @param player player to tell the message to
-- @param message message to tell, either a string or a list of alternative string to randomly choose from
-- @param[opt] ... more messages to tell, each being either a string or a list of alternative string to randomly choose from
--
function tell(player, message, ...) end

--- Ask a player something. The player needs to send the answer using the chat.
-- @param player player to ask
-- @param question question, either a string or a list of alternative string to randomly choose from
-- @param callback callback that gets invoked with the response and a function to respond easily, `respond(message)`.
-- If the callback returns `false`, the question will be posed again
--
function ask(player, question, callback) end

--- Despawn this entity.
function despawn() end

--- Despawn this entity with the kill animation.
function kill() end

--- Respawn this entity. If this entity is already spawned, it will be despawned first.
function respawn() end

--- Add a callback for the given event.
-- @param event event to add a handler to, possible events are:
--
-- * `"player.interact.atEntity"` - [PlayerInteractAtEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerInteractAtEntityEvent.html)
-- * `"entity.death"` - [EntityDeathEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityDeathEvent.html)
-- * `"player.interact.entity"` - [PlayerInteractEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerInteractEntityEvent.html)
-- * `"player.shearEntity"` - [PlayerShearEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerShearEntityEvent.html)
-- * `"player.unleashEntity"` - [PlayerUnleashEntityEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/player/PlayerUnleashEntityEvent.html)
-- * `"item.spawn"` - [ItemSpawnEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/ItemSpawnEvent.html)
-- * `"block.form.entity"` - [EntityBlockFormEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/block/EntityBlockFormEvent.html)
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
-- * `"entity.foodLevelChange"` - [FoodLevelChangeEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/FoodLevelChangeEvent.html)
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
-- * `"entity.shootbow"` - [EntityShootBowEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityShootBowEvent.html)
-- * `"entity.expBottle"` - [ExpBottleEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/ExpBottleEvent.html)
-- * `"entity.potionSplash"` - [PotionSplashEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/PotionSplashEvent.html)
-- * `"entity.horseJump"` - [HorseJumpEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/HorseJumpEvent.html)
-- * `"entity.sheep.regrowwool"` - [SheepRegrowWoolEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/SheepRegrowWoolEvent.html)
-- * `"entity.sheep.dyewool"` - [SheepDyeWoolEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/SheepDyeWoolEvent.html)
-- * `"entity.slime.split"` - [SlimeSplitEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/SlimeSplitEvent.html)
-- * `"vehicle.damage"` - [VehicleDamageEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleDamageEvent.html)
-- * `"vehicle.destroy"` - [VehicleDestroyEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleDestroyEvent.html)
-- * `"vehicle.enter"` - [VehicleEnterEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleEnterEvent.html)
-- * `"vehicle.collision.entity"` - [VehicleEntityCollisionEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleEntityCollisionEvent.html)
-- * `"vehicle.exit"` - [VehicleExitEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/vehicle/VehicleExitEvent.html)
--
-- @param handler a handler function that is invoked whenever the event happens, gets the event as first parameter
--
function on(event, handler) end

--- Remove an event handler.
-- @param[opt] event event to remove an event handler from; if not specified, all event handlers of this entity will be removed
-- @param[optchain] handler the handler function to remove as previously added with @{on}; if not specified, all handlers of this event will be removed
-- @return `true` if any event handler was removed, `false` if not
--
function off(event, handler) end