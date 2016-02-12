package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.managedentities.ManagedEntityBase;
import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedVillager;
import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
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
                final EntityType type = EntityType.valueOf(entityType.checkjstring().toUpperCase());
                LuaTable options = optionsArg.checktable();

                RpgPlusEntity entity;
                if (type == EntityType.VILLAGER) {
                    entity = new ManagedVillager(ScriptUtil.getLocation(optionsArg.checktable()));
                } else {
                    entity = new RpgPlusEntity(ScriptUtil.getLocation(optionsArg.checktable())) {
                        @Override
                        protected Entity spawnEntity(Location location) {
                            return location.getWorld().spawn(location, type.getEntityClass());
                        }
                    };
                }

                entity.setName(options.get("name").optjstring(""));
                entity.setSecondName(options.get("secondName").optjstring(""));
                entity.setTakingDamage(!options.get("invulnerable").optboolean(false));
                entity.setNameVisible(options.get("nameVisible").optboolean(true));

                if (entity instanceof ManagedVillager) {
                    if (!options.get("profession").isnil()) {
                        ((ManagedVillager) entity).setProfession(Villager.Profession.valueOf(options.get("profession").checkjstring().toUpperCase()));
                    }
                }

                entity.spawn();
                return EntityWrapper.wrap(entity);
            }
        });
    }
}
