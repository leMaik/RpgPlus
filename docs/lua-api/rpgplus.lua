--- Main module for the scripting API.
-- @module rpgplus

--- Writes a message into the log.
-- @param[opt="info"] level level of the message, must be `info`, `warn` or `error`
-- @param message message to log
--
function log(level, message) end

--- Registers and creates a managed villager.
-- @param options a table with options for the villager
-- @param interact a function that is invoked when a player right-clicks the villager
--
function registerVillager(options, interact) end

--- Navigates an entity to the given location, using A*.
-- @param entity entity to navigate
-- @param destination destination, a table with `x`, `y` and `z` fields
-- @param[opt] options a table with options for pathing, options are `openDoors` (boolean), `openFenceGates` (boolean) and `speed` (int, in meters per second)
-- @param[opt] callback callback that is invoked when the entity has reached the destination
-- @return true if the navigation was possible, false if not path was found
--
function navigateTo(entity, destination, options, callback) end

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

--- Sends a message to the given players.
-- @param players a list of players to send the message to, if it is only one player, no list is required
-- @param ... messages to send to the given players
--
function sendMessage(players, ...) end

--- Broadcasts a message to all players.
-- @param ... messages to send to all players
--
function broadcastMessage(...) end