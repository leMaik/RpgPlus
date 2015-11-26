--- Provides trading functions.
-- @module rpgplus.trading

--- Displays a villager trading menu to a player.
-- @param player player to show the menu to
-- @param offers offers to show, each offer is an array of the form `[take, take, give]` or `[take, give]` where `take`
-- and `give` are strings of the form `id:data:amount` or are @{rpgplus.types.itemstack}s.
--
function open(player, offers) end
