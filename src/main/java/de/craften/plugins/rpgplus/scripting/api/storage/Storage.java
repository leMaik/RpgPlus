package de.craften.plugins.rpgplus.scripting.api.storage;

import de.craften.plugins.rpgplus.RpgPlus;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Lua module for storage.
 */
public class Storage extends LuaTable {
    final de.craften.plugins.rpgplus.components.storage.Storage storage = RpgPlus.getPlugin(RpgPlus.class).getStorage();

    public Storage() {
        set("get", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue key, LuaValue defaultValue) {
                return LuaValue.valueOf(storage.get(key.checkjstring(), defaultValue.checkjstring()));
            }
        });

        set("set", new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue key, LuaValue value) {
                storage.set(key.checkjstring(), key.checkjstring());
                return LuaValue.NIL;
            }
        });

        set("of", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue player) {
                return new PlayerTable(Bukkit.getServer().getPlayer(player.checkjstring()));
            }
        });
    }

    private class PlayerTable extends LuaTable {
        public PlayerTable(final OfflinePlayer player) {
            set("get", new TwoArgFunction() {
                @Override
                public LuaValue call(LuaValue key, LuaValue defaultValue) {
                    return LuaValue.valueOf(storage.get(player, key.checkjstring(), defaultValue.checkjstring()));
                }
            });

            set("set", new TwoArgFunction() {
                @Override
                public LuaValue call(LuaValue key, LuaValue value) {
                    storage.set(player, key.checkjstring(), value.checkjstring());
                    return LuaValue.NIL;
                }
            });
        }
    }
}
