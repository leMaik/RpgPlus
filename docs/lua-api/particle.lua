--- Provides functions to spawn particles.
-- @module rpgplus.particle

--- Spawns particles at the given location
-- @param location location
-- @param effect effect as string. Can be every value of https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Effect.html
-- @param options options table with the fields count, speed, etc.
function spawn(location, effect, options)
