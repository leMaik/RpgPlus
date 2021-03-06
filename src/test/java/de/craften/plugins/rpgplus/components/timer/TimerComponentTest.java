package de.craften.plugins.rpgplus.components.timer;

import org.bukkit.Server;
import org.bukkit.World;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class TimerComponentTest {
    @Test
    public void testHandlerCalled() throws Exception {
        World mockWorld = mock(World.class);
        when(mockWorld.getTime()).thenReturn((long) 42);
        when(mockWorld.getName()).thenReturn("world");
        final Server mockServer = mock(Server.class);
        when(mockServer.getWorld("world")).thenReturn(mockWorld);
        Runnable callback = mock(Runnable.class);

        TimerComponent mockTimer = new TimerComponent() {
            @Override
            public Server getServer() {
                return mockServer;
            }
        };

        mockTimer.addHandler(mockWorld, 42, callback);
        mockTimer.tick();

        verify(callback, times(1)).run(); //handler should run when its time has come
    }

    @Test
    public void testHandlerOnlyCalledInTime() throws Exception {
        World mockWorld = mock(World.class);
        when(mockWorld.getTime()).thenReturn((long) 42);
        when(mockWorld.getName()).thenReturn("world");
        final Server mockServer = mock(Server.class);
        when(mockServer.getWorld("world")).thenReturn(mockWorld);
        Runnable callback = mock(Runnable.class);

        TimerComponent mockTimer = new TimerComponent() {
            @Override
            public Server getServer() {
                return mockServer;
            }
        };

        mockTimer.addHandler(mockWorld, 21, callback);
        mockTimer.tick();

        verify(callback, never()).run(); //handler should not run, as the time is 42 but the handler should run on 21
    }

    @Test
    public void testHandlerOnlyCalledOnce() throws Exception { //this one is for issue #15
        World mockWorld = mock(World.class);
        when(mockWorld.getTime()).thenReturn((long) 40);
        when(mockWorld.getName()).thenReturn("world");
        final Server mockServer = mock(Server.class);
        when(mockServer.getWorld("world")).thenReturn(mockWorld);
        Runnable callback = mock(Runnable.class);

        TimerComponent mockTimer = new TimerComponent() {
            @Override
            public Server getServer() {
                return mockServer;
            }
        };
        mockTimer.addHandler(mockWorld, 2, callback);

        when(mockWorld.getTime()).thenReturn((long) 1);
        mockTimer.tick();
        when(mockWorld.getTime()).thenReturn((long) 2);
        mockTimer.tick();
        when(mockWorld.getTime()).thenReturn((long) 3);
        mockTimer.tick();
        verify(callback, times(1)).run(); //callback should only be invoked once
    }

    @Test
    public void testWithLaggingServer() throws Exception {
        World mockWorld = mock(World.class);
        when(mockWorld.getTime()).thenReturn((long) 40);
        when(mockWorld.getName()).thenReturn("world");
        final Server mockServer = mock(Server.class);
        when(mockServer.getWorld("world")).thenReturn(mockWorld);
        Runnable callback = mock(Runnable.class);

        TimerComponent mockTimer = new TimerComponent() {
            @Override
            public Server getServer() {
                return mockServer;
            }
        };

        mockTimer.addHandler(mockWorld, 42, callback);
        mockTimer.tick();
        verify(callback, never()).run(); //time has not yet come
        when(mockWorld.getTime()).thenReturn((long) 43); //too late, server has lagged
        mockTimer.tick();
        verify(callback, times(1)).run(); //callback should now be invoked, although its exact tick was skipped
    }

    @Test
    public void testWithTimeChange() throws Exception {
        World mockWorld = mock(World.class);
        when(mockWorld.getTime()).thenReturn((long) 42);
        when(mockWorld.getName()).thenReturn("world");
        final Server mockServer = mock(Server.class);
        when(mockServer.getWorld("world")).thenReturn(mockWorld);
        Runnable callback = mock(Runnable.class);

        TimerComponent mockTimer = new TimerComponent() {
            @Override
            public Server getServer() {
                return mockServer;
            }
        };

        mockTimer.addHandler(mockWorld, 100, callback);
        mockTimer.addHandler(mockWorld, 0, callback);
        mockTimer.addHandler(mockWorld, 23_999, callback);

        mockTimer.tick();
        when(mockWorld.getTime()).thenReturn((long) 21); //earlier than before, i.e. player set time manually
        mockTimer.tick();
        verify(callback, times(3)).run(); //all missed callbacks should be invoked
    }

    @Test
    public void testRemoveHandler() throws Exception {
        World mockWorld = mock(World.class);
        when(mockWorld.getTime()).thenReturn((long) 42);
        when(mockWorld.getName()).thenReturn("world");
        final Server mockServer = mock(Server.class);
        when(mockServer.getWorld("world")).thenReturn(mockWorld);
        Runnable callback = mock(Runnable.class);
        Runnable callback2 = mock(Runnable.class);

        TimerComponent mockTimer = new TimerComponent() {
            @Override
            public Server getServer() {
                return mockServer;
            }
        };

        int handler = mockTimer.addHandler(mockWorld, 42, callback);
        mockTimer.addHandler(mockWorld, 42, callback2);
        mockTimer.tick();
        mockTimer.removeHandler(handler);
        mockTimer.tick();
        verify(callback, times(1)).run(); //only once (before the handler is removed)
        verify(callback2, times(2)).run(); //other timers should not be removed
    }
}