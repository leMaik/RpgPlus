package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.components.entitymanager.traits.VillagerTrait;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.entity.Villager;
import org.luaj.vm2.LuaValue;

class VillagerEntityWrapper extends EntityWrapper<Villager> {
    VillagerEntityWrapper(RpgPlusEntity<Villager> entity, EntityEventManager entityEventManager) {
        super(entity, entityEventManager);
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "profession":
                    return LuaValue.valueOf(entity.getEntity().getProfession().toString());
            }
        }
        return super.rawget(key);
    }


    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "profession":
                    getVillagerTrait().setProfession(ScriptUtil.enumValue(value, Villager.Profession.class));
                    break;
            }
        }
        super.rawset(key, value);
    }

    private VillagerTrait getVillagerTrait() {
        return entity.getNpc().getTrait(VillagerTrait.class);
    }
}
