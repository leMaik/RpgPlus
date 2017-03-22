package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.components.entitymanager.traits.BatTrait;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import org.bukkit.entity.Bat;
import org.luaj.vm2.LuaValue;

class BatEntityWrapper extends EntityWrapper<Bat> {
    BatEntityWrapper(RpgPlusEntity<Bat> entity, EntityEventManager entityEventManager) {
        super(entity, entityEventManager);
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "awake":
                    return LuaValue.valueOf(entity.getEntity().isAwake());
            }
        }
        return super.rawget(key);
    }


    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "awake":
                    getBatTrait().setAwake(value.checkboolean());
                    break;
            }
        }
        super.rawset(key, value);
    }

    private BatTrait getBatTrait() {
        return entity.getNpc().getTrait(BatTrait.class);
    }
}
