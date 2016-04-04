--- An inventory.
-- @classmod Inventory

--- Sets the items in this inventory.
-- @param items table of items in the inventory, with slots as keys
-- @param[opt=true] clear whether the inventory should be cleared before adding the items
--
function setItems(items, clear) end

--- Sets the item in the given slot of this inventory.
-- @param slot slot
-- @param item item to put into the slot
--
function setItem(slot, item) end

--- Opens this inventory for a player.
-- @param player player
--
function open(player) end

--- Closes this inventory for all viewers.
--
function close() end