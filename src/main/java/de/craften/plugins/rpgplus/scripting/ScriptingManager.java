package de.craften.plugins.rpgplus.scripting;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.api.*;
import de.craften.plugins.rpgplus.scripting.api.actionbar.ActionBarModule;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.api.events.ScriptEventManager;
import de.craften.plugins.rpgplus.scripting.api.images.Image;
import de.craften.plugins.rpgplus.scripting.api.storage.Storage;
import de.craften.plugins.rpgplus.scripting.util.Pastebin;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;

import org.bukkit.Bukkit;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Callable;

public class ScriptingManager extends PluginComponentBase {
    private File scriptDirectory;
    private Globals globals;
    private RpgPlusObject rpgPlusObject;
    private ScriptEventManager eventManager;
    private LuaValue schedulerModule;
    private LuaValue tradingModule;
    private ScriptTimedEventManager timerModule;
    private LuaValue soundModule;
    private LuaValue storageModule;
    private EntityEventManager entityEventManager;
    private Inventory inventoryModule;
    private Image imageModule;
    private ActionBarModule actionbarModule;
    
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
        final LuaValue originalRequire = globals.get("require");
        globals.set("require", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                switch (args.checkjstring(1)) {
                    case "rpgplus.image":
                        return imageModule;
                    case "rpgplus.inventory":
                        return inventoryModule;
                    case "rpgplus.scheduler":
                        return schedulerModule;
                    case "rpgplus.sound":
                        return soundModule;
                    case "rpgplus.storage":
                        return storageModule;
                    case "rpgplus.timer":
                        return timerModule;
                    case "rpgplus.trading":
                        return tradingModule;
                    case "rpgplus":
                        return rpgPlusObject;
                    case "rpgplus.actionbar":
                    	return actionbarModule;
                    default:
                        return originalRequire.invoke(args);
                }
            }
        });
        LuaC.install(globals);

        RpgPlus plugin = RpgPlus.getPlugin(RpgPlus.class);
        scriptDirectory = plugin.getDataFolder();

        rpgPlusObject = new RpgPlusObject(this);
        eventManager = new ScriptEventManager();
        registerEvents(eventManager);
        eventManager.installOn(rpgPlusObject);

        schedulerModule = new Scheduler(plugin);
        tradingModule = new Trading(plugin);
        timerModule = new ScriptTimedEventManager(plugin);
        soundModule = new Sound(plugin);
        storageModule = new Storage();
        entityEventManager = new EntityEventManager();
        plugin.getServer().getPluginManager().registerEvents(entityEventManager, plugin);
        inventoryModule = new Inventory();
        imageModule = new Image(plugin);
        actionbarModule = new ActionBarModule();
    }

    /**
     * Resets the script manager.
     */
    public void reset() {
        eventManager.reset();
        entityEventManager.reset();
        actionbarModule.reset();
    }

    public void loadScript(File script) throws ScriptErrorException {
        try {
            LuaValue chunk = globals.loadfile(script.getAbsolutePath());
            runSafely(chunk, LuaValue.valueOf(script.getAbsolutePath()));
        } catch (LuaError e) {
            throw new ScriptErrorException("Could not execute " + script.getPath(), e);
        }
    }

    public Varargs runSafely(Callable<Varargs> callable) {
        try {
            return callable.call();
        } catch (LuaError e) {
            reportScriptError(e);
            throw e;
        } catch (Exception e) {
            LuaError exception = new LuaError(e);
            reportScriptError(exception);
            throw exception;
        }
    }

    public Varargs runSafely(LuaValue callable, LuaValue... arguments) {
        try {
            return callable.invoke(arguments);
        } catch (Exception e) {
            reportScriptError(e);
            throw e;
        }
    }

    public Varargs runSafely(LuaValue callable, Varargs arguments) {
        try {
            return callable.invoke(arguments);
        } catch (Exception e) {
            reportScriptError(e);
            throw e;
        }
    }

    private void reportScriptError(final Exception exception) {
        Bukkit.getServer().broadcast("rpgplus.scripting.notifyerrors", "An error occurred while executing the script: " + exception.getMessage());
        Bukkit.getServer().getConsoleSender().sendMessage("An error occurred while executing the script: " + exception.getMessage());

        final String devKey = RpgPlus.getPlugin(RpgPlus.class).getConfig().getString("pastebinDevKey");
        if (devKey != null) {
            runTaskAsynchronously(new Runnable() {
                @Override
                public void run() {
                    try {
                        String pasteUrl = Pastebin.createStacktracePaste(devKey, "RpgPlus Error - " + new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), exception);
                        Bukkit.getServer().broadcast("rpgplus.scripting.notifyerrors", "The full stacktrace is available at: " + pasteUrl);
                    } catch (IOException e) {
                        RpgPlus.getPlugin(RpgPlus.class).getLogger().severe("Posting the stack trace of a script error to Pastebin failed");
                    }
                }
            });
        }
    }

    public File getScriptDirectory() {
        return scriptDirectory;
    }

    public EntityEventManager getEntityEventManager() {
        return entityEventManager;
    }
}
