--- Provides dialog functions.
-- @module rpgplus.dialog

--- Tell a message to a player.
-- @param entity entity that tells the message
-- @param player player to tell the message to
-- @param message message to tell, either a string or a list of alternative string to randomly choose from
-- @param[opt] ... more messages to tell, each being either a string or a list of alternative string to randomly choose from
--
function tell(entity, player, message, ...) end

--- Ask a player something. The player needs to send the answer using the chat.
-- @param entity entity that asks
-- @param player player to ask
-- @param question question, either a string or a list of alternative string to randomly choose from
-- @param callback callback that gets invoked with the response and a function to respond easily, `respond(message)`.
-- If the callback returns `false`, the question will be posed again
--
function ask(entity, player, question, callback) end