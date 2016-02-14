package de.craften.plugins.rpgplus.scripting;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.ResourceFinder;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ScriptingManager {
    private final Map<String, ScriptingModule> modules;
    private final File scriptDirectory;
    private Globals globals;

    public ScriptingManager(File scriptDirectory) {
        this.scriptDirectory = scriptDirectory;
        modules = new HashMap<>();
        globals = createGlobals();
        LuaC.install(globals);
    }

    public void registerModule(String name, ScriptingModule module) {

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
                ScriptingModule module = modules.get(args.checkjstring(1));
                if (module != null) {
                    return module.getModule();
                }
                return originalRequire.invoke(args);
            }
        });

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
            return runSafely(chunk, LuaValue.valueOf(script.getAbsolutePath()));
        } catch (LuaError e) {
            throw new ScriptErrorException("Could not execute " + script.getPath(), e);
        }
    }

    public Varargs executeScript(String script, String name) throws ScriptErrorException {
        try {
            LuaValue chunk = globals.load(script, name);
            return runSafely(chunk, LuaValue.valueOf(name));
        } catch (LuaError e) {
            throw new ScriptErrorException("Could not execute script " + name, e);
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

    /**
     * Invoked when an error occurs while running a script.
     *
     * @param exception exception that occurred, most likely a {@link LuaError}
     */
    protected void reportScriptError(final Exception exception) {
        exception.printStackTrace();
    }

    public File getScriptDirectory() {
        return scriptDirectory;
    }
}
