package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.managedentities.EntityManager;
import de.craften.plugins.managedentities.ManagedEntityBase;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedVillager;
import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

/**
 * A lua module for entities.
 */
public class EntityModule extends LuaTable implements ScriptingModule {
    private final EntityManager entityManager;
    private final EntityEventManager entityEventManager;

    public EntityModule(EntityManager entityManager, EntityEventManager entityEventManager) {
        this.entityManager = entityManager;
        this.entityEventManager = entityEventManager;
        Luaify.convertInPlace(this);
    }

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        //nothing to reset
    }

    @LuaFunction("spawn")
    public EntityWrapper spawnEntity(LuaValue entityType, LuaValue optionsArg) {
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

        entity.setName(ChatColor.translateAlternateColorCodes('&', options.get("name").optjstring("")));
        entity.setSecondName(ChatColor.translateAlternateColorCodes('&', options.get("secondName").optjstring("")));
        entity.setTakingDamage(!options.get("invulnerable").optboolean(false));
        entity.setNameVisible(options.get("nameVisible").optboolean(true));

        switch (options.get("movementType").optjstring("local")) {
            case "normal":
                entity.setFrozen(false);
                break;
            case "frozen":
            case "local": //TODO entity should move the head when the movementType is set to local
                entity.setFrozen(true);
                break;
        }

        if (entity instanceof ManagedVillager) {
            if (!options.get("profession").isnil()) {
                ((ManagedVillager) entity).setProfession(Villager.Profession.valueOf(options.get("profession").checkjstring().toUpperCase()));
            }
        }

        entityManager.addEntity(entity);
        entity.spawn();
        return new EntityWrapper(entity, entityEventManager);
    }

    @LuaFunction("getNearby")
    public LuaTable getNearbyEntities(LuaValue locationParam, LuaValue radius) {
        Location location = ScriptUtil.getLocation(locationParam);
        List<EntityWrapper> entities = new ArrayList<>();

        for (ManagedEntityBase entity : entityManager.getEntitiesNear(location, radius.checkdouble())) {
            if (entity instanceof RpgPlusEntity) {
                entities.add(new EntityWrapper((RpgPlusEntity) entity, entityEventManager));
            }
        }

        return ScriptUtil.tableOf(entities);
    }
}
