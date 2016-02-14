package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import org.bukkit.plugin.Plugin;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

/**
 * Lua API for using the scheduler.
 */
public class Scheduler extends LuaTable implements ScriptingModule {
    public Scheduler(final Plugin plugin) {
        set("async", new OneArgFunction() {
            @Override
            public LuaValue call(final LuaValue function) {
                return new VarArgFunction() {
                    @Override
                    public LuaValue invoke(final Varargs varargs) {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                                RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(function.checkfunction(), varargs);
                            }
                        });
                        return LuaValue.NIL;
                    }
                };
            }
        });

        set("sync", new OneArgFunction() {
            @Override
            public LuaValue call(final LuaValue function) {
                return new VarArgFunction() {
                    @Override
                    public LuaValue invoke(final Varargs varargs) {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(function.checkfunction(), varargs);
                            }
                        });
                        return LuaValue.NIL;
                    }
                };
            }
        });

        set("delay", new TwoArgFunction() {
            @Override
            public LuaValue call(final LuaValue delay, final LuaValue function) {
                return LuaValue.valueOf(plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                    @Override
                    public void run() {
                        RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(function.checkfunction());
                    }
                }, delay.checklong()));
            }
        });

        set("delayed", new TwoArgFunction() {
            @Override
            public LuaValue call(final LuaValue delay, final LuaValue function) {
                return new VarArgFunction() {
                    @Override
                    public LuaValue invoke(final Varargs varargs) {
                        return LuaValue.valueOf(plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(function.checkfunction(), varargs);
                            }
                        }, delay.checklong()));
                    }
                };
            }
        });

        set("repeat", new VarArgFunction() {
            @Override
            public LuaValue invoke(final Varargs varargs) {
                if (varargs.narg() == 2) {
                    return LuaValue.valueOf(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(varargs.checkfunction(2));
                        }
                    }, 0, varargs.checklong(1)));
                } else if (varargs.narg() == 3) {
                    return LuaValue.valueOf(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(varargs.checkfunction(3));
                        }
                    }, varargs.checklong(1), varargs.checklong(2)));
                } else {
                    throw new LuaError("Invalid number of arguments, expected 2 or 3.");
                }
            }
        });

        set("cancel", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue luaValue) {
                plugin.getServer().getScheduler().cancelTask(luaValue.checkint());
                return LuaValue.NIL;
            }
        });
    }

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        //TODO reset repeating tasks
    }
}
