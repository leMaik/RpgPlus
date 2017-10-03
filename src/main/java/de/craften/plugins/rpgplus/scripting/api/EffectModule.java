package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaInteger;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;

public class EffectModule  extends LuaTable implements ScriptingModule {
	
	public EffectModule(final RpgPlus plugin) {
		Luaify.convertInPlace(this);
	}
	
	@LuaFunction("giveEffect")
	public void giveEffect(LuaValue playerValue, LuaString effectType, LuaInteger duration, LuaInteger amplifier) {
		
		Player player = ScriptUtil.getPlayer(playerValue);
		
		PotionEffect effect = new PotionEffect(PotionEffectType.getByName(effectType.checkjstring().toUpperCase()), duration.checkint(), amplifier.checkint()-1, false, false);
		
		player.addPotionEffect(effect, true);
		
	}
	
	@LuaFunction("removeEffect")
	public void removeEffect(LuaValue playerValue, LuaString effectType) {
		
		Player player = ScriptUtil.getPlayer(playerValue);
		
		player.removePotionEffect(PotionEffectType.getByName(effectType.checkjstring().toUpperCase()));
		
	}
	
	@LuaFunction("hasEffect")
	public LuaBoolean hasEffect(LuaValue playerValue, LuaString effectType) {
		
		Player player = ScriptUtil.getPlayer(playerValue);
		
		return LuaBoolean.valueOf(player.hasPotionEffect(PotionEffectType.getByName(effectType.checkjstring().toUpperCase())));
		
	}
	
	@Override
	public LuaValue getModule() {
		return this;
	}

	@Override
	public void reset() {
		Bukkit.getOnlinePlayers().forEach((Player p) -> { 
			p.getActivePotionEffects().forEach((PotionEffect effect) -> {
				p.removePotionEffect(effect.getType());
			});
		});
	}
	
}
