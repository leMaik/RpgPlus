package de.craften.plugins.rpgplus.scripting.api;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import de.craften.plugins.rpgplus.components.inventory.ItemMatcher;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;

/**
 * Lua API for the player's inventory.
 */
public class InventoryModule extends LuaTable implements ScriptingModule {
	
	
    public InventoryModule() {
        Luaify.convertInPlace(this);
    }

    @LuaFunction("hasItems")
    public LuaBoolean hasItems(Varargs args) {
        LuaValue player = args.arg(1);
        for (int i = 2; i <= args.narg(); i++) {
            if (!checkItem(player, args.arg(i)).booleanValue()) {
                return LuaValue.FALSE;
            }
        }
        return LuaValue.TRUE;
    }

    @LuaFunction("giveItems")
    public Varargs giveItems(Varargs args) {
        LuaValue player = args.arg(1);

        LuaValue[] notFittingAmounts = new LuaValue[args.narg() - 1];
        for (int i = 2; i <= args.narg(); i++) {
            notFittingAmounts[i - 2] = giveItem(player, args.arg(i));
        }

        return LuaValue.varargsOf(notFittingAmounts);
    }

    @LuaFunction("takeItems")
    public Varargs takeItems(Varargs args) {
        LuaValue player = args.arg(1);

        LuaValue[] missingAmounts = new LuaValue[args.narg() - 1];
        for (int i = 2; i <= args.narg(); i++) {
            missingAmounts[i - 2] = takeItem(player, args.arg(i));
        }

        return LuaValue.varargsOf(missingAmounts);
    }
    
    @LuaFunction("openChest")
    public Varargs openChest(Varargs args) {
    	
    	Player p = ScriptUtil.getPlayer(args.arg(1));
       	int slots = args.arg(2).optint(1)*9;
       	String title = ChatColor.translateAlternateColorCodes('&', args.arg(3).optjstring(""));
       	Inventory inv = Bukkit.createInventory(null, slots, title);
       	LuaTable itemTable = args.arg(4).opttable(null);
       	if (itemTable != null) {
       		for (int i = 0; i < slots; i++) {
       			LuaValue value = itemTable.get(i);
       			if (value != LuaValue.NIL) {
       				ItemStack item = ScriptUtil.createItemMatcher(value).getItemStack();
           			inv.setItem(i, item);
       			}
       		}
        }
       	p.openInventory(inv);
    
    	return LuaValue.NIL;
    }
    
    private static LuaBoolean checkItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = ScriptUtil.createItemMatcher(item);
        return LuaValue.valueOf(matcher.matches(ScriptUtil.getPlayer(player).getInventory()));
    }

    private static LuaInteger giveItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = ScriptUtil.createItemMatcher(item);
        return LuaValue.valueOf(matcher.putInto(ScriptUtil.getPlayer(player).getInventory()));
    }

    private static LuaInteger takeItem(LuaValue player, LuaValue item) {
        ItemMatcher matcher = ScriptUtil.createItemMatcher(item);
        return LuaValue.valueOf(matcher.takeFrom(ScriptUtil.getPlayer(player).getInventory()));
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
