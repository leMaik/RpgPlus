package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.components.entitymanager.traits.OcelotTrait;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.entity.Ocelot;
import org.luaj.vm2.LuaValue;

class OcelotEntityWrapper extends EntityWrapper<Ocelot> {
    OcelotEntityWrapper(RpgPlusEntity<Ocelot> entity, EntityEventManager entityEventManager) {
        super(entity, entityEventManager);
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "type":
                    return LuaValue.valueOf(entity.getEntity().getCatType().toString());
            }
        }
        return super.rawget(key);
    }


    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "type":
                    getOcelotTrait().setCatType(ScriptUtil.enumValue(value, Ocelot.Type.class));
                    break;
            }
        }
        super.rawset(key, value);
    }

    private OcelotTrait getOcelotTrait() {
        return entity.getNpc().getTrait(OcelotTrait.class);
    }
}
