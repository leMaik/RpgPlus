package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.dialogs.AnswerHandler;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

/**
 * Provides an API for dialogs.
 */
public class Dialogs extends LuaTable {

    public Dialogs(final RpgPlus plugin) {
        set("ask", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                final Entity entity = (Entity) CoerceLuaToJava.coerce(varargs.arg(1), Entity.class);
                Player player = ScriptUtil.getPlayer(varargs.arg(2));
                final LuaFunction callback = varargs.checkfunction(4);
                plugin.getDialogs().ask(entity, player, varargs.checkjstring(3), new AnswerHandler() {
                    @Override
                    public void handleAnswer(final Player player, String answer) {
                        callback.invoke(LuaValue.valueOf(answer), new OneArgFunction() {
                            @Override
                            public LuaValue call(LuaValue message) {
                                plugin.getDialogs().tell(entity, player, message.checkjstring());
                                return LuaValue.NIL;
                            }
                        });
                    }
                });
                return LuaValue.NIL;
            }
        });

        set("tell", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue luaEntity, LuaValue luaPlayer, LuaValue message) {
                Entity entity = (Entity) CoerceLuaToJava.coerce(luaEntity, Entity.class);
                Player player = ScriptUtil.getPlayer(luaPlayer);
                plugin.getDialogs().tell(entity, player, message.checkjstring());
                return LuaValue.NIL;
            }
        });
    }
}
