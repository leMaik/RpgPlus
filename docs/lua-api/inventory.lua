--- Provides inventory functions.
-- @module rpgplus.inventory

--- Checks if the player has the given items.
-- @param player player
-- @param ... items to check for (each as a table with `type`, optional `data` and optional `amount`)
-- @return `true` if the player has the items or `false` if not
--
function hasItems(player, ...) end

--- Gives items to a player.
-- @param player player
-- @param ... items to give to the player
--
function giveItems(player, ...) end

--- Takes items from a player. The items are taken even if they are not enough.
-- @param player player
-- @param ... items to take from the player
-- @return missing amount of each specified item, as multiple return values
--
function takeItems(player, ...) end
