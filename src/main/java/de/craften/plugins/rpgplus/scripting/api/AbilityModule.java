package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.luaj.vm2.LuaBoolean;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaNumber;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.ability.Ability;
import de.craften.plugins.rpgplus.components.ability.AbilityComponent;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;

public class AbilityModule extends LuaTable implements ScriptingModule, Listener {
	
	private final RpgPlus plugin;
	
	public AbilityModule(final RpgPlus plugin) {
		this.plugin = plugin;
		
		Luaify.convertInPlace(this);
	}

	@de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("registerAbility")
	public void registerAbility(LuaTable options) {

		String identifier = options.get("identifier").checkjstring();
		String displayName = options.get("displayName").optjstring(identifier);

		LuaFunction giveTo = options.get("giveTo").checkfunction();
		LuaFunction removeFrom = options.get("removeFrom").checkfunction();
		LuaFunction onActivated = options.get("onActivated").checkfunction();
		LuaFunction onExpired = options.get("onExpired").checkfunction();

		Ability ability = new Ability() {

			@Override
			public String getIdentifier() {
				return identifier;
			}

			@Override
			public String getDisplayName() {
				return displayName;
			}

			@Override
			public void giveTo(Player player) {
				giveTo.invoke(CoerceJavaToLua.coerce(player));
			}

			@Override
			public void removeFrom(Player player) {
				removeFrom.invoke(CoerceJavaToLua.coerce(player));
			}

			@Override
			public void onActivated(Player player) {
				onActivated.invoke(CoerceJavaToLua.coerce(player));
			}

			@Override
			public void onExpired(Player player) {
				onExpired.invoke(CoerceJavaToLua.coerce(player));
			}
		};

		getAbilityManager().registerAbility(ability);

	}

	@de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("giveAbility")
	public void giveAbility(LuaValue player, LuaString abilityIdentifier, LuaNumber duration) {		
		getAbilityManager().giveAbility(ScriptUtil.getPlayer(player), abilityIdentifier.checkjstring(), duration.optlong(0));
	}

	@de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("hasAbility")
	public LuaBoolean hasAbility(LuaValue player, LuaString abilityIdentifier) {
		return LuaBoolean.valueOf(getAbilityManager().hasAbility(ScriptUtil.getPlayer(player), abilityIdentifier.checkjstring()));
	}

	@de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("pauseAbility")
	public void pauseAbility(LuaValue player, LuaString abilityIdentifier) {
		getAbilityManager().pauseAbility(ScriptUtil.getPlayer(player), abilityIdentifier.checkjstring());
	}

	@de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("pauseAllAbilities")
	public void pauseAllAbilities(LuaValue player) {
		getAbilityManager().pauseAllAbilities(ScriptUtil.getPlayer(player));
	}

	@de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("unpauseAbility")
	public void unpauseAbility(LuaValue player, LuaString abilityIdentifierValue) {
		getAbilityManager().unpauseAbility(ScriptUtil.getPlayer(player), abilityIdentifierValue.checkjstring());
	}

	@de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction("unpauseAllAbilities")
	public void unpauseAllAbilities(LuaValue player) {
		getAbilityManager().unpauseAllAbilities(ScriptUtil.getPlayer(player));
	}

	@Override
	public LuaValue getModule() {
		return this;
	}

	private AbilityComponent getAbilityManager() {
		return plugin.getAbilities();
	}
	
}
