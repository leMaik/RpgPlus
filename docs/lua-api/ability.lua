--- Provides functions to add abilities to players
-- @module rpgplus.ability

--- Registers a new ability.
-- @param options a table with options for the ability.
--
function registerAbility(options) end

--- Gives a ability to a player.
-- @param player player
-- @param ability the identifier of the ability
-- @param duration duration
--
function giveAbility(player, ability, duration) end

--- Checks if a player has an ability.
-- @param player player
-- @param ability the identifier of the ability
-- return 'true' if the ability is present, otherwise 'false'
--
function hasAbility(player, ability) end

--- Pauses an ability.
-- @param player player
-- @param ability the identifier of the ability
--
function pauseAbility(player, ability) end

--- Unpauses an ability.
-- @param player player
-- @param ability the identifier of the ability
--
function unpauseAbility(player, ability) end

--- Pauses all abilities.
-- @param player player
--
function pauseAllAbilities(player) end

--- Unpauses all abilities.
-- @param player player
--
function unpauseAllAbilities(player) end
