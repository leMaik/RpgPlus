package de.craften.plugins.rpgplus.scripting.api.regions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;

public class RegionModule extends LuaTable implements ScriptingModule, Listener {
	
	private Map<String, Region> regions;
	
	public RegionModule(final Plugin plugin) {
		
		this.regions = new HashMap<String, Region>();
		
		set("add", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
            	String regionID = varargs.checkjstring(1);
                LuaTable region = varargs.checktable(2);
                
                Region r = new Region(regionID, ScriptUtil.getLocation(region.get("pos1")), ScriptUtil.getLocation(region.get("pos2")), varargs.checkfunction(3), varargs.checkfunction(4));
                RegionModule.this.regions.put(regionID, r);
                
                return CoerceJavaToLua.coerce(r);
            }
        });
		
		set("delete", new OneArgFunction() {
			
			@Override
			public LuaValue call(LuaValue regionID) {
				return CoerceJavaToLua.coerce(RegionModule.this.regions.remove(regionID.optjstring("")));
			}
		});
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		for (Region r : this.regions.values()) {
			if (r.isInRegion(event.getTo())) {
				if (!r.isInRegion(event.getFrom())) {
					if (r.getEnterHandler() != null) {
						RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(r.getEnterHandler(), LuaValue.varargsOf(new LuaValue[]{CoerceJavaToLua.coerce(event), CoerceJavaToLua.coerce(r)}));
					}
				}
				
			} else if (r.isInRegion(event.getFrom())) {
				if (r.getLeaveHandler() != null) {
	                RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(r.getLeaveHandler(), LuaValue.varargsOf(new LuaValue[]{CoerceJavaToLua.coerce(event), CoerceJavaToLua.coerce(r)}));
				}
				
			}
		}
		
	}
	
	@Override
	public LuaValue getModule() {
		return this;
	}

	@Override
	public void reset() {
		regions.clear();
	}

}
