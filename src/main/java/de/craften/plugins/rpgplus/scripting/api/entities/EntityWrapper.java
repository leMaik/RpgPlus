package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.dialogs.AnswerHandler;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedEntity;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.AStar;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.PathingBehaviours;
import de.craften.plugins.rpgplus.components.pathfinding.pathing.PathingResult;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

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

        set("teleportTo", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue entityArg, LuaValue destinationArg) {
                LuaTable destination = destinationArg.checktable();
                entity.moveTo(new Location(
                        Bukkit.getWorld(destination.get("world").optjstring(entity.getLocalLocation().getWorld().getName())),
                        destination.get("x").checkdouble(),
                        destination.get("y").checkdouble(),
                        destination.get("z").checkdouble()
                ));
                return LuaValue.NIL;
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
                        RpgPlus.getPlugin(RpgPlus.class).getDialogs().ask(entity.getName(), player, messageAlternatives(varargs.arg(3)), new AnswerHandler() {
                            @Override
                            public void handleAnswer(final Player player, String answer) {
                                Varargs handled = callback.invoke(LuaValue.valueOf(answer), new OneArgFunction() {
                                    @Override
                                    public LuaValue call(LuaValue message) {
                                        RpgPlus.getPlugin(RpgPlus.class).getDialogs().tell(entity.getName(), player, messageAlternatives(message));
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
                    RpgPlus.getPlugin(RpgPlus.class).getDialogs().tell(entity.getName(), player, messageAlternatives(varargs.arg(i)));
                }

                return LuaValue.NIL;
            }
        });

        set("on", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue entity, LuaValue eventName, LuaValue callback) {
                return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().getEntityEventManager().on(entity, eventName, callback);
            }
        });

        set("off", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue entity, LuaValue eventName, LuaValue callback) {
                return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().getEntityEventManager().off(entity, eventName, callback);
            }
        });

        set("despawn", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                entity.despawn();
                return LuaValue.NIL;
            }
        });

        set("respawn", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                entity.spawn();
                return LuaValue.NIL;
            }
        });

        set("kill", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                entity.kill();
                return LuaValue.NIL;
            }
        });
    }

    @Override
    public LuaValue rawget(LuaValue key) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "health":
                    if (entity.getEntity() instanceof Damageable) {
                        return LuaValue.valueOf(((Damageable) entity.getEntity()).getHealth());
                    }
                    break;
                case "maxHealth":
                    if (entity.getEntity() instanceof Damageable) {
                        return LuaValue.valueOf(((Damageable) entity.getEntity()).getMaxHealth());
                    }
                    break;
                case "name":
                    return LuaValue.valueOf(entity.getName());
                case "secondName":
                    return LuaValue.valueOf(entity.getSecondName());
                case "invulnerable":
                    return LuaValue.valueOf(!entity.isTakingDamage());
                case "nameVisible":
                    return LuaValue.valueOf(entity.isNameVisible());
            }
        }
        return super.rawget(key);
    }

    @Override
    public void rawset(LuaValue key, LuaValue value) {
        if (key.isstring()) {
            switch (key.checkjstring()) {
                case "health":
                    if (entity.getEntity() instanceof Damageable) {
                        ((Damageable) entity.getEntity()).setHealth(value.checkdouble());
                    }
                    break;
                case "maxHealth":
                    if (entity.getEntity() instanceof Damageable) {
                        ((Damageable) entity.getEntity()).setMaxHealth(value.checkdouble());
                    }
                    break;
                case "name":
                    entity.setName(value.checkjstring());
                    break;
                case "secondName":
                    entity.setSecondName(value.checkjstring());
                    break;
                case "invulnerable":
                    entity.setIsTakingDamage(!value.checkboolean());
                    break;
                case "nameVisible":
                    entity.setNameVisible(value.checkboolean());
                    break;
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
