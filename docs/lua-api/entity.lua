--- An entity.
-- @classmod Entity

--- Health of the entity.
health = _

--- Maximum health of the entity.
maxHealth = _

--- Name of the entity.
name = _

--- Second name of the entity.
secondName = _

--- Specifies whether the entity is invulnerable.
invulnerable = _

--- Specifies whether the name of the entity is displayed above it.
nameVisible = _

--- Only for villagers: The profession of this villager.
profession = _

--- Only for horses: The [style](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/entity/Horse.Style.html) of this horse.
style = _

--- Only for horses: The [variant](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/entity/Horse.Variant.html) of this horse.
variant = _

--- Only for horses: The [color](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/entity/Horse.Color.html) of this horse.
color = _

--- Only for horses: The jump strength of this horse.
jumpStrength = _

--- Only for horses: The domestication of this horse.
domestication = _

--- Only for horses: The maximum domestication of this horse.
maxDomestication = _

--- Location of the entity. This field is read-only, use `teleportTo` to teleport the entity.
location = _

--- Name of the world of the entity. This field is read-only, use `teleportTo` to teleport the entity.
worldName = _

--- Target of the entity.
target = _

--- Only for rabbits and ocelots: The [rabbit type](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/entity/Rabbit.Type.html) or [ocelot type](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/entity/Ocelot.Type.html).
type = _

--- The Bukkit entity that is wrapped by this entity. Note that this may be `nil` if the entity is not spawned. This field is read-only.
bukkitEntity = _

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
-- @param callback callback that gets invoked with the response and a function to respond easily, `respond(message)`
-- If the callback returns `false`, the question will be posed again
--
function ask(player, question, callback) end

--- Ask a player a multiple choice question. The player can answer by either clicking on a choice or by writing the index of the choice in the chat.
-- @param player player to ask
-- @param question question, either a string or a list of alternative string to randomly choose from
-- @param choices choices that the user can select, each choice may be a string or a list of alternative strings to randomly choose from
-- @param callback callback that gets invoked with the response, the one-based response index and a function to respond easily, `respond(message)`
-- If the callback returns `false`, the question will be posed again
--
function askChoices(player, question, choices, callback) end

--- Start a dialog with the given player.
-- @param player player
-- @param dialogDefinition a dialog definition
--
function startDialog(player, dialogDefinition) end

--- Despawn this entity.
function despawn() end

--- Despawn this entity with the kill animation.
function kill() end

--- Respawn this entity. If this entity is already spawned, it will be despawned first.
function respawn() end

--- Add a callback for the given event.
-- @param event event to add a handler to, possible events are:
--
-- * `"entity.click"` - [NPCClickRevent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCClickEvent.html)
-- * `"entity.click.right"` - [NPCClickRevent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCRightClickEvent.html)
-- * `"entity.click.left"` - [NPCClickRevent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCLeftClickEvent.html)
-- * `"entity.addTrait"` - [NPCAddTraitEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCAddTraitEvent.html)
-- * `"entity.collision"` - [NPCCollisionEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCCollisionEvent.html)
-- * `"entity.combust"` - [NPCCombustEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCCombustEvent.html)
-- * `"entity.combust.byBlock"` - [NPCCombustByBlockEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCCombustByBlockEvent.html)
-- * `"entity.combust.byEntity"` - [NPCCombustByEntityEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCCombustByEntityEvent.html)
-- * `"entity.create"` - [NPCCreateEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCCreateEvent.html)
-- * `"entity.damage"` - [NPCDamageEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCDamageEvent.html)
-- * `"entity.damage.byBlock"` - [NPCDamageByBlockEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCDamageByBlockEvent.html)
-- * `"entity.damage.byEntity"` - [NPCDamageByEntityEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCDamageByEntityEvent.html)
-- * `"entity.death"` - [NPCDeathEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCDeathEvent.html)
-- * `"entity.ender.teleport"` - [NPCEnderTeleportEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCEnderTeleportEvent.html)
-- * `"entity.push"` - [NPCPushEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCPushEvent.html)
-- * `"entity.remove"` - [NPCRemoveEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCRemoveEvent.html)
-- * `"entity.removeTrait"` - [NPCRemoveTraitEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCRemoveTraitEvent.html)
-- * `"entity.select"` - [NPCSelectEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCSelectEvent.html)
-- * `"entity.spawn"` - [NPCSpawnEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCSpawnEvent.html)
-- * `"entity.attachTrait"` - [NPCTraitCommandAttachEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/NPCTraitCommandAttachEvent.html)
-- * `"entity.target"` - [EntityTargetNPCEvent](http://jd.citizensnpcs.co/net/citizensnpcs/api/event/EntityTargetNPCEvent.html)
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
-- @param[opt] event event to remove an event handler from; if not specified, all event handlers of this entity will be removed
-- @param[optchain] handler the handler function to remove as previously added with @{on}; if not specified, all handlers of this event will be removed
-- @return `true` if any event handler was removed, `false` if not
--
function off(event, handler) end