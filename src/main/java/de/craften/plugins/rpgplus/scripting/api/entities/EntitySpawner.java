package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.MovementType;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
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
                EntityType type = EntityType.valueOf(entityType.checkjstring().toUpperCase());
                LuaTable options = optionsArg.checktable();

                ManagedEntity entity = RpgPlus.getPlugin(RpgPlus.class).getEntityManager().spawn(
                        type.getEntityClass(),
                        ScriptUtil.getLocation(optionsArg.checktable())
                );

                entity.setName(options.get("name").optjstring(""));
                entity.setSecondName(options.get("secondName").optjstring(""));
                entity.setIsTakingDamage(!options.get("invulnerable").optboolean(false));
                entity.setMovementType(MovementType.valueOf(options.get("movementType").optjstring("local").toUpperCase()));
                entity.setNameVisible(options.get("nameVisible").optboolean(true));
                entity.spawn();
                return EntityWrapper.wrap(entity);
            }
        });
    }
}
