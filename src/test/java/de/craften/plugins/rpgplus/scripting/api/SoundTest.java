package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.Note;
import org.junit.Test;
import org.luaj.vm2.LuaError;

import static org.junit.Assert.*;

public class SoundTest {
    @Test
    public void testParseNote() throws Exception {
        assertEquals(Note.natural(0, Note.Tone.A), Sound.parseNote("a"));
        assertEquals(Note.sharp(0, Note.Tone.D), Sound.parseNote("d#"));
        assertEquals(Note.sharp(2, Note.Tone.F), Sound.parseNote("f#''"));
    }

    @Test(expected = LuaError.class)
    public void testParseInvalidNote() throws Exception {
        Sound.parseNote("z#");
    }

    @Test(expected = LuaError.class)
    public void testParseInvalidOctave() throws Exception {
        Sound.parseNote("f#'''");
    }
}