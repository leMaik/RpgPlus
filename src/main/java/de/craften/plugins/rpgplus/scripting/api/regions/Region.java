package de.craften.plugins.rpgplus.scripting.api.regions;

import org.bukkit.Location;
import org.luaj.vm2.LuaFunction;

public class Region {

	private final String regionID;
	
	private final Location pos1;
	private final Location pos2;
	
	private final LuaFunction enterHandler;
	private final LuaFunction leaveHandler;
	
	public Region(String regionID, Location pos1, Location pos2, LuaFunction enterHandler, LuaFunction leaveHandler) {
		this.regionID = regionID;
		this.pos1 = pos1;
		this.pos2 = pos2;
		
		this.enterHandler = enterHandler;
		this.leaveHandler = leaveHandler;
	}
	
	public boolean isInRegion(Location loc) {
		
		if (loc.getWorld().getName().equals(pos1.getWorld().getName()) && loc.getWorld().getName().equals(pos2.getWorld().getName())) {
			if (loc.getX() >= pos1.getX() && loc.getX() <= pos2.getX()) {
				if (loc.getY() >= pos1.getY() && loc.getY() <= pos2.getY()) {
					if (loc.getZ() >= pos1.getZ() && loc.getZ() <= pos2.getZ()) {
						return true;
					}
				}
			}
		}
		return false;
		
	}
	
	public LuaFunction getEnterHandler() {
		return enterHandler;
	}
	
	public LuaFunction getLeaveHandler() {
		return leaveHandler;
	}
	
	public String getRegionID() {
		return regionID;
	}
	
	public Location getPos1() {
		return pos1;
	}
	
	public Location getPos2() {
		return pos2;
	}
	
}
