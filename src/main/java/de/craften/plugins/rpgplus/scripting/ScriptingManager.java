package de.craften.plugins.rpgplus.scripting;

import de.craften.plugins.rpgplus.scripting.api.RpgPlusObject;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;

public class ScriptingManager extends PluginComponentBase {
    private Globals globals;
    private LuaTable rpgPlusObject;

    @Override
    protected void onActivated() {
        globals = JsePlatform.standardGlobals();
        LuaC.install(globals);

        rpgPlusObject = new RpgPlusObject(this);
    }

    public void loadScript(File script) throws ScriptErrorException {
        try {
            LuaValue chunk = globals.loadfile(script.getAbsolutePath());
            chunk.call(script.getAbsolutePath());
        } catch (LuaError e) {
            throw new ScriptErrorException("Could not execute " + script.getPath(), e);
        }
    }

    public LuaTable getMainLibrary() {
        return rpgPlusObject;
    }
}
