package de.craften.plugins.rpgplus.scripting.api;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;

public class ParticleModule extends LuaTable implements ScriptingModule{

	public ParticleModule(final RpgPlus plugin) {
		set("spawn", new VarArgFunction() {
			
			@Override
            public Varargs invoke(Varargs varargs) {
                
				Location loc = ScriptUtil.getLocation(varargs.arg(1));
				
				Effect effect = Effect.valueOf(varargs.arg(2).tojstring().toUpperCase());
				
				int id = varargs.toint(3);
				
				int data = varargs.toint(4);
				
				float offsetX = (float) varargs.optdouble(5, 0);
				float offsetY = (float) varargs.optdouble(6, 0);
				float offsetZ = (float) varargs.optdouble(7, 0);
				
				float speed = (float) varargs.optdouble(8, 0);
				
				int particleCount = varargs.optint(9, 1);
				
				int radius = varargs.optint(10, 100);
				
				loc.getWorld().spigot().playEffect(loc, effect, id, data, offsetX, offsetY, offsetZ, speed, particleCount, radius);
				
                return LuaValue.NIL;
            }
			
		});
	}
	
	@Override
	public LuaValue getModule() {
		return this;
	}

	@Override
	public void reset() {
		//nothing to do here
	}


}
