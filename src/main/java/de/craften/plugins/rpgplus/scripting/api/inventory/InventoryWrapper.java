package de.craften.plugins.rpgplus.scripting.api.inventory;

import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;

/**
 * A Lua wrapper for inventories.
 */
public class InventoryWrapper extends LuaTable {
    private final Inventory inventory;

    public InventoryWrapper(Inventory inventory) {
        this.inventory = inventory;
        Luaify.convertInPlace(this);
    }

    @LuaFunction("setItems")
    public void setItems(LuaValue inv, LuaTable items, LuaValue clear) {
        if (clear.optboolean(true)) {
            inventory.clear();
        }

        for (LuaValue slot : items.keys()) {
            ItemStack item = ScriptUtil.createItemMatcher(items.get(slot)).getItemStack();
            inventory.setItem(slot.checkint(), item);
        }

        //re-open inventory to update it
        for (HumanEntity viewer : new ArrayList<>(inventory.getViewers())) {
            viewer.openInventory(inventory);
        }
    }

    @LuaFunction("setItem")
    public void setItem(LuaValue inv, LuaValue slot, LuaTable item) {
        inventory.setItem(slot.checkint(), ScriptUtil.createItemMatcher(item).getItemStack());

        //re-open inventory to update it
        for (HumanEntity viewer : new ArrayList<>(inventory.getViewers())) {
            viewer.openInventory(inventory);
        }
    }

    @LuaFunction("open")
    public void open(LuaValue inv, LuaValue player) {
        ScriptUtil.getPlayer(player).openInventory(inventory);
    }

    @LuaFunction("close")
    public void close(LuaValue inv) {
        for (HumanEntity viewer : new ArrayList<>(inventory.getViewers())) {
            viewer.closeInventory();
        }
    }
}
