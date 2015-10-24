package rpgplus;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import de.craften.plugins.rpgplus.RpgPlus;

/**
 * Lua module for playing sounds.
 */
public class sound extends TwoArgFunction{

	@Override
	public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
		return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().getSoundModule();
	}

}
