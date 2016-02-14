package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.timer.TimerComponent;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.util.TimeUtil;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

/**
 * Manager for timed callbacks that scripts may register using <code>rpgplus.timer.at()</code> and unregister using
 * <code>rpgplus.timer.off()</code>.
 */
public class ScriptTimedEventManager extends LuaTable implements ScriptingModule {
    public ScriptTimedEventManager(final RpgPlus plugin) {
        final TimerComponent timer = plugin.getTimerManager();

        set("at", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue world, LuaValue time, LuaValue callback) {
                int id;
                if (time.islong()) {
                    id = timer.addHandler(plugin.getServer().getWorld(world.checkjstring()), time.checklong(), asRunnable(callback));
                } else {
                    id = timer.addHandler(plugin.getServer().getWorld(world.checkjstring()), toTime(time.checkjstring()), asRunnable(callback));
                }
                return LuaValue.valueOf(id);
            }
        });

        set("off", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                timer.removeHandler(luaValue.checkint());
                return LuaValue.NIL;
            }
        });
    }

    private static Runnable asRunnable(final LuaValue luaFunction) {
        return new Runnable() {
            @Override
            public void run() {
                RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(luaFunction.checkfunction());
            }
        };
    }

    private long toTime(String string) {
        switch (string) {
            case "midnight":
                return 18_000; //0:00
            case "day":
                return 1_000; //7:00 (/time set day)
            case "night":
                return 13_000; //19:00 (/time set night)
            case "noon":
                return 6_000; //12:00
            case "morning":
                return 0; //6:00
            case "evening":
                return 11_615; //17:37
            default:
                try {
                    return TimeUtil.ticksOfTime(string);
                } catch (IllegalArgumentException e) {
                    throw new LuaError(string + " is not a valid time alias");
                }
        }
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
