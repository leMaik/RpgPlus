package de.craften.plugins.rpgplus.scripting;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.api.RpgPlusObject;
import de.craften.plugins.rpgplus.scripting.api.Scheduler;
import de.craften.plugins.rpgplus.scripting.api.Trading;
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
    private LuaValue schedulerModule;
    private LuaValue tradingModule;

    @Override
    protected void onActivated() {
        globals = JsePlatform.standardGlobals();
        LuaC.install(globals);

        rpgPlusObject = new RpgPlusObject(this);
        schedulerModule = new Scheduler(RpgPlus.getPlugin(RpgPlus.class));
        tradingModule = new Trading(RpgPlus.getPlugin(RpgPlus.class));
    }

    public void loadScript(File script) throws ScriptErrorException {
        try {
            LuaValue chunk = globals.loadfile(script.getAbsolutePath());
            chunk.call(script.getAbsolutePath());
        } catch (LuaError e) {
            throw new ScriptErrorException("Could not execute " + script.getPath(), e);
        }
    }

    public LuaTable getMainModule() {
        return rpgPlusObject;
    }

    public LuaValue getSchedulerModule() {
        return schedulerModule;
    }

    public LuaValue getTradingModule() {
        return tradingModule;
    }
}
