--- Provides functions to set titles and tab titles.
-- @module rpgplus.titles

--- Sets the tab titles for the given player or for all players, if none is specified.
-- @param[opt] player player
-- @param header header title (displayed above the player list)
-- @param footer footer title (displayed below the player list)
--
function setTabTitles(player, header, footer) end

--- Sets the titles for the given player.
-- @param player player
-- @param options options table contains title, subtitle, fadeIn, fadeOut, stay
--
function setTabTitles(player, options) end