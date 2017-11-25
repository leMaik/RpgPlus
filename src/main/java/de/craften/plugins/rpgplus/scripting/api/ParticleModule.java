package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;

public class ParticleModule extends LuaTable implements ScriptingModule {

	public ParticleModule(final RpgPlus plugin) {
		Luaify.convertInPlace(this);
	}

	@LuaFunction("spawn")
	public void spawn(Varargs varargs) {

		Location loc = ScriptUtil.getLocation(varargs.arg(1));

		Effect effect = Effect.valueOf(varargs.arg(2).tojstring().toUpperCase());
		
		LuaTable options = varargs.opttable(3, new LuaTable());
		
		int id = options.get("id").optint(0);

		int data = options.get("data").optint(0);
		
		LuaTable offset = options.get("offset").opttable(new LuaTable());
		
		float offsetX = (float) offset.get("x").optdouble(0);
		float offsetY = (float) offset.get("y").optdouble(0);
		float offsetZ = (float) offset.get("z").optdouble(0);

		float speed = (float) options.get("speed").optdouble(0);

		int particleCount = options.get("count").optint(1);

		int radius = options.get("radius").optint(1);
		
		LuaTable players = varargs.opttable(4, null);
		
		if (players == null) {

			loc.getWorld()
					.spigot()
					.playEffect(loc, effect, id, data, offsetX, offsetY, offsetZ,
							speed, particleCount, radius);

		} else {
			
			ScriptUtil.toListTableStream(players).forEach((LuaValue value) -> {
				ScriptUtil.getPlayer(value).spigot().playEffect(loc, effect, id, data, offsetX, offsetY, offsetZ,
						speed, particleCount, radius);
			});
			
		}
		
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
