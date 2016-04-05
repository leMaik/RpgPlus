package de.craften.plugins.rpgplus.scripting.api.inventory;

import de.craften.plugins.rpgplus.scripting.api.inventory.events.InventoryEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.util.ArrayList;

/**
 * A Lua wrapper for inventories.
 */
public class InventoryWrapper extends LuaTable {
    private final Inventory inventory;
    private final InventoryEventManager entityEventManager;

    public InventoryWrapper(Inventory inventory, InventoryEventManager entityEventManager) {
        this.inventory = inventory;
        this.entityEventManager = entityEventManager;
        Luaify.convertInPlace(this);

        set("on", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue entity, LuaValue eventName, LuaValue callback) {
                return InventoryWrapper.this.entityEventManager.on(entity, eventName, callback);
            }
        });

        set("once", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue entity, LuaValue eventName, LuaValue callback) {
                return InventoryWrapper.this.entityEventManager.once(entity, eventName, callback);
            }
        });

        set("off", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue entity, LuaValue eventName, LuaValue callback) {
                return InventoryWrapper.this.entityEventManager.off(entity, eventName, callback);
            }
        });
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

    /**
     * Gets the wrapped inventory.
     *
     * @return the wrapped inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}
