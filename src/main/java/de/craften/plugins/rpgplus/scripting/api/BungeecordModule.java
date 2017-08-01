package de.craften.plugins.rpgplus.scripting.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;

public class BungeecordModule extends LuaTable implements ScriptingModule, PluginMessageListener{

	private final RpgPlus plugin;
	
	private List<org.luaj.vm2.LuaFunction> listeners;
	
	public BungeecordModule(final RpgPlus plugin) {
		
		this.plugin = plugin;
		
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
		
		listeners = new ArrayList<>();
		
		Luaify.convertInPlace(this);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
		      return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		
		for (org.luaj.vm2.LuaFunction listener : listeners) {
			this.plugin.getScriptingManager().invokeSafely(listener, CoerceJavaToLua.coerce(player), CoerceJavaToLua.coerce(subchannel), CoerceJavaToLua.coerce(in));
		}
		
	}
	
	@LuaFunction("onMessageReceive")
	public void onMessageReceive(org.luaj.vm2.LuaFunction listener) {
		this.listeners.add(listener);
	}
	
	@LuaFunction("sendMessage")
	public void sendMessage(LuaString playerName, LuaString message) {
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(playerName.checkjstring());
		out.writeUTF(message.checkjstring());
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		  
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	@LuaFunction("kickPlayer")
	public void kickPlayer(LuaString playerName, LuaString msg) {
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(playerName.checkjstring());
		out.writeUTF(msg.checkjstring());
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		  
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	@LuaFunction("connect")
	public void connectOther(LuaString playerName, LuaString server) {
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(playerName.checkjstring());
		out.writeUTF(server.checkjstring());
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		  
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	@Override
	public LuaValue getModule() {
		return this;
	}

	@Override
	public void reset() {
		// nothing to do here
	}
	
}
