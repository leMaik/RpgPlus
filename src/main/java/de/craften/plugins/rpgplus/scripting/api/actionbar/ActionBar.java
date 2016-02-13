package de.craften.plugins.rpgplus.scripting.api.actionbar;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.craften.plugins.rpgplus.RpgPlus;

public class ActionBar {
	
	private Player p;
	private String msg;
	
	private Object chat;
	private Object packet;
	private Object nmsPlayer;
	private Object connection;
	
	private int age = 0;
	
	private int scheduler = -1;
	
	public ActionBar(Player p, String msg, final int duration) {
		this.p = p;
		this.msg = msg;
		
		try {
			
			chat = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { "{'text': '" + msg + "'}" });
        	packet = getNmsClass("PacketPlayOutChat").getConstructor(new Class[] { getNmsClass("IChatBaseComponent"), Byte.TYPE }).newInstance(new Object[] { chat, (byte) 2});
        	nmsPlayer = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
        	connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.scheduler = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(RpgPlus.getPlugin(RpgPlus.class), new Runnable() {
			
			@Override
			public void run() {
				if(duration > 0) {
					age++;
					if(age >= duration - 2) {
						Bukkit.getServer().getScheduler().cancelTask(scheduler);
						scheduler = -1;
					}
				}
				send();
			}
		}, 20, 0);
		
	}

	private Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
		return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
	}
	
	public void end() {
		Bukkit.getServer().getScheduler().cancelTask(scheduler);
		scheduler = -1;
	}
	
	public void send() {
		if(p == null || msg == null)
			return;

    	try {
			connection.getClass().getMethod("sendPacket", new Class[] { getNmsClass("Packet") }).invoke(connection, new Object[] { packet });
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	
	}
	
}
