package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.commands.CommandHandler;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.List;
import java.util.concurrent.Callable;

public class RpgPlusObject extends LuaTable implements ScriptingModule {

    public RpgPlusObject(final RpgPlus plugin) {
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
                    public boolean onCommand(final CommandSender sender, final String command, List<String> args) {
                        final LuaValue[] luaArgs = new LuaValue[args.size()];
                        for (int i = 0; i < luaArgs.length; i++) {
                            luaArgs[i] = LuaValue.valueOf(args.get(i));
                        }

                        return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(new Callable<Varargs>() {
                            @Override
                            public Varargs call() throws Exception {
                                return handler.invoke(CoerceJavaToLua.coerce(sender), LuaValue.valueOf(command), LuaValue.varargsOf(luaArgs));
                            }
                        }).optboolean(1, true);
                    }
                });
                return LuaValue.NIL;
            }
        });

        set("playercommand", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue command, final LuaValue handler) { //TODO this almost duplicates the command code above, fix that!
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
                    public boolean onCommand(final CommandSender sender, final String command, List<String> args) {
                        if (sender instanceof Player) {
                            final LuaValue[] luaArgs = new LuaValue[args.size()];
                            for (int i = 0; i < luaArgs.length; i++) {
                                luaArgs[i] = LuaValue.valueOf(args.get(i));
                            }

                            return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(new Callable<Varargs>() {
                                @Override
                                public Varargs call() throws Exception {
                                    return handler
                                            .invoke(CoerceJavaToLua.coerce(sender), LuaValue.valueOf(command), LuaValue.varargsOf(luaArgs));
                                }
                            }).optboolean(1, true);
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
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', varargs.checkjstring(j)));
                        }
                    }
                } else {
                    throw new LuaError("Invalid number of arguments. At least two arguments are required.");
                }

                return LuaValue.NIL;
            }
        });

        set("broadcastMessage", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                if (varargs.narg() >= 1) {
                    for (int i = 1; i <= varargs.narg(); i++) {
                        plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', varargs.checkjstring(i)));
                    }
                } else {
                    throw new LuaError("Invalid number of arguments. At least one argument is required.");
                }

                return LuaValue.NIL;
            }
        });

        set("dropItem", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                if (args.narg() >= 2) {
                    Location loc = ScriptUtil.getLocation(args.checktable(1));

                    for (int i = 2; i <= args.narg(); i++) {
                        loc.getWorld().dropItemNaturally(loc, ScriptUtil.createItemMatcher(args.checkvalue(i)).getItemStack());
                    }
                } else {
                    throw new LuaError("Invalid number of arguments. At least a location and one item are required.");
                }
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
        //nothing to do
    }
}
