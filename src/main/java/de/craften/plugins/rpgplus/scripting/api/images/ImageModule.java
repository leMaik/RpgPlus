package de.craften.plugins.rpgplus.scripting.api.images;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.io.File;

/**
 * Lua API for posters and images using maps and item frames.
 */
public class ImageModule extends LuaTable implements ScriptingModule {
    public ImageModule(final RpgPlus plugin) {
        set("create", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue path, LuaValue width, LuaValue height) {
                File image = new File(plugin.getScriptingManager().getScriptDirectory(), path.checkjstring());
                return new PosterWrapper(image, width.optint(1), height.optint(1), plugin.getImages());
            }
        });
    }

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        //nothing to do
    }
}