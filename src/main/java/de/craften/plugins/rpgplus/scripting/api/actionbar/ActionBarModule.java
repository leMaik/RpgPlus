package de.craften.plugins.rpgplus.scripting.api.actionbar;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.Map;

public class ActionBarModule extends LuaTable implements ScriptingModule {
    private final Map<Player, ActionBar> actionBars;

    public ActionBarModule(RpgPlus plugin) {
        this.actionBars = plugin.getWeakPlayerMaps().createMap(ActionBar.class);
        Luaify.convert(this);
    }

    @LuaFunction("set")
    public void set(LuaValue player, LuaValue message, LuaValue duration) {
        Player p = ScriptUtil.getPlayer(player);
        String msg = ChatColor.translateAlternateColorCodes('&', message.checkjstring());

        if (actionBars.containsKey(p)) {
            actionBars.remove(p).end();
        }

        actionBars.put(p, new ActionBar(p, msg, duration.optint(-1)));
    }

    @LuaFunction("clear")
    public void clear(LuaValue player) {
        Player p = ScriptUtil.getPlayer(player);
        ActionBar actionBar = actionBars.remove(p);
        if (actionBar != null) {
            actionBar.end();
        }
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
