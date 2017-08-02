--- Provides functions to support bungeecord. Must be enabled in the config (just add "bungeecord: true")
-- @module rpgplus.bungeecord

--- Sends the message to the given player.
-- @param playerName player
-- @param message message
function sendMessage(playerName, message)

--- Adds a plugin message listener.
-- @param listener listener function. Takes as args: player, subchannel, byteArrayDataInput
function onMessageReceive(listener)

--- Kicks the player with the given message.
-- @param playerName player
-- @param message kick message
function kickPlayer(playerName, message)

--- Sends the given player to the given server.
-- @param playerName player
-- @param sevrer name of the target server
function connect(playerName, server)
