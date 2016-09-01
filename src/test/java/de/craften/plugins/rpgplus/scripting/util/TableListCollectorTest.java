package de.craften.plugins.rpgplus.scripting.util;

import org.junit.Test;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class TableListCollectorTest {
    @Test
    public void testCollector() {
        LuaTable table = IntStream.range(0, 10)
                .mapToObj(LuaValue::valueOf)
                .collect(ScriptUtil.asListTable());
        assertEquals(10, table.length());
        assertEquals(LuaValue.valueOf(0), table.get(1));
        assertEquals(LuaValue.valueOf(9), table.get(10));
    }
}