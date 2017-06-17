package de.craften.plugins.rpgplus.scripting.api.title;

import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * Module for titles (tab title and big titles).
 */
public class TitlesModule extends LuaTable implements ScriptingModule {
    public TitlesModule() {
        Luaify.convertInPlace(this);
    }

    @LuaFunction("setTabTitles")
    public void setTabTitles(Varargs args) {
        if (args.narg() == 3) {
            TitleApi.sendTabTitle(ScriptUtil.getPlayer(args.arg(1)),
                    ChatColor.translateAlternateColorCodes('&', args.checkjstring(2)),
                    ChatColor.translateAlternateColorCodes('&', args.checkjstring(3)));
        } else {
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                TitleApi.sendTabTitle(player,
                        ChatColor.translateAlternateColorCodes('&', args.checkjstring(1)),
                        ChatColor.translateAlternateColorCodes('&', args.checkjstring(2)));
            }
        }
    }
    
    @LuaFunction("setTitle")
    public void setTitle(Varargs args) {
        LuaTable options = args.checktable(2);
        
        String title = ChatColor.translateAlternateColorCodes('&', options.get("title").checkjstring());
        String subtitle = ChatColor.translateAlternateColorCodes('&', options.get("subtitle").optjstring(null));
        
        int fadeIn = options.get("fadeIn").optint(0);
        int fadeOut = options.get("fadeOut").optint(2);
        int stay = options.get("stay").optint(8);
        
        TitleApi.sendTitle(ScriptUtil.getPlayer(args.arg(1)), fadeIn, stay, fadeOut, title, subtitle);
        
    }
    
    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        //Reset tab titles
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            TitleApi.resetTitles(player);
        }
    }
}
