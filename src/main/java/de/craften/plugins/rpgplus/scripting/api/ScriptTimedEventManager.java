package de.craften.plugins.rpgplus.scripting.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.timer.TimerComponent;
import org.bukkit.Bukkit;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.Iterator;
import java.util.Map;

/**
 * Manager for timed callbacks that scripts may register using <code>rpgplus.timer.at()</code> and unregister using
 * <code>rpgplus.timer.off()</code>.
 */
public class ScriptTimedEventManager extends LuaTable {
    public ScriptTimedEventManager() {
        final TimerComponent timer = RpgPlus.getPlugin(RpgPlus.class).getTimerManager();

        set("at", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue world, LuaValue time, LuaValue callback) {
                int id;
                if (time.islong()) {
                    id = timer.addHandler(Bukkit.getWorld(world.checkjstring()), time.checklong(), asRunnable(callback));
                } else {
                    id = timer.addHandler(Bukkit.getWorld(world.checkjstring()), toTime(time.checkjstring()), asRunnable(callback));
                }
                return LuaValue.valueOf(id);
            }
        });

        //TODO implement removal of timers
        /*set("off", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg2) {
                if (arg1.isfunction()) {
                    Iterator<Map.Entry<Long, LuaFunction>> iterator = callbacks.entries().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Long, LuaFunction> entry = iterator.next();
                        if (entry.getValue().equals(arg1)) {
                            iterator.remove();
                        }
                    }
                } else if (arg2.isnil()) {
                    callbacks.removeAll(arg1.checklong());
                } else {
                    callbacks.remove(arg1.checklong(), arg2.checkfunction());
                }
                return LuaValue.NIL;
            }
        });*/
    }

    private static Runnable asRunnable(final LuaValue luaFunction) {
        return new Runnable() {
            @Override
            public void run() {
                luaFunction.checkfunction().invoke();
            }
        };
    }

    private long toTime(String string) {
        switch (string) {
            case "midnight":
                return 0;
            //TODO add more time aliases
            default:
                throw new LuaError(string + " is not a valid time alias");
        }
    }
}
