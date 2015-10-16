package rpgplus;

import de.craften.plugins.rpgplus.RpgPlus;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Lua module for the timer API.
 */
public class timer extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
        return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().getTimerModule();
    }
}
