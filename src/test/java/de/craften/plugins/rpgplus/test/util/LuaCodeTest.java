package de.craften.plugins.rpgplus.test.util;

import org.junit.Before;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.JsePlatform;

/**
 * A test that tests lua code.
 */
public class LuaCodeTest {
    private Globals globals;

    @Before
    public void setup() {
        globals = JsePlatform.standardGlobals();
        LuaC.install(globals);
    }

    public LuaValue executeLua(String luaCode) {
        return globals.load(luaCode).call();
    }

    public void setLuaVariable(String variable, LuaValue value) {
        globals.set(variable, value);
    }

    public LuaValue getLuaVariable(String variable) {
        return globals.get(variable);
    }
}
