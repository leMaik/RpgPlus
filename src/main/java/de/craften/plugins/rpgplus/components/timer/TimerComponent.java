package de.craften.plugins.rpgplus.components.timer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.craften.plugins.rpgplus.util.components.PluginComponentBase;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A component that provides a timing API.
 */
public class TimerComponent extends PluginComponentBase {
    private Multimap<String, Timer> callbacks = ArrayListMultimap.create();
    private Map<String, Long> lastTimes = new HashMap<>();
    private int id = 0;

    @Override
    protected void onActivated() {
        runTaskTimer(new Runnable() {
            @Override
            public void run() {
                tick();
            }
        }, 0, 1);
    }

    /**
     * Invokes all registered callbacks, if needed. If the world's time is earlier than the world time of the last
     * call of tick, all handlers between these two times are invoked.
     */
    protected void tick() {
        for (Map.Entry<String, Collection<Timer>> worldEntry : callbacks.asMap().entrySet()) {
            World world = getServer().getWorld(worldEntry.getKey());
            long lastTime = getLastTime(world);
            long now = world.getTime();

            if (now < lastTime) {
                for (long time = lastTime; time < 24_000; time++) {
                    for (Timer timer : worldEntry.getValue()) {
                        if (timer.time == time) {
                            timer.callback.run();
                        }
                    }
                }
                for (long time = 0; time <= now; time++) {
                    for (Timer timer : worldEntry.getValue()) {
                        if (timer.time == time) {
                            timer.callback.run();
                        }
                    }
                }
            } else {
                for (long time = lastTime; time <= now; time++) {
                    for (Timer timer : worldEntry.getValue()) {
                        if (timer.time == time) {
                            timer.callback.run();
                        }
                    }
                }
            }
            this.lastTimes.put(world.getName(), world.getTime());
        }
    }

    private long getLastTime(World world) {
        Long lastTick = this.lastTimes.get(world.getName());
        if (lastTick != null) {
            return lastTick;
        }
        return world.getTime();
    }

    /**
     * Adds the given time-based callback.
     *
     * @param world    world
     * @param time     time to call this handler (0..23999)
     * @param callback callback
     * @return an ID of the handler that can be used to remove it with {@link #removeHandler(int)}
     */
    public int addHandler(World world, long time, Runnable callback) {
        id++;
        callbacks.put(world.getName(), new Timer(id, time % 24_000, callback));
        return id;
    }

    /**
     * Remove the handler with the given ID.
     *
     * @param id ID of the handler, as returned by {@link #addHandler(World, long, Runnable)}
     */
    public void removeHandler(int id) {
        Iterator<Map.Entry<String, Timer>> x = callbacks.entries().iterator();
        while (x.hasNext()) {
            if (x.next().getValue().id == id) {
                x.remove();
                return;
            }
        }
    }

    private static class Timer {
        private final int id;
        private final long time;
        private final Runnable callback;

        public Timer(int id, long time, Runnable callback) {
            this.id = id;
            this.time = time;
            this.callback = callback;
        }
    }
}
