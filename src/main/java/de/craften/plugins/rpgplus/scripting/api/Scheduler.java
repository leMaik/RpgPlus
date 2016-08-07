package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.bukkit.plugin.Plugin;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.HashSet;
import java.util.Set;

/**
 * Lua API for using the scheduler.
 */
public class Scheduler extends LuaTable implements ScriptingModule {
    private final Plugin plugin;
    private final Set<Integer> repeatingTasks = new HashSet<>();

    public Scheduler(final Plugin plugin) {
        this.plugin = plugin;
        Luaify.convertInPlace(this);
    }

    @LuaFunction("async")
    public VarArgFunction async(final LuaValue function) {
        return new VarArgFunction() {
            @Override
            public LuaValue invoke(final Varargs varargs) {
                plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(function.checkfunction(), varargs);
                    }
                });
                return LuaValue.NIL;
            }
        };
    }

    @LuaFunction("sync")
    public VarArgFunction sync(final LuaValue function) {
        return new VarArgFunction() {
            @Override
            public LuaValue invoke(final Varargs varargs) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(function.checkfunction(), varargs);
                    }
                });
                return LuaValue.NIL;
            }
        };
    }

    @LuaFunction("delay")
    public LuaValue delay(final LuaValue delay, final LuaValue function) {
        int task = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(function.checkfunction());
            }
        }, delay.checklong());
        return LuaValue.valueOf(task);
    }

    @LuaFunction("delayed")
    public LuaValue delayed(final LuaValue delay, final LuaValue function) {
        return new VarArgFunction() {
            @Override
            public LuaValue invoke(final Varargs varargs) {
                int task = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(function.checkfunction(), varargs);
                    }
                }, delay.checklong());
                return LuaValue.valueOf(task);
            }
        };
    }

    @LuaFunction("repeating")
    public LuaValue repeating(final Varargs varargs) {
        if (varargs.narg() == 2) {
            final int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(varargs.checkfunction(2));
                }
            }, 0, varargs.checklong(1));
            repeatingTasks.add(task);
            return LuaValue.valueOf(task);
        } else if (varargs.narg() == 3) {
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(varargs.checkfunction(3));
                }
            }, varargs.checklong(1), varargs.checklong(2));
            repeatingTasks.add(task);
            return LuaValue.valueOf(task);
        } else {
            throw new LuaError("Invalid number of arguments, expected 2 or 3.");
        }
    }

    @LuaFunction("repeatingAsync")
    public LuaValue repeatAsync(final Varargs varargs) {
        if (varargs.narg() == 2) {
            int task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(varargs.checkfunction(2));
                }
            }, 0, varargs.checklong(1)).getTaskId();
            repeatingTasks.add(task);
            return LuaValue.valueOf(task);
        } else if (varargs.narg() == 3) {
            int task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
                @Override
                public void run() {
                    RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(varargs.checkfunction(3));
                }
            }, varargs.checklong(1), varargs.checklong(2)).getTaskId();
            repeatingTasks.add(task);
            return LuaValue.valueOf(task);
        } else {
            throw new LuaError("Invalid number of arguments, expected 2 or 3.");
        }
    }

    @LuaFunction("cancel")
    public void cancel(LuaValue luaValue) {
        int task = luaValue.checkint();
        plugin.getServer().getScheduler().cancelTask(task);
        repeatingTasks.remove(task);
    }

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        for (int task : repeatingTasks) {
            plugin.getServer().getScheduler().cancelTask(task);
        }
        repeatingTasks.clear();
    }
}
