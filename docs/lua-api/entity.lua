--- An entity.
-- @classmod Entity

--- health of the entity
health = _

--- maximum health of the entity
maxHealth = _

--- Navigates to the given location, using A*.
-- @param destination destination, a table with `x`, `y` and `z` fields
-- @param[opt] options a table with options for pathing, options are `openDoors` (boolean), `openFenceGates` (boolean) and `speed` (int, in meters per second)
-- @param[opt] callback callback that is invoked when the entity has reached the destination
-- @return `true` if the navigation was possible, `false` if no path was found
--
function navigateTo(destination, options, callback) end

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
