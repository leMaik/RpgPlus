package de.craften.plugins.rpgplus.scripting.api.inventory;

import de.craften.plugins.rpgplus.components.inventory.ItemMatcher;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.luaj.vm2.*;

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
        String title = ChatColor.translateAlternateColorCodes('&', args.arg(3).optjstring(""));
        Inventory inv = Bukkit.createInventory(null, args.arg(2).optint(1) * 9, title);

        InventoryWrapper inventoryWrapper = new InventoryWrapper(inv);
        inventoryWrapper.setItems(inventoryWrapper, args.arg(4).checktable());
        inventoryWrapper.open(inventoryWrapper, args.arg(1));

        return inventoryWrapper;
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
