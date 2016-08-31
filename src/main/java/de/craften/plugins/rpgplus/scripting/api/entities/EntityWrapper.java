package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.dialogs.AnswerHandler;
import de.craften.plugins.rpgplus.components.dialogs.ChoiceAnswerHandler;
import de.craften.plugins.rpgplus.components.entitymanager.ManagedRabbit;
import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.npc.ai.CitizensNavigator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A lua wrapper for a {@link RpgPlusEntity}.
 */
public class EntityWrapper<T extends Entity> extends LuaTable {
    private static final Random random = new Random();
    protected final RpgPlusEntity<T> entity;
    private final EntityEventManager entityEventManager;

    protected EntityWrapper(final RpgPlusEntity<T> entity, EntityEventManager entityEventManager) {
        this.entity = entity;
        this.entityEventManager = entityEventManager;

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

                entity.getNpc().getNavigator()
                        .setTarget(new Location(entity.getEntity().getWorld(),
                                destination.get("x").checkdouble(),
                                destination.get("y").checkdouble(),
                                destination.get("z").checkdouble()
                        ));

                if (options.get("speed").isint()) {
                    entity.getNpc().getNavigator().getLocalParameters().baseSpeed(options.get("speed").checkint());
                }
                if (options.get("openDoors").isboolean() && options.get("openDoors").checkboolean()) {
                    // TODO implement our own door examiner to open fence gates or doors only (add support for openFenceGates)
                    entity.getNpc().getNavigator().getLocalParameters().examiner(new CitizensNavigator.DoorExaminer());
                }
                // TODO call the callback after navigation (and include the success there!)
                return LuaValue.valueOf(entity.getNpc().getNavigator().isNavigating());
            }
        });

        set("teleportTo", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue entityArg, LuaValue destinationArg) {
                LuaTable destination = destinationArg.checktable();
                entity.teleport(new Location(
                        Bukkit.getWorld(destination.get("world").optjstring(entity.getNpc().getStoredLocation().getWorld().getName())),
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
                final AtomicReference<Runnable> ask = new AtomicReference<Runnable>();
                ask.set(new Runnable() {
                    @Override
                    public void run() {
                        RpgPlus.getPlugin(RpgPlus.class).getDialogs().ask(entity.getName(), player, messageAlternatives(varargs.arg(3)), new AnswerHandler() {
                            @Override
                            public boolean handleAnswer(final Player player, String answer) {
                                Varargs handled = RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(
                                        callback, LuaValue.valueOf(answer), new OneArgFunction() {
                                            @Override
                                            public LuaValue call(LuaValue message) {
                                                RpgPlus.getPlugin(RpgPlus.class).getDialogs().tell(entity.getName(), player, messageAlternatives(message));
                                                return LuaValue.NIL;
                                            }
                                        });
                                if (!handled.optboolean(1, true)) {
                                    ask.get().run();
                                }
                                return true;
                            }
                        });
                    }
                });
                ask.get().run();
                return LuaValue.NIL;
            }
        });

        set("askChoices", new VarArgFunction() {
            @Override
            public Varargs invoke(final Varargs varargs) {
                final Player player = ScriptUtil.getPlayer(varargs.arg(2));
                final LuaFunction callback = varargs.checkfunction(5);
                final AtomicReference<Runnable> ask = new AtomicReference<Runnable>();
                ask.set(new Runnable() {
                    @Override
                    public void run() {
                        String[] choices = new String[varargs.checktable(4).length()];
                        for (int i = 1; i <= varargs.checktable(4).length(); i++) {
                            choices[i - 1] = messageAlternatives(varargs.checktable(4).get(i));
                        }
                        RpgPlus.getPlugin(RpgPlus.class).getDialogs().askChoices(entity.getName(), player, messageAlternatives(varargs.arg(3)), choices, new ChoiceAnswerHandler() {
                            @Override
                            public boolean handleAnswer(final Player player, int i, String answer) {
                                Varargs handled = RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(
                                        callback, LuaValue.valueOf(answer), LuaValue.valueOf(i + 1), new OneArgFunction() {
                                            @Override
                                            public LuaValue call(LuaValue message) {
                                                RpgPlus.getPlugin(RpgPlus.class).getDialogs().tell(entity.getName(), player, messageAlternatives(message));
                                                return LuaValue.NIL;
                                            }
                                        });
                                if (!handled.optboolean(1, true)) {
                                    ask.get().run();
                                }
                                return true;
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
                return EntityWrapper.this.entityEventManager.on(entity, eventName, callback);
            }
        });

        set("once", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue entity, LuaValue eventName, LuaValue callback) {
                return EntityWrapper.this.entityEventManager.once(entity, eventName, callback);
            }
        });

        set("off", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue entity, LuaValue eventName, LuaValue callback) {
                return EntityWrapper.this.entityEventManager.off(entity, eventName, callback);
            }
        });

        set("despawn", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                entity.getNpc().despawn(DespawnReason.REMOVAL);
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
                if (entity.getNpc().isSpawned() && entity.getEntity() instanceof Damageable) {
                    ((Damageable) entity.getEntity()).damage(((Damageable) entity.getEntity()).getHealth());
                } else {
                    entity.getNpc().despawn(DespawnReason.REMOVAL);
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
                case "location":
                    return ScriptUtil.getLocation(entity.getNpc().getStoredLocation());
                case "worldName":
                    return LuaValue.valueOf(entity.getEntity().getWorld().getName());
                case "target":
                    return ScriptUtil.getTarget(entity);
                case "type":
                    if (entity instanceof ManagedRabbit) {
                        return LuaValue.valueOf(((ManagedRabbit) entity).getType().toString());
                    }
                    return LuaValue.NIL;
                case "bukkitEntity":
                    return CoerceJavaToLua.coerce(entity.getEntity());
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
                    entity.setName(ChatColor.translateAlternateColorCodes('&', value.checkjstring()));
                    break;
                case "secondName":
                    entity.setSecondName(ChatColor.translateAlternateColorCodes('&', value.checkjstring()));
                    break;
                case "invulnerable":
                    entity.setTakingDamage(!value.checkboolean());
                    break;
                case "nameVisible":
                    entity.setNameVisible(value.checkboolean());
                    break;
                case "target":
                    entity.setTarget(value == LuaValue.NIL ? null : ScriptUtil.getPlayer(value));
                    break;
                case "type":
                    if (entity instanceof ManagedRabbit) {
                        ((ManagedRabbit) entity).setType(ScriptUtil.enumValue(value, Rabbit.Type.class));
                    }
                    break;
            }
        }
        super.rawset(key, value);
    }

    private String messageAlternatives(LuaValue messages) {
        if (messages.istable()) {
            return ChatColor.translateAlternateColorCodes('&', messages.checktable().get(random.nextInt(messages.length()) + 1).checkjstring());
        } else {
            return ChatColor.translateAlternateColorCodes('&', messages.checkjstring());
        }
    }

    /**
     * Gets the wrapped entity.
     *
     * @return the wrapped entity
     */
    public RpgPlusEntity getEntity() {
        return entity;
    }

    /**
     * Gets an entity wrapper for the given entity.
     *
     * @param entity  entity to wrap
     * @param manager entity event manager
     * @return wrapper for the entity
     */
    public static EntityWrapper create(RpgPlusEntity entity, EntityEventManager manager) {
        if (entity.getEntity().getType() == EntityType.HORSE) {
            return new HorseEntityWrapper(entity, manager);
        } else if (entity.getEntity().getType() == EntityType.VILLAGER) {
            return new VillagerEntityWrapper(entity, manager);
        } else if (entity.getEntity().getType() == EntityType.OCELOT) {
            return new OcelotEntityWrapper(entity, manager);
        } else {
            return new EntityWrapper(entity, manager);
        }
    }
}
