package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.plugin.Plugin;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

/**
 * Lua API for using the scheduler.
 */
public class Scheduler extends LuaTable {
    public Scheduler(final Plugin plugin) {
        set("async", new OneArgFunction() {
            @Override
            public LuaValue call(final LuaValue function) {
                return new ZeroArgFunction() {
                    @Override
                    public LuaValue call() {
                        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                                function.checkfunction().invoke();
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
                return new ZeroArgFunction() {
                    @Override
                    public LuaValue call() {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                function.checkfunction().invoke();
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
                return new ZeroArgFunction() {
                    @Override
                    public LuaValue call() {
                        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                function.checkfunction().invoke();
                            }
                        }, delay.checklong());
                        return LuaValue.NIL;
                    }
                };
            }
        });

        set("repeat", new VarArgFunction() {
            @Override
            public LuaValue invoke(final Varargs varargs) {
                return new ZeroArgFunction() {
                    @Override
                    public LuaValue call() {
                        if (varargs.narg() == 2) {
                            return LuaValue.valueOf(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    varargs.checkfunction(2).invoke();
                                }
                            }, 0, varargs.checklong(1)));
                        } else if (varargs.narg() == 3) {
                            return LuaValue.valueOf(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    varargs.checkfunction(3).invoke();
                                }
                            }, varargs.checklong(1), varargs.checklong(2)));
                        } else {
                            throw new LuaError("Invalid number of arguments, expected 2 or 3.");
                        }
                    }
                };
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
}
