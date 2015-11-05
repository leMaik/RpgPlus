package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.components.inventory.ItemMatcher;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.Material;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

/**
 * Lua API for the player's inventory.
 */
public class Inventory extends LuaTable {
    public Inventory() {
        set("hasItem", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue player, LuaValue item) {
                return checkItem(player, item);
            }
        });

        set("hasItems", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue player, LuaValue items) {
                for (int i = 1; i <= items.checktable().length(); i++) {
                    if (!checkItem(player, items.get(i)).booleanValue()) {
                        return LuaValue.FALSE;
                    }
                }
                return LuaValue.TRUE;
            }
        });

        set("giveItem", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue player, LuaValue item) {
                giveItem(player, item);
                return LuaValue.NIL;
            }
        });

        set("giveItems", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue player, LuaValue items) {
                for (int i = 1; i <= items.checktable().length(); i++) {
                    giveItem(player, items.get(i));
                }
                return LuaValue.NIL;
            }
        });

        set("takeItem", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue player, LuaValue item) {
                return takeItem(player, item);
            }
        });

        set("takeItems", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                LuaValue player = args.arg(1);
                LuaValue items = args.arg(2);

                LuaValue[] missingAmounts = new LuaValue[items.length()];
                for (int i = 1; i <= items.checktable().length(); i++) {
                    missingAmounts[i - 1] = takeItem(player, items.get(i));
                }
                
                return LuaValue.varargsOf(missingAmounts);
            }
        });
    }

    private LuaBoolean checkItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = createMatcher(item.checktable());
        return LuaValue.valueOf(matcher.matches(ScriptUtil.getPlayer(player).getInventory()));
    }

    private void giveItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = createMatcher(item.checktable());
        matcher.putInto(ScriptUtil.getPlayer(player).getInventory());
    }

    private LuaInteger takeItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = createMatcher(item.checktable());
        return LuaValue.valueOf(matcher.takeFrom(ScriptUtil.getPlayer(player).getInventory()));
    }

    private ItemMatcher createMatcher(LuaTable table) {
        ItemMatcher.Builder builder = ItemMatcher.builder();
        builder.type(Material.valueOf(table.get("type").checkjstring().toUpperCase()));
        if (!table.get("data").isnil()) {
            builder.data(table.get("data").checkint());
        }
        if (!table.get("amount").isnil()) {
            builder.amount(table.get("amount").checkint());
        }
        return builder.build();
    }
}
