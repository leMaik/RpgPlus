package de.craften.plugins.rpgplus.scripting.api.actionbar;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;

import java.util.Map;

public class ActionBarModule extends LuaTable implements ScriptingModule {
    private final Map<Player, ActionBar> actionBars;

    public ActionBarModule(RpgPlus plugin) {
        this.actionBars = plugin.getWeakPlayerMaps().createMap(ActionBar.class);

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

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        for (ActionBar actionBar : actionBars.values()) {
            actionBar.end();
        }
        actionBars.clear();
    }
}
