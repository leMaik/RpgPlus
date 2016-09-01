package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.*;
import de.craften.plugins.rpgplus.components.entitymanager.traits.HorseTrait;
import de.craften.plugins.rpgplus.components.entitymanager.traits.OcelotTrait;
import de.craften.plugins.rpgplus.components.entitymanager.traits.RabbitTrait;
import de.craften.plugins.rpgplus.components.entitymanager.traits.VillagerTrait;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import de.craften.plugins.rpgplus.scripting.util.luaify.LuaFunction;
import de.craften.plugins.rpgplus.scripting.util.luaify.Luaify;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayList;
import java.util.List;

/**
 * A lua module for entities.
 */
public class EntityModule extends LuaTable implements ScriptingModule {
    private final EntityEventManager entityEventManager;
    private final List<RpgPlusEntity> entities = new ArrayList<>();

    public EntityModule(EntityEventManager entityEventManager) {
        this.entityEventManager = entityEventManager;
        entityEventManager.setEntityModule(this);
        Luaify.convertInPlace(this);
    }

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        entities.forEach((entity) -> {
            entity.getNpc().despawn();
            entity.getNpc().destroy();
        });
        entities.clear();
    }

    @LuaFunction("spawn")
    public EntityWrapper spawnEntity(LuaValue entityType, LuaValue optionsArg) {
        final EntityType type = EntityType.valueOf(entityType.checkjstring().toUpperCase());
        LuaTable options = optionsArg.checktable();

        RpgPlusEntity entity;
        if (type == EntityType.VILLAGER) {
            entity = new ManagedVillager(ScriptUtil.getLocation(optionsArg.checktable()));
        } else if (type == EntityType.HORSE) {
            entity = new ManagedHorse(ScriptUtil.getLocation(optionsArg.checktable()));
        } else if (type == EntityType.RABBIT) {
            entity = new ManagedRabbit(ScriptUtil.getLocation(optionsArg.checktable()));
        } else if (type == EntityType.OCELOT) {
            entity = new ManagedOcelot(ScriptUtil.getLocation(optionsArg.checktable()));
        } else {
            entity = new RpgPlusEntity(ScriptUtil.getLocation(optionsArg.checktable()), type);
        }

        entity.setName(ChatColor.translateAlternateColorCodes('&', options.get("name").optjstring("")));
        entity.setSecondName(ChatColor.translateAlternateColorCodes('&', options.get("secondName").optjstring("")));
        entity.setTakingDamage(!options.get("invulnerable").optboolean(false));
        entity.setNameVisible(options.get("nameVisible").optboolean(true));

        // TODO add corresponding traits to the entities
        /*
        switch (options.get("movementType").optjstring("local")) {
            case "normal":
                entity.setFrozen(false);
                break;
            case "frozen":
            case "local": //TODO entity should move the head when the movementType is set to local
                entity.setFrozen(true);
                break;
        }
        */

        if (entity instanceof ManagedVillager) {
            VillagerTrait villager = entity.getNpc().getTrait(VillagerTrait.class);
            if (!options.get("profession").isnil()) {
                villager.setProfession(ScriptUtil.enumValue(options.get("profession"), Villager.Profession.class));
            }
        } else if (entity instanceof ManagedHorse) {
            HorseTrait horse = entity.getNpc().getTrait(HorseTrait.class);
            if (!options.get("style").isnil()) {
                horse.setStyle(ScriptUtil.enumValue(options.get("style"), Horse.Style.class));
            }
            if (!options.get("variant").isnil()) {
                horse.setVariant(ScriptUtil.enumValue(options.get("variant"), Horse.Variant.class));
            }
            if (!options.get("color").isnil()) {
                horse.setColor(ScriptUtil.enumValue(options.get("color"), Horse.Color.class));
            }
            if (!options.get("jumpStrength").isnil()) {
                horse.setJumpStrength(options.get("jumpStrength").checkdouble());
            }
            if (!options.get("domestication").isnil()) {
                horse.setDomestication(options.get("domestication").checkint());
            }
            if (!options.get("maxDomestication").isnil()) {
                horse.setMaxDomestication(options.get("maxDomestication").checkint());
            }
        } else if (entity instanceof ManagedRabbit) {
            RabbitTrait rabbit = entity.getNpc().getTrait(RabbitTrait.class);
            if (!options.get("type").isnil()) {
                rabbit.setRabbitType(ScriptUtil.enumValue(options.get("type"), Rabbit.Type.class));
            }
        } else if (entity instanceof ManagedOcelot) {
            OcelotTrait ocelot = entity.getNpc().getTrait(OcelotTrait.class);
            if (!options.get("type").isnil()) {
                ocelot.setCatType(ScriptUtil.enumValue(options.get("type"), Ocelot.Type.class));
            }
        }

        entities.add(entity);
        entity.spawn();
        return EntityWrapper.create(entity, entityEventManager);
    }

    @LuaFunction("getNearby")
    public LuaTable getNearbyEntities(LuaValue locationParam, LuaValue radius) {
        final double radiusSquared = radius.checkdouble() * radius.checkdouble();
        Location location = ScriptUtil.getLocation(locationParam);

        return entities.stream()
                .filter((entity) -> entity.getNpc().isSpawned() &&
                        entity.getEntity().getLocation().distanceSquared(location) <= radiusSquared)
                .map((entity) -> EntityWrapper.create(entity, entityEventManager))
                .collect(ScriptUtil.asListTable());
    }

    public RpgPlusEntity getEntity(Entity entity) {
        for (RpgPlusEntity rpgPlusEntity : entities) {
            if (rpgPlusEntity.getEntity() == entity) {
                return rpgPlusEntity;
            }
        }
        return null;
    }
}
