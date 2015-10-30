package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.commands.CommandHandler;
import de.craften.plugins.rpgplus.components.entitymanager.BasicManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.MovementType;
import de.craften.plugins.rpgplus.scripting.ScriptingManager;
import de.craften.plugins.rpgplus.scripting.api.entities.EntitySpawner;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.List;

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
                ManagedEntity villager = new BasicManagedEntity<Villager>(Villager.class,
                        new Location(
                                Bukkit.getWorld(options.get("world").checkjstring()),
                                options.get("x").checkdouble(),
                                options.get("y").checkdouble(),
                                options.get("z").checkdouble())) {

                    @Override
                    public void onPlayerInteract(PlayerInteractEntityEvent event) {
                        interactCallback.checkfunction().invoke(CoerceJavaToLua.coerce(event));
                    }
                };
                villager.setName(options.get("name").checkjstring());
                villager.setIsTakingDamage(false);
                villager.setMovementType(MovementType.LOCAL);
                villager.spawn();
                RpgPlus.getPlugin(RpgPlus.class).getEntityManager().registerEntity(villager);
                return LuaValue.NIL;
            }
        });

        set("command", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue command, final LuaValue handler) {
                String[] commandPath;
                if (command.istable()) {
                    commandPath = new String[command.checktable().length()];
                    for (int i = 0; i < commandPath.length; i++) {
                        commandPath[i] = command.get(i + 1).checkjstring();
                    }
                } else {
                    commandPath = new String[]{command.checkjstring()};
                }

                RpgPlus.getPlugin(RpgPlus.class).getCommandManager().registerCommand(commandPath, new CommandHandler() {
                    @Override
                    public boolean onCommand(CommandSender sender, String command, List<String> args) {
                        LuaValue[] luaArgs = new LuaValue[args.size()];
                        for (int i = 0; i < luaArgs.length; i++) {
                            luaArgs[i] = LuaValue.valueOf(args.get(i));
                        }

                        return handler
                                .invoke(CoerceJavaToLua.coerce(sender), LuaValue.valueOf(command), LuaValue.varargsOf(luaArgs))
                                .optboolean(1, true);
                    }
                });
                return LuaValue.NIL;
            }
        });

        set("playercommand", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue command, final LuaValue handler) {
                String[] commandPath;
                if (command.istable()) {
                    commandPath = new String[command.checktable().length()];
                    for (int i = 0; i < commandPath.length; i++) {
                        commandPath[i] = command.get(i + 1).checkjstring();
                    }
                } else {
                    commandPath = new String[]{command.checkjstring()};
                }

                RpgPlus.getPlugin(RpgPlus.class).getCommandManager().registerCommand(commandPath, new CommandHandler() {
                    @Override
                    public boolean onCommand(CommandSender sender, String command, List<String> args) {
                        if (sender instanceof Player) {
                            LuaValue[] luaArgs = new LuaValue[args.size()];
                            for (int i = 0; i < luaArgs.length; i++) {
                                luaArgs[i] = LuaValue.valueOf(args.get(i));
                            }

                            return handler
                                    .invoke(CoerceJavaToLua.coerce(sender), LuaValue.valueOf(command), LuaValue.varargsOf(luaArgs))
                                    .optboolean(1, true);
                        } else {
                            return false;
                        }
                    }
                });
                return LuaValue.NIL;
            }
        });

        set("sendMessage", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                if (varargs.narg() >= 2) {
                    LuaTable players;
                    if (varargs.istable(1)) {
                        players = varargs.checktable(1);
                    } else {
                        players = LuaTable.listOf(new LuaValue[]{varargs.arg(1)});
                    }
                    for (int i = 1; i <= players.length(); i++) {
                        Player p = ScriptUtil.getPlayer(players.get(i));
                        for (int j = 2; j <= varargs.narg(); j++) {
                            p.sendMessage(varargs.checkjstring(j));
                        }
                    }
                } else {
                    throw new LuaError("Invalid count of arguments. At least two arguments are required.");
                }

                return LuaValue.NIL;
            }
        });

        set("broadcastMessage", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                if (varargs.narg() >= 1) {
                    for (int i = 1; i <= varargs.narg(); i++) {
                        plugin.getServer().broadcastMessage(varargs.checkjstring(i));
                    }
                } else {
                    throw new LuaError("Invalid count of arguments. At least one argument are required.");
                }

                return LuaValue.NIL;
            }
        });

        //Functions to register and unregister event handlers
        ScriptEventManager events = new ScriptEventManager();
        plugin.registerEvents(events);
        events.installOn(this);

        EntitySpawner entitySpawner = new EntitySpawner();
        entitySpawner.installOn(this);
    }
}
