--- An entity.
-- @classmod Entity

--- Health of the entity. This field is writable.
health = _

--- Maximum health of the entity. This field is writable.
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

--- Add a callback for the given event.
-- @param event event to add a handler to, possible events are:
--
-- * `"interact"` - [EntityInteractEvent](https://hub.spigotmc.org/javadocs/bukkit/index.html?org/bukkit/event/entity/EntityInteractEvent.html)
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