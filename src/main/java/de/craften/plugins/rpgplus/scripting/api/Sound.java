package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

public class Sound extends LuaTable {

	public Sound(final Plugin plugin) {
		set("playSound", new TwoArgFunction() {

			@Override
			public LuaValue call(final LuaValue player, final LuaValue sound) {
				Player p = (Player) CoerceLuaToJava.coerce(player.checkstring(), Player.class);
				p.playSound(p.getLocation(), org.bukkit.Sound.valueOf(sound.checkjstring()), 1.0f, 1.0f);
				
				return LuaValue.NIL;
			}

		});
		
		set("playNote", new ThreeArgFunction() {
			
			@Override
			public LuaValue call(final LuaValue player, final LuaValue instrument, final LuaValue note) {
				
				Player p = (Player) CoerceLuaToJava.coerce(player.checkstring(), Player.class);
				p.playNote(p.getLocation(), Instrument.valueOf(instrument.checkjstring()), new Note(note.checkint()));
				
				return null;
			}
		});
		
	}

}
