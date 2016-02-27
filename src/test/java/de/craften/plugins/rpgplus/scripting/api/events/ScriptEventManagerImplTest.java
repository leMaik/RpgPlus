package de.craften.plugins.rpgplus.scripting.api.events;

import de.craften.plugins.rpgplus.test.util.DummySafeInvoker;
import org.bukkit.event.Event;
import org.junit.Before;
import org.junit.Test;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ScriptEventManagerImplTest {
    private ScriptEventManager eventManager;
    private AtomicInteger callbackInvoked;
    private ZeroArgFunction callback;

    @Before
    public void setUp() throws Exception {
        eventManager = new ScriptEventManager(new DummySafeInvoker());
        callbackInvoked = new AtomicInteger(0);
        callback = new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                callbackInvoked.incrementAndGet();
                return LuaValue.NONE;
            }
        };
    }

    @Test
    public void testOn() throws Exception {
        eventManager.on(LuaValue.valueOf("test"), callback);
        eventManager.callHandlers("test", mock(Event.class));
        eventManager.callHandlers("test", mock(Event.class));

        assertEquals(2, callbackInvoked.get());

    }

    @Test
    public void testOnce() throws Exception {
        eventManager.once(LuaValue.valueOf("test"), callback);
        eventManager.callHandlers("test", mock(Event.class));
        eventManager.callHandlers("test", mock(Event.class));

        assertEquals(1, callbackInvoked.get());
    }

    @Test
    public void testOff() throws Exception {
        eventManager.on(LuaValue.valueOf("test"), callback);
        eventManager.callHandlers("test", mock(Event.class));
        eventManager.off(LuaValue.valueOf("test"), callback);
        eventManager.callHandlers("test", mock(Event.class));

        assertEquals(1, callbackInvoked.get());
    }

    @Test
    public void testReset() throws Exception {
        eventManager.on(LuaValue.valueOf("test"), callback);
        eventManager.reset();
        eventManager.callHandlers("test", mock(Event.class));

        assertEquals(0, callbackInvoked.get());
    }
}