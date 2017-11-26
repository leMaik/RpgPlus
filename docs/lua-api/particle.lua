--- Provides functions to spawn particles.
-- @module rpgplus.particle

--- Spawns particles at the given location
-- @param location location
-- @param effect the [effect](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Effect.html) as string
-- @param options options table with the fields count, speed, etc.
-- @param[opt] players all players that are supposed to see the effect
function spawn(location, effect, options, players)
