package de.craften.plugins.rpgplus.scripting.api.actionbar;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.util.Map;

public class ActionBarModule extends LuaTable {
    private Map<Player, ActionBar> actionBars = RpgPlus.getPlugin(RpgPlus.class).getWeakPlayerMaps().createMap(ActionBar.class);

    public ActionBarModule() {
        set("set", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue player, LuaValue message, LuaValue duration) {
                Player p = ScriptUtil.getPlayer(player);
                String msg = ChatColor.translateAlternateColorCodes('&', message.checkjstring());

                if (actionBars.containsKey(p)) {
                    actionBars.remove(p).end();
                }

                actionBars.put(p, new ActionBar(p, msg, duration.optint(-1)));

                return LuaValue.NIL;
            }
        });

        set("clear", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue player) {
                Player p = ScriptUtil.getPlayer(player);
                ActionBar actionBar = actionBars.remove(p);
                if (actionBar != null) {
                    actionBar.end();
                }

                return LuaValue.NIL;
            }
        });
    }

    public void reset() {
        for (ActionBar actionBar : actionBars.values()) {
            actionBar.end();
        }
        actionBars.clear();
    }
}
