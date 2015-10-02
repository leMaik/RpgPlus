package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.common.entity.RPGVillager;
import de.craften.plugins.rpgplus.scripting.ScriptingManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.util.Vector;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class RpgPlusObject extends LuaTable {

    public RpgPlusObject(final ScriptingManager plugin) {
        set("log", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                if (varargs.narg() >= 2) {
                    switch (varargs.checkjstring(1)) {
                        case "info":
                            plugin.getLogger().info(varargs.checkjstring(2));
                            break;
                        case "warn":
                            plugin.getLogger().warning(varargs.checkjstring(2));
                            break;
                        case "error":
                            plugin.getLogger().severe(varargs.checkjstring(2));
                            break;
                        default:
                            throw new LuaError("Invalid argument, first parameter must be info, warn or error.");
                    }
                } else {
                    plugin.getLogger().info(varargs.checkjstring(1));
                }
                return LuaValue.NIL;
            }
        });

        set("registerVillager", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue options, final LuaValue interactCallback) {
                RPGVillager villager = new RPGVillager(
                        options.get("name").checkjstring(),
                        true,
                        new Location(
                                Bukkit.getWorld(options.get("world").checkjstring()),
                                options.get("x").checkdouble(),
                                options.get("y").checkdouble(),
                                options.get("z").checkdouble()),
                        new Vector(0, 0, 0), 1, true, new String[0]) {

                    @Override
                    public void onPlayerInteract(PlayerInteractEntityEvent event) {
                        interactCallback.checkfunction().invoke(CoerceJavaToLua.coerce(event));
                    }
                };
                villager.spawn();
                RpgPlus.getPlugin(RpgPlus.class).getEntityManager().registerEntity(villager);
                return LuaValue.NIL;
            }
        });

        //Functions to register and unregister event handlers
        ScriptEventManager events = new ScriptEventManager();
        plugin.registerEvents(events);
        events.installOn(this);
    }
}
