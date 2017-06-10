--- Provides functions to set regions.
-- @module rpgplus.regions

--- Adds a new region.
-- @param regionID id of the region
-- @param region region table with a 'pos1' and a 'pos2'
-- @param[opt] enterHandler handler that will be executed when a player enters the region
-- @param[opt] leaveHandler handler that will be executed when a player leaves the region
--
function add(regionID, region, enterHandler, leaveHandler) end

--- Removes the region with the given id.
-- @param regionID id of the region
--
function delete(regionID) end