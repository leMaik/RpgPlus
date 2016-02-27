package de.craften.plugins.rpgplus.scripting.api.entities.events;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The internal implementation of the {@link EntityEventManager}. Used to separate the huge number of listener
 * functions from this code.
 */
abstract class EntityEventManagerImpl {
    private final Map<RpgPlusEntity, Multimap<String, LuaFunction>> eventHandlers = new HashMap<>();

    private Multimap<String, LuaFunction> getHandlers(Entity entity) {
        RpgPlusEntity managedEntity = (RpgPlusEntity) RpgPlus.getPlugin(RpgPlus.class).getEntityManager().getEntity(entity);
        return managedEntity != null ? eventHandlers.get(managedEntity) : null;
    }

    protected void callHandlers(String eventName, EntityEvent event) {
        callHandlers(eventName, event, event.getEntity());
    }

    protected void callHandlers(String eventName, Event event, Entity entity) {
        Multimap<String, LuaFunction> handlers = getHandlers(entity);
        if (handlers != null) {
            Collection<LuaFunction> callbacks = handlers.get(eventName);
            for (LuaFunction callback : callbacks.toArray(new LuaFunction[callbacks.size()])) {
                RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().runSafely(callback, CoerceJavaToLua.coerce(event));
            }
        }
    }

    public LuaValue on(LuaValue entity, LuaValue eventName, LuaValue callback) {
        RpgPlusEntity managedEntity = ScriptUtil.getManagedEntity(entity, true);
        Multimap<String, LuaFunction> handlers = eventHandlers.get(managedEntity);
        if (handlers == null) {
            handlers = ArrayListMultimap.create();
            eventHandlers.put(managedEntity, handlers);
        }
        handlers.put(eventName.checkjstring(), callback.checkfunction());
        return LuaValue.NIL;
    }

    public void once(final LuaValue entity, final LuaValue eventName, final LuaValue callback) {
        on(entity, eventName, new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                off(entity, eventName, callback);
                return callback.invoke(args);
            }
        });
    }

    public LuaValue off(LuaValue entity, LuaValue eventName, LuaValue callback) {
        RpgPlusEntity managedEntity = ScriptUtil.getManagedEntity(entity, true);
        if (eventName.isnil()) {
            return LuaValue.valueOf(!eventHandlers.remove(managedEntity).isEmpty());
        } else {
            Multimap<String, LuaFunction> handlers = eventHandlers.get(managedEntity);
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

    /**
     * Removes all event handlers.
     */
    public void reset() {
        eventHandlers.clear();
    }

    /*
    public static void main(String[] args) throws Exception {
        //to use this program, bukkit and mockito need to be in the classpath
        //it generates a list of all events as required by luadoc

        final AtomicReference<String> eventName = new AtomicReference<>();

        EntityEventManager printEventManager = new EntityEventManager() {
            @Override
            protected void callHandlers(String name, Event event, Entity entity) {
                eventName.set(name);
            }

            @Override
            protected void callHandlers(String name, EntityEvent event) {
                eventName.set(name);
            }
        };
        for (Method method : printEventManager.getClass().getMethods()) {
            if (method.isAnnotationPresent(EventHandler.class)) {
                method.invoke(printEventManager, mock(method.getParameterTypes()[0]));
                System.out.printf("-- * `\"%s\"` - [%s](https://hub.spigotmc.org/javadocs/bukkit/index.html?%s.html)\n",
                        eventName.get(),
                        method.getParameterTypes()[0].getSimpleName(),
                        method.getParameterTypes()[0].getName().replace(".", "/"));
            }
        }
    }
    */
}
