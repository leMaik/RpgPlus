package de.craften.plugins.rpgplus.scripting.api.inventory.events;

import de.craften.plugins.rpgplus.scripting.util.SafeInvoker;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * An event manager for inventory events..
 */
public class InventoryEventManager extends InventoryEventManagerImpl implements Listener {
    public InventoryEventManager(SafeInvoker invoker) {
        super(invoker);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        callHandlers("click", event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        callHandlers("close", event);
    }
}
