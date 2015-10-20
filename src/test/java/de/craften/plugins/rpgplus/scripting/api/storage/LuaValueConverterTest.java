package de.craften.plugins.rpgplus.scripting.api.storage;

import org.junit.Test;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class LuaValueConverterTest {

    @Test
    public void testConvertFromTable() throws Exception {
        LuaTable table = new LuaTable();
        table.set("answer", LuaValue.valueOf(42));
        table.set("string", LuaValue.valueOf("hello world"));

        Map<String, String> map = LuaValueConverter.convertFromTable(table);
        assertEquals("42", map.get("answer"));
        assertEquals("hello world", map.get("string"));
        assertEquals(2, map.size());
    }

    @Test
    public void testConvertToTable() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("string", "hello world");
        map.put("answer", "42");

        LuaTable table = LuaValueConverter.convertToTable(map);
        assertEquals(LuaValue.valueOf("hello world"), table.get("string"));
        assertEquals(LuaValue.valueOf(42), table.get("answer"));
    }

    @Test
    public void testTypeConversion() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("boolean", "true");
        map.put("integer", "-1");
        map.put("double", "3.141");
        map.put("string", "hello world");

        LuaTable table = LuaValueConverter.convertToTable(map);
        assertEquals(true, table.get("boolean").checkboolean());
        assertEquals(-1, table.get("integer").checkint());
        assertEquals(3.141, table.get("double").checkdouble(), 0);
        assertEquals("hello world", table.get("string").checkjstring());
    }

    @Test
    public void testSubKeys() throws Exception {
        LuaTable rootTable = new LuaTable();
        LuaTable subtable = new LuaTable();
        subtable.set("bar", "bar");
        subtable.set("answer", 42);
        rootTable.set("foo", subtable);

        Map<String, String> map = LuaValueConverter.convertFromTable(rootTable);
        assertEquals("bar", map.get("foo.bar"));
        assertEquals("42", map.get("foo.answer"));

        LuaTable convertedTable = LuaValueConverter.convertToTable(map);
        assertEquals(LuaValue.valueOf("bar"), convertedTable.get("foo").get("bar"));
        assertEquals(LuaValue.valueOf(42), convertedTable.get("foo").get("answer"));
    }
}