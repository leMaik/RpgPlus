package de.craften.plugins.rpgplus.scripting;

import de.craften.plugins.rpgplus.scripting.util.SafeInvoker;
import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ScriptingManager implements SafeInvoker {
    private final Map<String, ScriptingModule> modules;
    private final File scriptDirectory;
    private final StrictModeOption strictMode;
    private Globals globals;

    public ScriptingManager(File scriptDirectory, StrictModeOption strictMode) {
        this.scriptDirectory = scriptDirectory;
        modules = new HashMap<>();
        this.strictMode = strictMode;
        globals = createGlobals();
        LuaC.install(globals);
    }

    public void registerModule(String name, ScriptingModule module) {
        modules.put(name, module);
    }

    public ScriptingModule getModule(String name) {
        return modules.get(name);
    }

    private Globals createGlobals() {
        Globals globals = JsePlatform.standardGlobals();

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
                ScriptingModule module = getModule(args.checkjstring(1));
                if (module != null) {
                    return module.getModule();
                }
                return originalRequire.invoke(args);
            }
        });

        if (strictMode != StrictModeOption.DISABLED) {
            //Throw when using global variables
            LuaValue metaTable = globals.getmetatable();
            if (metaTable == null) {
                metaTable = new LuaTable();
            }
            if (strictMode == StrictModeOption.ERROR) {
                metaTable.rawset("__newindex", new ThreeArgFunction() {
                    @Override
                    public LuaValue call(LuaValue table, LuaValue key, LuaValue value) {
                        throw new LuaError("Undefined variable '" + key + "'. (Strict mode: global variables are disabled)");
                        //table.rawset(key, value);
                        //return NONE;
                    }
                });
            } else if (strictMode == StrictModeOption.WARN) {
                metaTable.rawset("__newindex", new ThreeArgFunction() {
                    @Override
                    public LuaValue call(LuaValue table, LuaValue key, LuaValue value) {
                        reportScriptWarning("Strict mode warning: Variable '" + key + "' is global");
                        table.rawset(key, value);
                        return NONE;
                    }
                });
            }
            globals.setmetatable(metaTable);
        }

        return globals;
    }

    /**
     * Resets the script manager.
     */
    public void reset() {
        //re-install globals to ensure that require()-d modules are also reloaded
        globals = createGlobals();
        LuaC.install(globals);

        for (ScriptingModule module : modules.values()) {
            module.reset();
        }
    }

    public Varargs executeScript(File script) throws ScriptErrorException {
        try {
            LuaValue chunk = globals.loadfile(script.getAbsolutePath());
            return invokeSafely(chunk, LuaValue.valueOf(script.getAbsolutePath()));
        } catch (LuaError e) {
            throw new ScriptErrorException("Could not execute " + script.getPath(), e);
        }
    }

    public Varargs executeScript(String script, String name) throws ScriptErrorException {
        try {
            LuaValue chunk = globals.load(script, name);
            return invokeSafely(chunk, LuaValue.valueOf(name));
        } catch (LuaError e) {
            throw new ScriptErrorException("Could not execute script " + name, e);
        }
    }

    @Override
    public Varargs invokeSafely(Callable<Varargs> callable) {
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

    @Override
    public Varargs invokeSafely(LuaValue callable, LuaValue... arguments) {
        try {
            return callable.invoke(arguments);
        } catch (Exception e) {
            reportScriptError(e);
            throw e;
        }
    }

    @Override
    public Varargs invokeSafely(LuaValue callable, Varargs arguments) {
        try {
            return callable.invoke(arguments);
        } catch (Exception e) {
            reportScriptError(e);
            throw e;
        }
    }

    /**
     * Invoked when an error occurs while running a script.
     *
     * @param exception exception that occurred, most likely a {@link LuaError}
     */
    protected void reportScriptError(final Exception exception) {
        exception.printStackTrace();
    }

    /**
     * Send a script warning.
     *
     * @param warning warning
     */
    public void reportScriptWarning(final String warning) {
        System.out.println(warning);
    }

    public File getScriptDirectory() {
        return scriptDirectory;
    }

    public enum StrictModeOption {
        DISABLED,
        WARN,
        ERROR
    }
}
