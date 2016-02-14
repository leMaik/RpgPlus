package de.craften.plugins.rpgplus.components.songplayer;

import com.xxmicloxx.NoteBlockAPI.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;

/**
 * A wrapper for {@link com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain}. Note that using this class will throw
 * {@link ClassNotFoundException}s if the NoteBlockAPI plugin is not available.
 */
public class SongPlayerWrapper {
    /**
     * Starts playing the given song to the given player.
     *
     * @param songFile file of the song to play
     * @param player   player to play the song to
     */
    public static void startPlaySong(File songFile, Player player) {
        Song song = NBSDecoder.parse(songFile);
        SongPlayer songPlayer = new RadioSongPlayer(song);
        songPlayer.setAutoDestroy(true);
        songPlayer.addPlayer(player);
        songPlayer.setPlaying(true);
    }

    /**
     * Stops all songs that are currently playing.
     */
    public static void stopAllSongs() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            NoteBlockPlayerMain.stopPlaying(player);
        }
    }
}
