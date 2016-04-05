package de.craften.plugins.rpgplus.scripting.api.events;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.craften.plugins.rpgplus.scripting.util.SafeInvoker;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.bukkit.event.Event;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Collection;

/**
 * The internal implementation of the {@link ScriptEventManager}. Used to separate the huge number of listener
 * functions from this code.
 */
public class ScriptEventManagerImpl {
    private Multimap<String, LuaFunction> eventHandlers = ArrayListMultimap.create();
    private final SafeInvoker invoker;

    public ScriptEventManagerImpl(SafeInvoker invoker) {
        this.invoker = invoker;
    }

    protected void callHandlers(String eventName, Event event) {
        Collection<LuaFunction> callbacks = eventHandlers.get(eventName);
        for (LuaFunction callback : callbacks.toArray(new LuaFunction[callbacks.size()])) {
            invoker.invokeSafely(callback, CoerceJavaToLua.coerce(event));
        }
    }

    public void installOn(LuaTable object) {
        Luaify.convert(this, object);
    }

    @de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("on")
    public void on(LuaValue eventName, LuaValue callback) {
        eventHandlers.put(eventName.checkjstring(), callback.checkfunction());
    }

    @de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("once")
    public void once(final LuaValue eventName, final LuaValue callback) {
        eventHandlers.put(eventName.checkjstring(), new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                off(eventName, this);
                return callback.invoke(args);
            }
        });
    }

    @de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("off")
    public LuaValue off(LuaValue eventName, LuaValue callback) {
        if (callback.isnil()) {
            return LuaValue.valueOf(!eventHandlers.removeAll(eventName.checkjstring()).isEmpty());
        } else {
            return LuaFunction.valueOf(eventHandlers.remove(eventName.checkjstring(), callback.checkfunction()));
        }
    }

    public void reset() {
        eventHandlers.clear();
    }
}
