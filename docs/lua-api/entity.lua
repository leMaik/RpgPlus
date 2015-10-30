--- An entity.
-- @classmod Entity

--- Navigates to the given location, using A*.
-- @param destination destination, a table with `x`, `y` and `z` fields
-- @param[opt] options a table with options for pathing, options are `openDoors` (boolean), `openFenceGates` (boolean) and `speed` (int, in meters per second)
-- @param[opt] callback callback that is invoked when the entity has reached the destination
-- @return `true` if the navigation was possible, `false` if no path was found
--
function navigateTo(destination, options, callback) end
