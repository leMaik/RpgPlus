package rpgplus;

import de.craften.plugins.rpgplus.RpgPlus;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Lua module for trading.
 */
public class trading extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue luaValue, LuaValue luaValue1) {
        return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().getTradingModule();
    }
}
