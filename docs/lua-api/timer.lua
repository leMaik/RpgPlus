--- Provides timer functions.
-- @module rpgplus.timer

--- Registers a callback that will be called whenever the time of the given world equals the given time. If the server skips
-- this exact time (due to lag or manual time change), the callback is still called.
-- @param world world to watch for the time
-- @param time when to call the callback, can be a time string (hh:mm), a time in ticks (0..23,999) or one of the following shorthands:
--
-- * `"morning"` - 6:00, 0 ticks
-- * `"day"` - 7:00, 1,000 ticks (same as `/time set day`)
-- * `"noon"` - 12:00, 6,000 ticks
-- * `"evening"` - 17:37, 11,615 ticks
-- * `"night"` - 19:00, 13,000 ticks (same as `/time set night`)
-- * `"midnight"` - 0:00, 18,000 ticks
--
-- @param fn function to call whenever the time in the given world equals the given time
-- @return ID of this callback, can be used with @{off} to unregister this callback
--
function at(world, time, fn) end

--- Unregisters a callback that was previously registered with @{at}.
-- @param id ID of a callback
--
function off(id) end