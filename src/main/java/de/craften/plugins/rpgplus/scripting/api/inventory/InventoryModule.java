package de.craften.plugins.rpgplus.scripting.api.inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
import de.craften.plugins.rpgplus.scripting.api.inventory.events.InventoryEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;

/**
 * Lua API for the player's inventory.
 */
public class InventoryModule extends LuaTable implements ScriptingModule {
    private final InventoryEventManager inventoryEventManager;

    public InventoryModule(InventoryEventManager inventoryEventManager) {
        this.inventoryEventManager = inventoryEventManager;
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

    @LuaFunction("giveOrDropItems")
    public Varargs giveOrDropItems(Varargs args) {
        LuaValue player = args.arg(1);

        LuaValue[] notFittingAmounts = new LuaValue[args.narg() - 1];
        for (int i = 2; i <= args.narg(); i++) {
            int remaining = (notFittingAmounts[i - 2] = giveItem(player, args.arg(i))).checkint();
            if (remaining > 0) {
                ItemStack dropItems = ScriptUtil.createItemMatcher(args.arg(i)).getItemStack();
                dropItems.setAmount(remaining);
                Location location = ScriptUtil.getPlayer(player).getLocation();
                location.getWorld().dropItemNaturally(location, dropItems);
            }
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

    @LuaFunction("setItem")
    public void setItem(Varargs args) {
        Player player = ScriptUtil.getPlayer(args.arg(1));
        ItemMatcher matcher = ScriptUtil.createItemMatcher(args.arg(3));
        if (args.isnumber(2)) {
            int slot = args.toint(2);
            player.getInventory().setItem(slot, matcher.getItemStack());
        } else {
        	switch (args.tojstring(2)) {
			case "chestplate":
	            player.getInventory().setChestplate(matcher.getItemStack());
				break;
			case "leggings":
	            player.getInventory().setLeggings(matcher.getItemStack());
				break;
			case "boots":
	            player.getInventory().setBoots(matcher.getItemStack());
				break;
			case "helmet":
	            player.getInventory().setHelmet(matcher.getItemStack());
				break;
			case "hand":
	            player.getInventory().setItemInHand(matcher.getItemStack());
				break;

			default:
				break;
			}
        }
    }
    
    @LuaFunction("openChest")
    public Varargs openChest(Varargs args) {
        String title = ChatColor.translateAlternateColorCodes('&', args.arg(3).optjstring(""));
        Inventory inv = Bukkit.createInventory(null, args.arg(2).optint(1) * 9, title);

        InventoryWrapper inventoryWrapper = new InventoryWrapper(inv, inventoryEventManager);
        inventoryWrapper.setItems(inventoryWrapper, args.arg(4).checktable(), LuaValue.FALSE);
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
