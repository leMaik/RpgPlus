--- Provides sound and music functions.
-- @module rpgplus.sound

--- Play a sound to a specific player.
-- @param player player to play the sound to
-- @param sound sound to play
function playSound(player, sound) end

--- Play a note to a specific player.
-- @param player player to play the note to
-- @param instrument instrument which will playing
-- @param note note to play, either its number of a string like "c#'" or "a"
function playNote(player, instrument, note) end

--- Play a song to a specific player.
-- @param player player to play the song to
-- @param song path of the .nbs song file, relative to the script directory
-- @return true if the song was started, false if not (due to missing NoteBlockAPI plugin)
--
function playSong(player, song) end