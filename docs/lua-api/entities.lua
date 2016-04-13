--- Provides functions to spawn entities and work with them.
-- @module rpgplus.entities

--- Registers and creates a managed entity.
-- @param type the type of the entity to spawn
-- @param options a table with options for the entity
-- @treturn Entity the spawned entity
--
function spawn(type, options) end

--- Gets the entities in the given radius near the given location. This will only get entities that were spawned
-- with @{spawn}.
-- @param location center location
-- @param radius radius around that location, in blocks
-- @treturn Entity[] list of nearby entities
--
function getNearby(location, radius) end