package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.songplayer.SongPlayerWrapper;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sound extends LuaTable implements ScriptingModule {
    private static Pattern NOTE_PARSER = java.util.regex.Pattern.compile("([A-G])(#?)('*)");

    public Sound(final RpgPlus plugin) {
        set("playSound", new TwoArgFunction() {

            @Override
            public LuaValue call(final LuaValue player, final LuaValue sound) {
                Player p = ScriptUtil.getPlayer(player);
                p.playSound(p.getLocation(), org.bukkit.Sound.valueOf(sound.checkjstring().toUpperCase()), 1.0f, 1.0f);

                return LuaValue.NIL;
            }

        });

        set("playNote", new ThreeArgFunction() {
            @Override
            public LuaValue call(final LuaValue player, final LuaValue instrument, final LuaValue note) {
                Player p = ScriptUtil.getPlayer(player);
                if (note.isint()) {
                    p.playNote(p.getLocation(), Instrument.valueOf(instrument.checkjstring().toUpperCase()), new Note(note.checkint()));
                } else {
                    p.playNote(p.getLocation(), Instrument.valueOf(instrument.checkjstring().toUpperCase()), parseNote(note.checkjstring()));
                }
                return LuaValue.NIL;
            }
        });

        set("playSong", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue player, LuaValue song) {
                if (isSongPlayerAvailable()) {
                    SongPlayerWrapper.startPlaySong(new File(plugin.getScriptingManager().getScriptDirectory(), song.checkjstring()), ScriptUtil.getPlayer(player));
                    return LuaValue.TRUE;
                } else {
                    plugin.getLogger().warning("You need to install the NoteBlockAPI plugin to play songs.");
                    return LuaValue.FALSE;
                }
            }
        });
    }

    public static Note parseNote(String note) {
        Matcher matcher = NOTE_PARSER.matcher(note.toUpperCase());
        if (matcher.matches()) {
            Note.Tone tone = Note.Tone.valueOf(matcher.group(1));
            boolean sharped = !matcher.group(2).isEmpty();
            int octave = matcher.group(3).length();
            try {
                return new Note(octave, tone, sharped);
            } catch (IllegalArgumentException e) {
                throw new LuaError("Invalid note: " + note + ", " + e.getMessage());
            }
        }
        throw new LuaError("Invalid note: " + note);
    }

    private boolean isSongPlayerAvailable() {
        try {
            Class.forName("com.xxmicloxx.NoteBlockAPI.NoteBlockPlayerMain");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        //TODO stop all playing songs
    }
}
