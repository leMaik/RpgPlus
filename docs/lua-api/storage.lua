--- Provides a simple key-value storage.
-- @module rpgplus.storage

--- Get the value of a specific key or the default value, if the key doesn't exist.
-- @param key key
-- @param[opt=nil] defaultValue fallback value
-- @return value of the given key or the fallback value if the key doesn't exist
--
function get(key, defaultValue) end

--- Set the value of the given key. If the value is a table, subkeys will be set based on the keys of the table,
-- recursively.
-- @param key key
-- @param value value
--
function set(key, value) end

--- Get the storage of the given player.
-- @param player a player name
-- @return storage of the given player, it works like this storage but is player-specific
--
function of(player) end