package de.craften.plugins.rpgplus.scripting.api.storage;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Converter for simple {@link org.luaj.vm2.LuaValue}s.
 */
public class LuaValueConverter {

    public static Map<String, String> convertFromTable(LuaTable table) {
        return convertFromTable(table, "", new HashMap<String, String>());
    }

    private static Map<String, String> convertFromTable(LuaTable table, String targetKey, Map<String, String> targetMap) {
        for (LuaValue key : table.keys()) {
            LuaValue value = table.get(key);
            if (value.istable()) {
                convertFromTable(value.checktable(), targetKey + key.checkjstring() + ".", targetMap);
            } else {
                targetMap.put(targetKey + key.checkjstring(), value.tojstring());
            }
        }
        return targetMap;
    }

    public static LuaTable convertToTable(Map<String, String> sourceMap) {
        LuaTable table = new LuaTable();
        for (Map.Entry<String, String> entry : sourceMap.entrySet()) {
            String[] parts = entry.getKey().split("\\.");
            LuaTable subTable = table;
            for (int i = 0; i < parts.length - 1; i++) {
                LuaValue next = subTable.get(parts[i]);
                if (next.istable()) {
                    subTable = next.checktable();
                } else {
                    next = new LuaTable();
                    subTable.set(parts[i], next);
                    subTable = (LuaTable) next;
                }
            }
            subTable.set(parts[parts.length - 1], convert(entry.getValue()));
        }
        return table;
    }

    private static LuaValue convert(String value) {
        if (value.equalsIgnoreCase("true")) {
            return LuaValue.TRUE;
        } else if (value.equalsIgnoreCase("false")) {
            return LuaValue.FALSE;
        } else if (value.matches("\\-?\\d+(\\.\\d+)?")) {
            return LuaValue.valueOf(Double.valueOf(value));
        } else {
            return LuaValue.valueOf(value);
        }
    }
}
