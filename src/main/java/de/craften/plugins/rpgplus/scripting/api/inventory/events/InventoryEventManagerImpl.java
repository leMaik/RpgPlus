package de.craften.plugins.rpgplus.scripting.api.inventory.events;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.craften.plugins.rpgplus.scripting.api.inventory.InventoryWrapper;
import de.craften.plugins.rpgplus.scripting.util.SafeInvoker;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The internal implementation of the {@link InventoryEventManager}. Used to separate the listener
 * functions from this code.
 */
abstract class InventoryEventManagerImpl {
    private final Map<Inventory, Multimap<String, LuaFunction>> eventHandlers = new HashMap<>();
    private final SafeInvoker invoker;

    public InventoryEventManagerImpl(SafeInvoker invoker) {
        this.invoker = invoker;
    }

    private Multimap<String, LuaFunction> getHandlers(Inventory inventory) {
        return eventHandlers.get(inventory);
    }

    protected void callHandlers(String eventName, InventoryEvent event) {
        callHandlers(eventName, event, event.getInventory());
    }

    protected void callHandlers(String eventName, Event event, Inventory inventory) {
        Multimap<String, LuaFunction> handlers = getHandlers(inventory);
        if (handlers != null) {
            Collection<LuaFunction> callbacks = handlers.get(eventName);
            for (LuaFunction callback : callbacks.toArray(new LuaFunction[callbacks.size()])) {
                invoker.invokeSafely(callback, CoerceJavaToLua.coerce(event));
            }
        }
    }

    public LuaValue on(LuaValue inventoryWrapper, LuaValue eventName, LuaValue callback) {
        if (inventoryWrapper instanceof InventoryWrapper) {
            Inventory inventory = ((InventoryWrapper) inventoryWrapper).getInventory();
            Multimap<String, LuaFunction> handlers = eventHandlers.get(inventory);
            if (handlers == null) {
                handlers = ArrayListMultimap.create();
                eventHandlers.put(inventory, handlers);
            }
            handlers.put(eventName.checkjstring(), callback.checkfunction());
            return LuaValue.NIL;
        } else {
            throw new LuaError("Not an inventory");
        }
    }

    public LuaValue once(final LuaValue entity, final LuaValue eventName, final LuaValue callback) {
        return on(entity, eventName, new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs args) {
                off(entity, eventName, this);
                return callback.invoke(args);
            }
        });
    }

    public LuaValue off(LuaValue inventoryWrapper, LuaValue eventName, LuaValue callback) {
        if (inventoryWrapper instanceof InventoryWrapper) {
            Inventory inventory = ((InventoryWrapper) inventoryWrapper).getInventory();
            if (eventName.isnil()) {
                return LuaValue.valueOf(!eventHandlers.remove(inventory).isEmpty());
            } else {
                Multimap<String, LuaFunction> handlers = eventHandlers.get(inventory);
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
        }else {
            throw new LuaError("Not an inventory");
        }
    }

    /**
     * Removes all event handlers.
     */
    public void reset() {
        eventHandlers.clear();
    }
}
