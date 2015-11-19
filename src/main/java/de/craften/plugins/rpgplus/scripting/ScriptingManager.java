package de.craften.plugins.rpgplus.scripting;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.api.*;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.api.images.Image;
import de.craften.plugins.rpgplus.scripting.api.storage.Storage;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class ScriptingManager extends PluginComponentBase {
    private File scriptDirectory;
    private Globals globals;
    private LuaTable rpgPlusObject;
    private LuaValue schedulerModule;
    private LuaValue tradingModule;
    private LuaValue timerModule;
    private LuaValue soundModule;
    private LuaValue storageModule;
    private EntityEventManager entityEventManager;
    private Inventory inventoryModule;
    private Image imageModule;

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

        RpgPlus plugin = RpgPlus.getPlugin(RpgPlus.class);
        scriptDirectory = plugin.getDataFolder();
        rpgPlusObject = new RpgPlusObject(this);
        schedulerModule = new Scheduler(plugin);
        tradingModule = new Trading(plugin);
        timerModule = new ScriptTimedEventManager(plugin);
        soundModule = new Sound(plugin);
        storageModule = new Storage();
        entityEventManager = new EntityEventManager();
        plugin.getServer().getPluginManager().registerEvents(entityEventManager, plugin);
        inventoryModule = new Inventory();
        imageModule = new Image(plugin);
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

    private void reportScriptError(Exception exception) {
        Bukkit.getServer().broadcast("rpgplus.scripting.notifyerrors", "An error occurred while executing the script: " + exception.getMessage());
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

    public EntityEventManager getEntityEventManager() {
        return entityEventManager;
    }

    public Inventory getInventoryModule() {
        return inventoryModule;
    }

    public Image getImageModule() {
        return imageModule;
    }
}
