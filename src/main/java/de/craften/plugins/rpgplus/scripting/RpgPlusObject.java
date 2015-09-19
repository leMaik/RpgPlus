package de.craften.plugins.rpgplus.scripting;

import de.craften.plugins.rpgplus.RpgPlus;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

class RpgPlusObject extends LuaTable {

    RpgPlusObject(final RpgPlus plugin) {
        set("log", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                if (varargs.narg() >= 2) {
                    switch (varargs.checkjstring(1)) {
                        case "info":
                            plugin.getLogger().info(varargs.checkjstring(2));
                            break;
                        case "warn":
                            plugin.getLogger().warning(varargs.checkjstring(2));
                            break;
                        case "error":
                            plugin.getLogger().severe(varargs.checkjstring(2));
                            break;
                        default:
                            throw new LuaError("Invalid argument, first parameter must be info, warn or error.");
                    }
                } else {
                    plugin.getLogger().info(varargs.checkjstring(1));
                }
                return LuaValue.NIL;
            }
        });
    }
}
