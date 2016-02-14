package de.craften.plugins.rpgplus.scripting.api.storage;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.components.storage.StorageException;
import de.craften.plugins.rpgplus.scripting.ScriptingModule;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.Map;

/**
 * Lua module for storage.
 */
public class StorageModule extends LuaTable implements ScriptingModule {
    public StorageModule() {
        set("get", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue key, LuaValue defaultValue) {
                try {
                    de.craften.plugins.rpgplus.components.storage.Storage storage = RpgPlus.getPlugin(RpgPlus.class).getStorage();
                    if (storage.contains(key.checkjstring())) {
                        return LuaValueConverter.convert(storage.get(key.checkjstring(), null));
                    } else {
                        Map<String, String> tableMap = storage.getAll(key.checkjstring());
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
                    de.craften.plugins.rpgplus.components.storage.Storage storage = RpgPlus.getPlugin(RpgPlus.class).getStorage();

                    if (value.istable()) {
                        for (Map.Entry<String, String> entry : LuaValueConverter.convertFromTable(value.checktable()).entrySet()) {
                            storage.set(key.checkjstring() + "." + entry.getKey(), entry.getValue());
                        }
                    } else {
                        storage.set(key.checkjstring(), LuaValueConverter.convert(value));
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
                return new PlayerTable(Bukkit.getServer().getPlayer(player.checkjstring()));
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
                        de.craften.plugins.rpgplus.components.storage.Storage storage = RpgPlus.getPlugin(RpgPlus.class).getStorage();

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
                        de.craften.plugins.rpgplus.components.storage.Storage storage = RpgPlus.getPlugin(RpgPlus.class).getStorage();

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
        }
    }
}
