package de.craften.plugins.rpgplus.scripting.api.entities;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.dialogs.AnswerHandler;
import de.craften.plugins.rpgplus.components.dialogs.ChoiceAnswerHandler;
import de.craften.plugins.rpgplus.components.entitymanager.RpgPlusEntity;
import de.craften.plugins.rpgplus.scripting.api.dialogs.DialogParser;
import de.craften.plugins.rpgplus.scripting.api.entities.events.EntityEventManager;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.ai.Navigator;
import net.citizensnpcs.api.ai.StuckAction;
import net.citizensnpcs.api.ai.event.NavigationCancelEvent;
import net.citizensnpcs.api.ai.event.NavigationCompleteEvent;
import net.citizensnpcs.api.ai.event.NavigationStuckEvent;
import net.citizensnpcs.api.ai.tree.Behavior;
import net.citizensnpcs.api.ai.tree.BehaviorStatus;
import net.citizensnpcs.api.event.DespawnReason;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.npc.ai.CitizensNavigator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
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
                final Location destination = ScriptUtil.getLocation(varargs.arg(2));
                destination.setWorld(entity.getEntity().getWorld());
                final LuaTable options;
                Runnable callback = null;
                if (varargs.narg() == 4) {
                    options = varargs.arg(3).checktable();
                    callback = new Runnable() {
                        @Override
                        public void run() {
                            varargs.arg(4).invoke(ScriptUtil.getLocation(destination));
                        }
                    };
                } else {
                    if (varargs.isfunction(3)) {
                        options = new LuaTable();
                        callback = new Runnable() {
                            @Override
                            public void run() {
                                varargs.arg(3).invoke(ScriptUtil.getLocation(destination));
                            }
                        };
                    } else {
                        options = varargs.arg(3).opttable(new LuaTable());
                    }
                }
                
                entity.getNpc().getNavigator()
                        .setTarget(destination);
                
                boolean hasPath = entity.getNpc().getNavigator().isNavigating();
                final Runnable finishCallback = callback;
                
                RpgPlus.getPlugin(RpgPlus.class).getServer().getPluginManager().registerEvents(new Listener() {
                	@EventHandler
                	public void onNavComplete(NavigationCompleteEvent event) {
                		if (event.getNPC().getUniqueId().equals(entity.getNpc().getUniqueId())) {
                			finishCallback.run();
                			
                			HandlerList.unregisterAll(this);
                		}
                	}
                	
                	@EventHandler
                	public void onNavCancelled(NavigationCancelEvent event) {
                		if (event.getNPC().getUniqueId().equals(entity.getNpc().getUniqueId())) {
                			finishCallback.run();
                			
                			HandlerList.unregisterAll(this);
                		}
                	}
                	
                	@EventHandler
                	public void onStuck(NavigationStuckEvent event) {
                		if (event.getNPC().getUniqueId().equals(entity.getNpc().getUniqueId())) {
                			finishCallback.run();
                			
                			HandlerList.unregisterAll(this);
                			event.setAction(new StuckAction() {
								
								@Override
								public boolean run(NPC arg0, Navigator arg1) {
									return false;
								}
							});
                		}
                	}
				},  RpgPlus.getPlugin(RpgPlus.class));
                
				if (options.get("speed").isnumber()) {
                    entity.getNpc().getNavigator().getLocalParameters().baseSpeed((float)options.get("speed").checkdouble());
                }
                if (options.get("openDoors").isboolean() && options.get("openDoors").checkboolean()) {
                    // TODO implement our own door examiner to open fence gates or doors only (add support for openFenceGates)
                    entity.getNpc().getNavigator().getLocalParameters().examiner(new CitizensNavigator.DoorExaminer());
                }
                
                return LuaValue.valueOf(hasPath);
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

        set("startDialog", new ThreeArgFunction() {
            @Override
            public LuaValue call(LuaValue self, LuaValue player, LuaValue dialogDefinition) {
                RpgPlus.getPlugin(RpgPlus.class).getDialogs()
                        .startDialog(
                                DialogParser.parseDialog(dialogDefinition.checktable()),
                                entity.getName(),
                                ScriptUtil.getPlayer(player)
                        );
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

        set("addBehavior", new TwoArgFunction() {

            @Override
            public LuaValue call(LuaValue arg1, LuaValue arg) {

                if (arg.istable()) {

                    int priority = arg.get("priority").optint(1);

                    LuaValue shouldExecute = arg.get("shouldExecute").optfunction(null);
                    LuaValue run = arg.get("run").optfunction(null);
                    LuaValue reset = arg.get("reset").optfunction(null);

                    Behavior behavior = new Behavior() {

                        @Override
                        public boolean shouldExecute() {
                            if (shouldExecute != null)
                                return RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(shouldExecute).optboolean(1, false);
                            else
                                return false;
                        }

                        @Override
                        public BehaviorStatus run() {
                            if (run != null) {
                                String result = RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(run).optjstring(1, "FAILURE").toUpperCase();
                                if (BehaviorStatus.valueOf(result) != null) {
                                    return BehaviorStatus.valueOf(result);
                                }
                            }

                            return null;
                        }

                        @Override
                        public void reset() {
                            if (reset != null) {
                                RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(reset);
                            }
                        }
                    };

                    entity.getNpc().getDefaultGoalController().addBehavior(behavior, priority);

                }

                return LuaValue.NIL;
            }
        });
        

        set("addTrait", new OneArgFunction() {

            @Override
            public LuaValue call(LuaValue arg) {

                if (arg.istable()) {
                	
                    String name = arg.get("name").checkjstring();
                    LuaValue run = arg.get("run").optfunction(null);
                    LuaValue onSpawn = arg.get("onSpawn").optfunction(null);
                    LuaValue onDespawn = arg.get("onDespawn").optfunction(null);
                    LuaValue onRemove = arg.get("onRemove").optfunction(null);
                    
                    Trait trait = new Trait(name) {
                    	@Override
                    	public void run() {
                    		RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(run);
                    	}
                    	
                    	@Override
                    	public void onSpawn() {
                    		if (onSpawn != null) {
                        		RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(onSpawn);
                    		}
                    	};
                    	
                    	@Override
                    	public void onDespawn() {
                    		if (onDespawn != null) {
                        		RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(onDespawn);
                    		}
                    	};
                    	
                    	@Override
                    	public void onRemove() {
                    		if (onRemove != null) {
                        		RpgPlus.getPlugin(RpgPlus.class).getScriptingManager().invokeSafely(onRemove);
                    		}
                    	};  	
                    	
                    };

                    entity.getNpc().addTrait(trait);

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
                case "bukkitEntity":
                    return CoerceJavaToLua.coerce(entity.getEntity());
                case "npc":
                    return CoerceJavaToLua.coerce(entity.getNpc());
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
        } else if (entity.getEntity().getType() == EntityType.RABBIT) {
            return new RabbitEntityWrapper(entity, manager);
        } else if (entity.getEntity().getType() == EntityType.ARMOR_STAND) {
            return new ArmorStandEntityWrapper(entity, manager);
        } else {
            return new EntityWrapper(entity, manager);
        }
    }
}
