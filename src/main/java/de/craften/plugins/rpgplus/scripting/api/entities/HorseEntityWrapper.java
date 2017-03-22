package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.components.entitymanager.traits.HorseTrait;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.entity.Horse;
import org.luaj.vm2.LuaValue;

class HorseEntityWrapper extends EntityWrapper<Horse> {
    HorseEntityWrapper(RpgPlusEntity<Horse> entity, EntityEventManager entityEventManager) {
        super(entity, entityEventManager);
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "style":
                    return LuaValue.valueOf(entity.getEntity().getStyle().toString());
                case "variant":
                    return LuaValue.valueOf(entity.getEntity().getVariant().toString());
                case "color":
                    return LuaValue.valueOf(entity.getEntity().getColor().toString());
                case "jumpStrength":
                    return LuaValue.valueOf(entity.getEntity().getJumpStrength());
                case "domestication":
                    return LuaValue.valueOf(entity.getEntity().getDomestication());
                case "maxDomestication":
                    return LuaValue.valueOf(entity.getEntity().getMaxDomestication());
            }
        }
        return super.rawget(key);
    }


    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "style":
                    getHorseTrait().setStyle(ScriptUtil.enumValue(value, Horse.Style.class));
                    break;
                case "variant":
                    getHorseTrait().setVariant(ScriptUtil.enumValue(value, Horse.Variant.class));
                    break;
                case "color":
                    getHorseTrait().setColor(ScriptUtil.enumValue(value, Horse.Color.class));
                    break;
                case "jumpStrength":
                    getHorseTrait().setJumpStrength(value.checkdouble());
                    break;
                case "domestication":
                    getHorseTrait().setDomestication(value.checkint());
                    break;
                case "maxDomestication":
                    getHorseTrait().setMaxDomestication(value.checkint());
                    break;
            }
        }
        super.rawset(key, value);
    }

    private HorseTrait getHorseTrait() {
        return entity.getNpc().getTrait(HorseTrait.class);
    }
}
