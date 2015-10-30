package de.craften.plugins.rpgplus.scripting;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.api.*;
import de.craften.plugins.rpgplus.scripting.api.storage.Storage;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ScriptingManager extends PluginComponentBase {
    private File scriptDirectory;
    private Globals globals;
    private LuaTable rpgPlusObject;
    private LuaValue schedulerModule;
    private LuaValue tradingModule;
    private LuaValue timerModule;
    private LuaValue soundModule;
    private LuaValue storageModule;

    @Override
    protected void onActivated() {
        globals = JsePlatform.standardGlobals();
        final ResourceFinder originalFinder = globals.finder;
        globals.finder = new ResourceFinder() {
            @Override
            public InputStream findResource(String s) {
                File localFile = new File(scriptDirectory, s);
                if (localFile.exists()) {
                    try {
                        return new FileInputStream(localFile);
                    } catch (FileNotFoundException ignore) {
                    }
                }
                return originalFinder.findResource(s);
            }
        };
        LuaC.install(globals);

        scriptDirectory = RpgPlus.getPlugin(RpgPlus.class).getDataFolder();
        rpgPlusObject = new RpgPlusObject(this);
        schedulerModule = new Scheduler(RpgPlus.getPlugin(RpgPlus.class));
        tradingModule = new Trading(RpgPlus.getPlugin(RpgPlus.class));
        timerModule = new ScriptTimedEventManager(RpgPlus.getPlugin(RpgPlus.class));
        soundModule = new Sound(RpgPlus.getPlugin(RpgPlus.class));
        storageModule = new Storage();
    }

    public void loadScript(File script) throws ScriptErrorException {
        try {
            LuaValue chunk = globals.loadfile(script.getAbsolutePath());
            chunk.call(script.getAbsolutePath());
        } catch (LuaError e) {
            throw new ScriptErrorException("Could not execute " + script.getPath(), e);
        }
    }

    public File getScriptDirectory() {
        return scriptDirectory;
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

    public LuaValue getTimerModule() {
        return timerModule;
    }

    public LuaValue getSoundModule() {
        return soundModule;
    }

    public LuaValue getStorageModule() {
        return storageModule;
    }
}
