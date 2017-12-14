package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.components.entitymanager.traits.SkeletonTrait;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;

import org.bukkit.entity.Skeleton;
import org.luaj.vm2.LuaValue;

class SkeletonEntityWrapper extends EntityWrapper<Skeleton> {
	
	SkeletonEntityWrapper(RpgPlusEntity<Skeleton> entity, EntityEventManager entityEventManager) {
        super(entity, entityEventManager);
    }

    @Override
    public LuaValue rawget(LuaValue key) {
    	if (key.isstring()) {
            switch (key.checkjstring()) {
                case "isWitherSkeleton":
                    return LuaValue.valueOf(getSkeletonTrait().isWitherSkeleton());
            }
        }
        return super.rawget(key);
    }


    @Override
    public void rawset(LuaValue key, LuaValue value) {
    	if (key.isstring()) {
            switch (key.checkjstring()) {
                case "isWitherSkeleton":
                    getSkeletonTrait().setWitherSkeleton(value.checkboolean());
                    break;
            }
        }
        super.rawset(key, value);
    }

    private SkeletonTrait getSkeletonTrait() {
        return entity.getNpc().getTrait(SkeletonTrait.class);
    }
    
}
