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
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides an API for dialogs.
 */
public class Dialogs extends LuaTable {
    private final Random random = new Random();

    public Dialogs(final RpgPlus plugin) {
        set("ask", new VarArgFunction() {
            @Override
            public Varargs invoke(final Varargs varargs) {
                final Entity entity = (Entity) CoerceLuaToJava.coerce(varargs.arg(1), Entity.class);
                final Player player = ScriptUtil.getPlayer(varargs.arg(2));
                final LuaFunction callback = varargs.checkfunction(4);
                final AtomicBoolean returnValue = new AtomicBoolean(false);
                final AtomicReference<Runnable> ask = new AtomicReference<Runnable>();
                ask.set(new Runnable() {
                    @Override
                    public void run() {
                        plugin.getDialogs().ask(entity, player, messageAlternatives(varargs.arg(3)), new AnswerHandler() {
                            @Override
                            public void handleAnswer(final Player player, String answer) {
                                Varargs handled = callback.invoke(LuaValue.valueOf(answer), new OneArgFunction() {
                                    @Override
                                    public LuaValue call(LuaValue message) {
                                        plugin.getDialogs().tell(entity, player, messageAlternatives(message));
                                        return LuaValue.NIL;
                                    }
                                });
                                if (!handled.optboolean(1, true)) {
                                    ask.get().run();
                                }
                            }
                        });
                    }
                });
                ask.get().run();
                return LuaValue.NIL;
            }
        });

        set("tell", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                final Entity entity = (Entity) CoerceLuaToJava.coerce(varargs.arg(1), Entity.class);
                Player player = ScriptUtil.getPlayer(varargs.arg(2));
                for (int i = 3; i <= varargs.narg(); i++) {
                    plugin.getDialogs().tell(entity, player, messageAlternatives(varargs.arg(i)));
                }

                return LuaValue.NIL;
            }
        });
    }

    private String messageAlternatives(LuaValue messages) {
        if (messages.istable()) {
            return messages.checktable().get(random.nextInt(messages.length()) + 1).checkjstring();
        } else {
            return messages.checkjstring();
        }
    }
}
