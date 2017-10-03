--- Provides functions to support potion effects
-- @module rpgplus.effect

--- Gives an effect to a player
-- @param playerName player
-- @param effect effect
-- @param duration duration in ticks
-- @param amplifier amplifier
function giveEffect(playerName, effect, duration, amplifier)

--- Removes an effect from a player
-- @param playerName player
-- @param effect effect
function removeEffect(playerName, effect)

--- Checks if the player has the effect
-- @param playerName player
-- @param effect effect
-- Returns 'true' if the effect is present.
function hasEffect(playerName, effect)
