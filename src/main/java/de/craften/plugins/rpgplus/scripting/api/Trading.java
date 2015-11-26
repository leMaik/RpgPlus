package de.craften.plugins.rpgplus.scripting.api;

import de.craften.plugins.rpgplus.components.trading.Merchant;
import de.craften.plugins.rpgplus.components.trading.MerchantOffer;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.entity.Player;
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
                                ScriptUtil.createItemMatcher(offerConfig.get(1)).getItemStack(),
                                ScriptUtil.createItemMatcher(offerConfig.get(2)).getItemStack(),
                                ScriptUtil.createItemMatcher(offerConfig.get(3)).getItemStack());
                    } else if (offerConfig.length() == 2) {
                        offer = new MerchantOffer(
                                ScriptUtil.createItemMatcher(offerConfig.get(1)).getItemStack(),
                                ScriptUtil.createItemMatcher(offerConfig.get(2)).getItemStack());
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

}
