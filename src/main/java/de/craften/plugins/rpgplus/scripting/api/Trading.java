package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.components.trading.Merchant;
import de.craften.plugins.rpgplus.components.trading.MerchantOffer;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.util.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

/**
 * Lua API for trading using villagers.
 */
public class Trading extends LuaTable {
    public Trading(final Plugin plugin) {
        set("open", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                Merchant merchant = new Merchant();
                LuaTable offers = varargs.checktable(2);
                for (int i = 1; i <= offers.length(); i++) {
                    LuaTable offerConfig = offers.get(i).checktable();
                    MerchantOffer offer;
                    if (offerConfig.length() == 3) {
                        offer = new MerchantOffer(
                                getItem(offerConfig.get(1)),
                                getItem(offerConfig.get(2)),
                                getItem(offerConfig.get(3)));
                    } else if (offerConfig.length() == 2) {
                        offer = new MerchantOffer(
                                getItem(offerConfig.get(1)),
                                getItem(offerConfig.get(2)));
                    } else {
                        throw new LuaError("Invalid argument length, offer array must contain at least two items");
                    }
                    merchant.addOffer(offer);
                }

                Player player = (Player) CoerceLuaToJava.coerce(varargs.arg(1), Player.class);
                merchant.setCustomer(player);
                merchant.openTrading(player);
                return LuaValue.NIL;
            }
        });
    }

    private ItemStack getItem(LuaValue item) {
        if (item.isstring()) {
            return new Item(item.checkjstring()).getItemStack();
        } else if (item.istable()) {
            return ScriptUtil.createItemMatcher(item.checktable()).getItemStack();
        } else {
            throw new LuaError("Expected string or table to represent an itemstack.");
        }
    }
}
