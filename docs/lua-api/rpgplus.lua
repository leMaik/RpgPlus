--- Main module for the scripting API.
-- @module rpgplus

--- Writes a message into the log.
-- @param level optional level of the message, must be `info`, `warn` or `error`; defaults to `info`
-- @param message message to log
--
function log(level, message) end

--- Registers and creates a managed villager.
-- @param options a table with options for the villager
-- @param interact a function that is invoked when a player right-clicks the villager
--
function registerVillager(options, interact) end
