import de.craften.plugins.rpgplus.RpgPlus;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Lua module for the RpgPlus API.
 */
public class rpgplus extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue moduleName, LuaValue environment) {
        LuaTable library = RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().getMainModule();
        environment.set("rpgplus", library);
        return library;
    }
}
