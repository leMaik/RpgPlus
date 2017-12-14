package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.components.entitymanager.traits.ArmorStandTrait;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;

import org.bukkit.entity.ArmorStand;
import org.luaj.vm2.LuaValue;

class ArmorStandEntityWrapper extends EntityWrapper<ArmorStand> {
	
	ArmorStandEntityWrapper(RpgPlusEntity<ArmorStand> entity, EntityEventManager entityEventManager) {
        super(entity, entityEventManager);
    }

    @Override
    public LuaValue rawget(LuaValue key) {
    	if (key.isstring()) {
            switch (key.checkjstring()) {
                case "visible":
                    return LuaValue.valueOf(getArmorStandTrait().isVisible());
            }
        }
        return super.rawget(key);
    }


    @Override
    public void rawset(LuaValue key, LuaValue value) {
    	if (key.isstring()) {
            switch (key.checkjstring()) {
                case "visible":
                    getArmorStandTrait().setVisible(value.checkboolean());
                    break;
            }
        }
        super.rawset(key, value);
    }

    private ArmorStandTrait getArmorStandTrait() {
        return entity.getNpc().getTrait(ArmorStandTrait.class);
    }
}
