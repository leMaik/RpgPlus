package de.craften.plugins.rpgplus.scripting.api.entities.events;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.event.entity.EntityEvent;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The internal implementation of the {@link EntityEventManager}. Used to separate the huge number of listener
 * functions from this code.
 */
abstract class EntityEventManagerImpl {
    private final Map<UUID, Multimap<String, LuaFunction>> eventHandlers = new HashMap<>();

    protected void callHandlers(String eventName, EntityEvent event) {
        Multimap<String, LuaFunction> handlers = eventHandlers.get(event.getEntity().getUniqueId());
        if (handlers != null) {
            for (LuaFunction callback : handlers.get(eventName)) {
                callback.invoke(CoerceJavaToLua.coerce(event));
            }
        }
    }

    public LuaValue on(LuaValue entity, LuaValue eventName, LuaValue callback) {
        UUID entityUuid = ScriptUtil.getEntity(entity).getUniqueId();
        Multimap<String, LuaFunction> handlers = eventHandlers.get(entityUuid);
        if (handlers == null) {
            handlers = ArrayListMultimap.create();
            eventHandlers.put(entityUuid, handlers);
        }
        handlers.put(eventName.checkjstring(), callback.checkfunction());
        return LuaValue.NIL;
    }

    public LuaValue off(LuaValue entity, LuaValue eventName, LuaValue callback) {
        UUID entityUuid = ScriptUtil.getEntity(entity).getUniqueId();
        if (eventName.isnil()) {
            return LuaValue.valueOf(!eventHandlers.remove(entityUuid).isEmpty());
        } else {
            Multimap<String, LuaFunction> handlers = eventHandlers.get(entityUuid);
            if (handlers != null) {
                if (callback.isnil()) {
                    return LuaValue.valueOf(!handlers.removeAll(eventName.checkjstring()).isEmpty());
                } else {
                    return LuaFunction.valueOf(handlers.remove(eventName.checkjstring(), callback.checkfunction()));
                }
            } else {
                return LuaValue.FALSE;
            }
        }
    }
}
