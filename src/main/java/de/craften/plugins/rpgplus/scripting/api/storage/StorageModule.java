package de.craften.plugins.rpgplus.scripting.api.storage;

import de.craften.plugins.rpgplus.components.storage.Storage;
import de.craften.plugins.rpgplus.components.storage.StorageException;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import de.craften.plugins.rpgplus.scripting.util.ScriptUtil;

import org.bukkit.OfflinePlayer;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.Map;

/**
 * Lua module for storage.
 */
public class StorageModule extends LuaTable implements ScriptingModule {
    private final Storage storage;

    public StorageModule(Storage storage) {
        this.storage = storage;

        set("get", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue key, LuaValue defaultValue) {
                try {
                    if (StorageModule.this.storage.contains(key.checkjstring())) {
                        return LuaValueConverter.convert(StorageModule.this.storage.get(key.checkjstring(), null));
                    } else {
                        Map<String, String> tableMap = StorageModule.this.storage.getAll(key.checkjstring());
                        if (!tableMap.isEmpty()) {
                            return LuaValueConverter.convertToTable(tableMap);
                        }
                        return defaultValue;
                    }
                } catch (StorageException e) {
                    throw new LuaError(e);
                }
            }
        });

        set("set", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue key, LuaValue value) {
                try {
                    if (value.istable()) {
                        for (Map.Entry<String, String> entry : LuaValueConverter.convertFromTable(value.checktable()).entrySet()) {
                            StorageModule.this.storage.set(key.checkjstring() + "." + entry.getKey(), entry.getValue());
                        }
                    } else {
                        StorageModule.this.storage.set(key.checkjstring(), LuaValueConverter.convert(value));
                    }
                    return LuaValue.NIL;
                } catch (StorageException e) {
                    throw new LuaError(e);
                }
            }
        });

        set("of", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue player) {
                return new PlayerTable(ScriptUtil.getOfflinePlayer(player));
            }
        });
        
        set("clear", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                storage.clear();
                return LuaValue.NIL;
            }
        });
    }

    @Override
    public LuaValue getModule() {
        return this;
    }

    @Override
    public void reset() {
        //nothing to do
    }

    private class PlayerTable extends LuaTable {
        public PlayerTable(final OfflinePlayer player) {
            set("get", new TwoArgFunction() {
                @Override
                public LuaValue call(LuaValue key, LuaValue defaultValue) {
                    try {
                        if (storage.contains(player, key.checkjstring())) {
                            return LuaValueConverter.convert(storage.get(player, key.checkjstring(), null));
                        } else {
                            Map<String, String> tableMap = storage.getAll(player, key.checkjstring());
                            if (!tableMap.isEmpty()) {
                                return LuaValueConverter.convertToTable(tableMap);
                            }
                            return defaultValue;
                        }
                    } catch (StorageException e) {
                        throw new LuaError(e);
                    }
                }
            });

            set("set", new TwoArgFunction() {
                @Override
                public LuaValue call(LuaValue key, LuaValue value) {
                    try {
                        if (value.istable()) {
                            for (Map.Entry<String, String> entry : LuaValueConverter.convertFromTable(value.checktable()).entrySet()) {
                                storage.set(key.checkjstring() + "." + entry.getKey(), entry.getValue());
                            }
                        } else {
                            storage.set(player, key.checkjstring(), LuaValueConverter.convert(value));
                        }
                        return LuaValue.NIL;
                    } catch (StorageException e) {
                        throw new LuaError(e);
                    }
                }
            });
            
            set("clear", new ZeroArgFunction() {
                @Override
                public LuaValue call() {
                    storage.clear(player);
                    return LuaValue.NIL;
                }
            });
        }
    }
}
