--- Provides functions to set the action bar of players.
-- @module rpgplus.actionbar

--- Sets the given message as action bar for the given player.
-- @param player player
-- @param message message of the action bar, formattings are possible using `&` as escape character
-- @param[opt] duration duration, in seconds, that the action bar should be displayed - if not specified, the action bar will be displayed forever
--
function set(player, message, duration) end

--- Clears the action bar for the given player.
-- @param player player
--
function clear(player) end