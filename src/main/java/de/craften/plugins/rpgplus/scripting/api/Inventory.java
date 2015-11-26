package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.components.inventory.ItemMatcher;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.VarArgFunction;

/**
 * Lua API for the player's inventory.
 */
public class Inventory extends LuaTable {
    public Inventory() {
        set("hasItems", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaValue player = args.arg(1);
                for (int i = 2; i <= args.narg(); i++) {
                    if (!checkItem(player, args.arg(i)).booleanValue()) {
                        return LuaValue.FALSE;
                    }
                }
                return LuaValue.TRUE;
            }
        });

        set("giveItems", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaValue player = args.arg(1);

                LuaValue[] notFittingAmounts = new LuaValue[args.narg() - 1];
                for (int i = 2; i <= args.narg(); i++) {
                    notFittingAmounts[i - 2] = giveItem(player, args.arg(i));
                }

                return LuaValue.varargsOf(notFittingAmounts);
            }
        });

        set("takeItems", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaValue player = args.arg(1);

                LuaValue[] missingAmounts = new LuaValue[args.narg() - 1];
                for (int i = 2; i <= args.narg(); i++) {
                    missingAmounts[i - 2] = takeItem(player, args.arg(i));
                }

                return LuaValue.varargsOf(missingAmounts);
            }
        });
    }

    private static LuaBoolean checkItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = ScriptUtil.createItemMatcher(item.checktable());
        return LuaValue.valueOf(matcher.matches(ScriptUtil.getPlayer(player).getInventory()));
    }

    private static LuaInteger giveItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = ScriptUtil.createItemMatcher(item.checktable());
        return LuaValue.valueOf(matcher.putInto(ScriptUtil.getPlayer(player).getInventory()));
    }

    private static LuaInteger takeItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = ScriptUtil.createItemMatcher(item.checktable());
        return LuaValue.valueOf(matcher.takeFrom(ScriptUtil.getPlayer(player).getInventory()));
    }

}
