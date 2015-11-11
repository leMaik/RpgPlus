--- Provides inventory functions.
-- @module rpgplus.inventory

--- Checks if the player has the given items.
-- @param player player
-- @param ... items to check for (each as an @{itemstack})
-- @return `true` if the player has the items or `false` if not
--
function hasItems(player, ...) end

--- Gives items to a player.
-- @param player player
-- @param ... items to give to the player (each as an @{itemstack})
-- @return amount of each specified item that didn't fit into the inventory, as multiple return values
--
function giveItems(player, ...) end

--- Takes items from a player. The items are taken even if they are not enough.
-- @param player player
-- @param ... items to take from the player (each as an @{itemstack})
-- @return missing amount of each specified item, as multiple return values
--
function takeItems(player, ...) end

--- A table that represents an itemstack.
-- @table itemstack
-- @field type type of the item, must be a [Bukkit Material](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html)
-- @field[opt=nil] data of the item, if `nil`, the data is ignored when matching and set to 0 when giving the item to a player
-- @field[opt=1] amount amount of the item
itemstack = {}