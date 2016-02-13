package de.craften.plugins.rpgplus.scripting.api.actionbar;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;

public class ActionBarManager extends LuaTable{

	private HashMap<Player, ActionBar> actionBars = new HashMap<Player, ActionBar>();
	
	public ActionBarManager() {
		set("set", new TwoArgFunction() {
			
			@Override
			public LuaValue call(LuaValue player, LuaValue message) {
				
				Player p = ScriptUtil.getPlayer(player);
				String msg = ChatColor.translateAlternateColorCodes('&', message.tojstring());
				
				if(actionBars.containsKey(p)) {
					actionBars.remove(p).end();
				}

				actionBars.put(p, new ActionBar(p, msg, -1));
				
				return LuaValue.NIL;
			}
		});
		
		set("set", new ThreeArgFunction() {
			
			@Override
			public LuaValue call(LuaValue player, LuaValue message, LuaValue duration) {
				
				Player p = ScriptUtil.getPlayer(player);
				String msg = ChatColor.translateAlternateColorCodes('&', message.tojstring());
				
				if(actionBars.containsKey(p)) {
					actionBars.remove(p).end();
				}

				actionBars.put(p, new ActionBar(p, msg, duration.toint()));
				
				return LuaValue.NIL;
			}
		});
		
	}
	
	public void reset() {
		for(ActionBar ab : actionBars.values()) {
			ab.end();
		}
	}
	
}
