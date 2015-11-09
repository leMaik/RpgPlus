package de.craften.plugins.rpgplus.scripting.api.images;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.images.ImagesComponent;
import de.craften.plugins.rpgplus.components.images.Poster;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.entity.Damageable;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.io.File;

/**
 * Lua API for posters and images using maps and item frames.
 */
public class Images extends LuaTable {
    public Images(final RpgPlus plugin) {
        set("create", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                File image = new File(plugin.getScriptingManager().getScriptDirectory(), varargs.checkjstring(1));
                int width, height;
                final LuaFunction callback;
                if (varargs.narg() == 2) {
                    width = 1;
                    height = 1;
                    callback = varargs.checkfunction(2);
                } else {
                    width = varargs.checkint(2);
                    height = varargs.checkint(3);
                    callback = varargs.checkfunction(4);
                }
                plugin.getImages().createPoster(image, width, height, null, true, //TODO do we really need to specify a world?
                        new ImagesComponent.PosterCallback() {
                            @Override
                            public void posterCreated(Poster poster) {
                                callback.call(new PosterWrapper(poster, plugin.getImages()));
                            }

                            @Override
                            public void creationFailed(Throwable exception) {
                                callback.call(LuaValue.FALSE);
                            }
                        }
                );
                return LuaValue.NIL;
            }
        });
    }
}
