package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.commands.CommandHandler;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class RpgPlusObject extends LuaTable implements ScriptingModule {
    private final RpgPlus plugin;

    public RpgPlusObject(RpgPlus plugin) {
        this.plugin = plugin;
        Luaify.convertInPlace(this);
    }

    @LuaFunction("log")
    public void log(Varargs varargs) {
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
    }

    @LuaFunction("command")
    public void registerCommand(LuaValue command, final LuaValue handler) {
        plugin.getCommandManager().registerCommand(getCommandPath(command), new CommandHandler() {
            @Override
            public boolean onCommand(final CommandSender sender, final String command, final List<String> args) {
                return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(new Callable<Varargs>() {
                    @Override
                    public Varargs call() throws Exception {
                        return handler.invoke(CoerceJavaToLua.coerce(sender), LuaValue.valueOf(command), asVarargs(args));
                    }
                }).optboolean(1, true);
            }
        });
    }

    @LuaFunction("playercommand")
    public void registerPlayerCommand(LuaValue command, final LuaValue handler) {
        plugin.getCommandManager().registerCommand(getCommandPath(command), new CommandHandler() {
            @Override
            public boolean onCommand(final CommandSender sender, final String command, final List<String> args) {
                if (sender instanceof Player) {
                    return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(new Callable<Varargs>() {
                        @Override
                        public Varargs call() throws Exception {
                            return handler
                                    .invoke(CoerceJavaToLua.coerce(sender), LuaValue.valueOf(command), asVarargs(args));
                        }
                    }).optboolean(1, true);
                } else {
                    return false;
                }
            }
        });
    }

    @LuaFunction("unregisterCommand")
    public void unregisterCommand(LuaValue command) {
    	plugin.getCommandManager().unregisterCommand(command.checkjstring());
    }
    
    @LuaFunction("sendMessage")
    public void sendMessage(Varargs varargs) {
        if (varargs.narg() >= 2) {
            LuaTable players;
            if (varargs.istable(1)) {
                players = varargs.checktable(1);
            } else {
                players = LuaTable.listOf(new LuaValue[]{varargs.arg(1)});
            }
            for (int i = 1; i <= players.length(); i++) {
                
            	if (players.get(i).isuserdata(CommandSender.class)) {
            		for (int j = 2; j <= varargs.narg(); j++) {
                        ((CommandSender) CoerceLuaToJava.coerce(players.get(i), CommandSender.class)).sendMessage(ChatColor.translateAlternateColorCodes('&', varargs.checkjstring(j)));
                    }
            	} else {
            		Player p = ScriptUtil.getPlayer(players.get(i));
                    for (int j = 2; j <= varargs.narg(); j++) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', varargs.checkjstring(j)));
                    }
            	}
            	
            }
        } else {
            throw new LuaError("Invalid number of arguments. At least two arguments are required.");
        }
    }

    @LuaFunction("broadcastMessage")
    public void broadcastMessage(Varargs varargs) {
        if (varargs.narg() >= 1) {
            for (int i = 1; i <= varargs.narg(); i++) {
                plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', varargs.checkjstring(i)));
            }
        } else {
            throw new LuaError("Invalid number of arguments. At least one argument is required.");
        }
    }

    @LuaFunction("dropItems")
    public void dropItems(Varargs args) {
        if (args.narg() >= 2) {
            Location loc = ScriptUtil.getLocation(args.arg(1));

            for (int i = 2; i <= args.narg(); i++) {
                loc.getWorld().dropItemNaturally(loc, ScriptUtil.createItemMatcher(args.checkvalue(i)).getItemStack());
            }
        } else {
            throw new LuaError("Invalid number of arguments. At least a location and one item are required.");
        }
    }

    @LuaFunction("spawn")
    public Varargs spawn(Varargs args) {
        //TODO remove this function
        plugin.getScriptingManager().reportScriptWarning("rpgplus.spawn() is deprecated. Use the spawn() function from the rpgplus.entities module instead.");
        return plugin.getScriptingManager().getModule("rpgplus.entities").getModule().get("spawn").checkfunction().invoke(args);
    }
    
    @LuaFunction("teleport")
    public void teleport(LuaValue player, LuaValue location) {
    	ScriptUtil.getPlayer(player).teleport(ScriptUtil.getLocation(location));
    }
    
    @LuaFunction("getOnlinePlayers")
    public LuaTable getOnlinePlayers() {
    	return plugin.getServer().getOnlinePlayers().stream().map(CoerceJavaToLua::coerce).collect(ScriptUtil.asListTable());
    }
    
    @LuaFunction("getPlayer")
    public LuaValue getPlayer(LuaValue player) {
    	return CoerceJavaToLua.coerce(ScriptUtil.getPlayer(player));
    }
    
    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        //nothing to do
    }

    private static String[] getCommandPath(LuaValue command) {
        if (command.istable()) {
            String[] commandPath = new String[command.checktable().length()];
            for (int i = 0; i < commandPath.length; i++) {
                commandPath[i] = command.get(i + 1).checkjstring();
            }
            return commandPath;
        }
        return new String[]{command.checkjstring()};
    }

    private static Varargs asVarargs(List<String> args) {
        final LuaValue[] luaArgs = new LuaValue[args.size()];
        for (int i = 0; i < luaArgs.length; i++) {
            luaArgs[i] = LuaValue.valueOf(args.get(i));
        }
        return LuaValue.varargsOf(luaArgs);
    }
}
