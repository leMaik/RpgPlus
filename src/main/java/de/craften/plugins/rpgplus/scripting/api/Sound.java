package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.components.songplayer.SongPlayerWrapper;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.File;

public class Sound extends LuaTable {

    public Sound(final Plugin plugin) {
        set("playSound", new TwoArgFunction() {

            @Override
            public LuaValue call(final LuaValue player, final LuaValue sound) {
                Player p = plugin.getServer().getPlayer(player.checkjstring());
                p.playSound(p.getLocation(), org.bukkit.Sound.valueOf(sound.checkjstring().toUpperCase()), 1.0f, 1.0f);

                return LuaValue.NIL;
            }

        });

        set("playNote", new ThreeArgFunction() {

            @Override
            public LuaValue call(final LuaValue player, final LuaValue instrument, final LuaValue note) {
                Player p = plugin.getServer().getPlayer(player.checkjstring());
                p.playNote(p.getLocation(), Instrument.valueOf(instrument.checkjstring().toUpperCase()), new Note(note.checkint()));

                return LuaValue.NIL;
            }
        });

        set("playSong", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue player, LuaValue song) {
                if (isSongPlayerAvailable()) {
                    SongPlayerWrapper.startPlaySong(new File(plugin.getDataFolder(), song.checkjstring()), plugin.getServer().getPlayer(player.checkjstring()));
                    return LuaValue.TRUE;
                } else {
                    plugin.getLogger().warning("You need to install the NoteBlockAPI plugin to play songs.");
                    return LuaValue.FALSE;
                }
            }
        });
    }

    private boolean isSongPlayerAvailable() {
        try {
            Class.forName("com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
