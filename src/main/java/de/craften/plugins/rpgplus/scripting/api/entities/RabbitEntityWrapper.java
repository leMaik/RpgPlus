package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.components.entitymanager.traits.RabbitTrait;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.entity.Rabbit;
import org.luaj.vm2.LuaValue;

class RabbitEntityWrapper extends EntityWrapper<Rabbit> {
    RabbitEntityWrapper(RpgPlusEntity<Rabbit> entity, EntityEventManager entityEventManager) {
        super(entity, entityEventManager);
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "type":
                    return LuaValue.valueOf(entity.getEntity().getRabbitType().toString());
            }
        }
        return super.rawget(key);
    }


    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "type":
                    getRabbitTrait().setRabbitType(ScriptUtil.enumValue(value, Rabbit.Type.class));
                    break;
            }
        }
        super.rawset(key, value);
    }

    private RabbitTrait getRabbitTrait() {
        return entity.getNpc().getTrait(RabbitTrait.class);
    }
}
