package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.BasicManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.MovementType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Lua API for spawning entities.
 */
public class EntitySpawner {
    public void installOn(LuaTable object) {
        object.set("spawn", new TwoArgFunction() {

            @Override
            public LuaValue call(LuaValue entityType, LuaValue optionsArg) {
                EntityType type = EntityType.valueOf(entityType.checkjstring());
                LuaTable options = optionsArg.checktable();

                ManagedEntity entity = new BasicManagedEntity<>(type.getEntityClass(), new Location(
                        Bukkit.getWorld(options.get("world").checkjstring()),
                        options.get("x").checkdouble(),
                        options.get("y").checkdouble(),
                        options.get("z").checkdouble()));

                entity.setName(options.get("name").optjstring(""));
                entity.setIsTakingDamage(options.get("invulnerable").optboolean(false));
                entity.setMovementType(MovementType.valueOf(options.get("movementType").optjstring("LOCAL")));
                entity.spawn();

                return EntityWrapper.wrap(entity);
            }
        });
    }
}
