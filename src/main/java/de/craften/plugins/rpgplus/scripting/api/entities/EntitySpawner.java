package de.craften.plugins.rpgplus.scripting.api.entities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import de.craften.plugins.rpgplus.components.entitymanager.BasicManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.components.entitymanager.MovementType;

public class EntitySpawner {

    public void installOn(LuaTable object){
        object.set("registerEntity", new TwoArgFunction() {
            
            @Override
            public LuaValue call(LuaValue entityType, LuaValue optionsArg) {
                
                EntityType type = EntityType.valueOf(entityType.checkjstring());
                Class<? extends Entity> entityClass = type.getEntityClass();
                
                LuaTable options = optionsArg.checktable();
                
                ManagedEntity<? extends Entity> entity = new BasicManagedEntity<>(entityClass, new Location(
                        Bukkit.getWorld(options.get("world").checkjstring()),
                        options.get("x").checkdouble(),
                        options.get("y").checkdouble(),
                        options.get("z").checkdouble()));
                
                entity.setName(options.get("name").optjstring(""));
                entity.setIsTakingDamage(options.get("invulnerable").optboolean(false));
                entity.setMovementType(MovementType.valueOf(options.get("movementType").optjstring("LOCAL")));
                
                return EntityWrapper.wrap(entity);
            }
        });
    }
    
}
