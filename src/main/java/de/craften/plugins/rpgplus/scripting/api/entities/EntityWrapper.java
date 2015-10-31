package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.dialogs.AnswerHandler;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.AStar;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.PathingBehaviours;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.PathingResult;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A lua wrapper for a {@link de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity}.
 */
public class EntityWrapper extends LuaTable {
    private static final Random random = new Random();
    private final ManagedEntity entity;

    public EntityWrapper(final ManagedEntity entity) {
        final RpgPlus plugin = RpgPlus.getPlugin(RpgPlus.class);
        this.entity = entity;

        set("navigateTo", new VarArgFunction() {
            @Override
            public Varargs invoke(final Varargs varargs) {
                final LuaTable destination = varargs.arg(2).checktable();
                final LuaTable options;
                Runnable callback = null;
                if (varargs.narg() == 4) {
                    options = varargs.arg(3).checktable();
                    callback = new Runnable() {
                        @Override
                        public void run() {
                            varargs.arg(4).invoke(destination);
                        }
                    };
                } else {
                    if (varargs.isfunction(3)) {
                        options = new LuaTable();
                        callback = new Runnable() {
                            @Override
                            public void run() {
                                varargs.arg(3).invoke(destination);
                            }
                        };
                    } else {
                        options = varargs.arg(3).opttable(new LuaTable());
                    }
                }
                try {
                    PathingResult result = RpgPlus.getPlugin(RpgPlus.class).getPathfinding().navigate(
                            entity.getEntity(),
                            new Location(entity.getEntity().getWorld(),
                                    destination.get("x").checkdouble(),
                                    destination.get("y").checkdouble(),
                                    destination.get("z").checkdouble()
                            ),
                            options.get("speed").optint(10),
                            PathingBehaviours.builder()
                                    .openDoors(options.get("openDoors").optboolean(false))
                                    .openFenceGates(options.get("openFenceGates").optboolean(false))
                                    .build(),
                            callback);
                    return result == PathingResult.SUCCESS ? LuaValue.TRUE : LuaValue.FALSE;
                } catch (AStar.InvalidPathException e) {
                    return LuaValue.FALSE;
                }
            }
        });
        set("ask", new VarArgFunction() {
            @Override
            public Varargs invoke(final Varargs varargs) {
                final Player player = ScriptUtil.getPlayer(varargs.arg(2));
                final LuaFunction callback = varargs.checkfunction(4);
                final AtomicBoolean returnValue = new AtomicBoolean(false);
                final AtomicReference<Runnable> ask = new AtomicReference<Runnable>();
                ask.set(new Runnable() {
                    @Override
                    public void run() {
                        plugin.getDialogs().ask(entity.getName(), player, messageAlternatives(varargs.arg(3)), new AnswerHandler() {
                            @Override
                            public void handleAnswer(final Player player, String answer) {
                                Varargs handled = callback.invoke(LuaValue.valueOf(answer), new OneArgFunction() {
                                    @Override
                                    public LuaValue call(LuaValue message) {
                                        plugin.getDialogs().tell(entity.getName(), player, messageAlternatives(message));
                                        return LuaValue.NIL;
                                    }
                                });
                                if (!handled.optboolean(1, true)) {
                                    ask.get().run();
                                }
                            }
                        });
                    }
                });
                ask.get().run();
                return LuaValue.NIL;
            }
        });

        set("tell", new VarArgFunction() {
            @Override
            public Varargs invoke(Varargs varargs) {
                Player player = ScriptUtil.getPlayer(varargs.arg(2));
                for (int i = 3; i <= varargs.narg(); i++) {
                    plugin.getDialogs().tell(entity.getName(), player, messageAlternatives(varargs.arg(i)));
                }

                return LuaValue.NIL;
            }
        });
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "health":
                    if (entity instanceof Damageable) {
                        return LuaValue.valueOf(((Damageable) entity).getHealth());
                    }
            }
        }
        return super.rawget(key);
    }

    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "health":
                    if (entity instanceof Damageable) {
                        ((Damageable) entity).setHealth(value.checkdouble());
                    }
            }
        }
        super.rawset(key, value);
    }

    private String messageAlternatives(LuaValue messages) {
        if (messages.istable()) {
            return messages.checktable().get(random.nextInt(messages.length()) + 1).checkjstring();
        } else {
            return messages.checkjstring();
        }
    }

    public ManagedEntity getEntity() {
        return entity;
    }

    public static LuaValue wrap(ManagedEntity entity) {
        return new EntityWrapper(entity);
    }
}
