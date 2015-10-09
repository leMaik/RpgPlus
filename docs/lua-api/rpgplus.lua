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

--- Registers a command.
-- @param command the command, either a string or a list of the command and all nested commands
-- @param handler a function that handles the command, it is invoked with the sender, the command and the arguments.
-- Note that if the command is a nested command, the command parameter is the last command and the arguments are
-- anything the sender entered after that command
--
function command(command, handler) end