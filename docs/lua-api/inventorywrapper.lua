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


--- Add a callback for the given event.
-- @param event event to add a handler to, possible events are:
--
-- * `"click"` - [InventoryClickEvent](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/inventory/InventoryClickEvent.html)
-- * `"close"` - [InventoryCloseEvent](https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/event/inventory/InventoryCloseEvent.html)
--
-- @param handler a handler function that is invoked whenever the event happens, gets the event as first parameter
--
function on(event, handler) end

--- Register an event handler that is only called once.
-- @param event event to add a one-time handler to
-- @param handler a handler function that is invoked whenever the event happens, gets the event as first parameter
--
function once(event, handler) end

--- Remove an event handler.
-- @param[opt] event event to remove an event handler from; if not specified, all event handlers of this inventory will be removed
-- @param[optchain] handler the handler function to remove as previously added with @{on}; if not specified, all handlers of this event will be removed
-- @return `true` if any event handler was removed, `false` if not
--
function off(event, handler) end