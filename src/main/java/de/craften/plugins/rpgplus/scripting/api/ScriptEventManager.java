package de.craften.plugins.rpgplus.scripting.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * Manager for event callbacks that scripts may register using <code>rpgplus.on()</code> and unregister using
 * <code>rpgplus.off()</code>.
 */
public class ScriptEventManager implements Listener {
    Multimap<String, LuaFunction> eventHandlers = ArrayListMultimap.create();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        callHandlers("join", event);
    }

    //TODO Handle more/all events

    private void callHandlers(String eventName, Event event) {
        for (LuaFunction callback : eventHandlers.get(eventName)) {
            callback.invoke(CoerceJavaToLua.coerce(event));
        }
    }

    public void installOn(LuaTable object) {
        object.set("on", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue eventName, LuaValue callback) {
                eventHandlers.put(eventName.checkjstring(), callback.checkfunction());
                return LuaValue.NIL;
            }
        });

        object.set("off", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue eventName, LuaValue callback) {
                if (callback.isnil()) {
                    return LuaValue.valueOf(!eventHandlers.removeAll(eventName.checkjstring()).isEmpty());
                } else {
                    return LuaFunction.valueOf(eventHandlers.remove(eventName.checkjstring(), callback.checkfunction()));
                }
            }
        });
    }
}
