--- Provides scheduler functions.
-- @module rpgplus.scheduler

--- Creates a new function that will call the given function asynchronously when invoked.
-- Parameters are passed through.
-- @param fn function that should be called asynchronously
-- @return a new function
--
function async(fn) end

--- Creates a new function that will run the given function in the Bukkit main thread when invoked.
-- Parameters are passed througgh. This function only needs to be used inside asynchronous code.
-- @param fn  function that should run in the main thread
--
function sync(fn) end

--- Calls a function after a given delay.
-- @param delay delay in ticks
-- @param fn function to call
--
function delay(delay, fn) end

--- Creates a new function that will invoke the given function after the given delay when called.
-- Parameters are passed through.
-- @param delay delay in ticks
-- @param fn  function to call
--
function delayed(delay, fn) end

--- Starts to periodically call the given function after the given delay with the given period.
-- @param[opt=0] delay delay before first execution in ticks
-- @param period period in ticks
-- @param fn function to call periodically
-- @return a task ID that can be used by @{cancel} to cancel the calls
--
function repeating(delay, period, fn) end

--- Cancels the task with the given ID.
-- @param taskId task ID
--
function cancel(taskId) end