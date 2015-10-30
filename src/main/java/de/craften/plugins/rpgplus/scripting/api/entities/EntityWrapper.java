package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 * A lua wrapper for a {@link de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity}.
 */
public class EntityWrapper {
    private final ManagedEntity entity;

    public EntityWrapper(ManagedEntity entity) {
        this.entity = entity;

    }

    public static LuaValue wrap(ManagedEntity entity) {
        return CoerceJavaToLua.coerce(new EntityWrapper(entity));
    }
}
