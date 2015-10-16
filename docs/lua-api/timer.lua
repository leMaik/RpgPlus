--- Provides timer functions.
-- @module rpgplus.timer

--- Registers a callback that will be called whenever the time of the given world equals the given time. If the server skips
-- this exact time (due to lag or manual time change), the callback is still called.
-- @param world world to watch for the time
-- @param time when to call the callback, can be a time string (hh:mm), a shorthand (morning, midnight, ...) or a time in ticks (0..23999)
-- @param fn function to call whenever the time in the given world equals the given time
-- @return ID of this callback, can be used with @{off} to unregister this callback
--
function at(world, time, fn) end

--- Unregisters a callback that was previously registered with @{on}.
-- @param id ID of a callback
--
function off(id) end