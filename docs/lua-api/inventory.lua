--- Provides inventory functions.
-- @module rpgplus.inventory

--- Checks if the player has the given items.
-- @param player player
-- @param ... items to check for (each as an @{rpgplus.types.itemstack} or a string of the form `id:data:amount`)
-- @return `true` if the player has the items or `false` if not
--
function hasItems(player, ...) end

--- Gives items to a player.
-- @param player player
-- @param ... items to give to the player (each as an @{rpgplus.types.itemstack} or a string of the form `id:data:amount`)
-- @return amount of each specified item that didn't fit into the inventory, as multiple return values
--
function giveItems(player, ...) end

--- Takes items from a player. The items are taken even if they are not enough.
-- @param player player
-- @param ... items to take from the player (each as an @{rpgplus.types.itemstack} or a string of the form `id:data:amount`)
-- @return missing amount of each specified item, as multiple return values
--
function takeItems(player, ...) end

--- Opens an inventory.
-- @param player player
-- @param size size of the inventory (rows of 9)
-- @param title title of the inventory
-- @param items table of items in the inventory, with slots as keys
-- @treturn Inventory inventory
--
function openChest(player, size, title, items) end
