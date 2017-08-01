package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

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
	
	public BungeecordModule(final RpgPlus plugin) {
		
		this.plugin = plugin;
		
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
		
		Luaify.convertInPlace(this);
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (!channel.equals("BungeeCord")) {
		      return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subchannel = in.readUTF();
		
	}
	
	@LuaFunction("sendMessage")
	public void sendMessage(Varargs args) {
		String playerName = args.checkjstring(1);
		String msg = args.checkjstring(2);
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("Message");
		out.writeUTF(playerName);
		out.writeUTF(msg);
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		  
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	@LuaFunction("kickPlayer")
	public void kickPlayer(Varargs args) {
		String playerName = args.checkjstring(1);
		String msg = args.checkjstring(2);
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("KickPlayer");
		out.writeUTF(playerName);
		out.writeUTF(msg);
		
		Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		  
		player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
	}
	
	@LuaFunction("connect")
	public void connectOther(Varargs args) {
		String playerName = args.checkjstring(1);
		String server = args.checkjstring(2);
		
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF("ConnectOther");
		out.writeUTF(playerName);
		out.writeUTF(server);
		
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
