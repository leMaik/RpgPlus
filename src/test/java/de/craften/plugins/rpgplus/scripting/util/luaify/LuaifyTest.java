package de.craften.plugins.rpgplus.scripting.util.luaify;

import org.junit.Test;
import org.luaj.vm2.*;

import static org.junit.Assert.*;

public class LuaifyTest {
    @Test
    public void testConvert() throws Exception {
        LuaTable table = new LuaTable() {
            @LuaFunction("testFunction")
            public Varargs testFunction(Varargs varargs) {
                return LuaValue.valueOf(2 * varargs.checkint(1));
            }
        };
        Luaify.convert(table);

        assertTrue("function should exist on that table", table.get("testFunction").isfunction());
        assertEquals("function should work correctly", 42, table.get("testFunction").invoke(LuaValue.valueOf(21)).checkint(1));
    }

    @Test
    public void testConvertScope() throws Exception {
        LuaTable table = new LuaTable();
        Luaify.convert(new Object() {
            private int i = 1;

            @LuaFunction("testFunction")
            public Varargs testFunction(Varargs varargs) {
                return LuaValue.valueOf(i++);
            }
        }, table);

        assertEquals("function should work correctly", 1, table.get("testFunction").invoke().checkint(1));
        assertEquals("function should work correctly and be called on the correct object", 2, table.get("testFunction").invoke().checkint(1));
    }

    @Test
    public void testNonPublicMethods() throws Exception {
        LuaTable table = new LuaTable();
        Luaify.convert(new Object() {
            @LuaFunction("testFunction")
            private void testFunction(Varargs varargs) {
            }

            @LuaFunction("testFunction2")
            protected void testFunction2(Varargs varargs) {
            }
        }, table);

        assertFalse("private functions should not be converted", table.get("testFunction").isfunction());
        assertFalse("protected functions should not be converted", table.get("testFunction2").isfunction());
    }

    @Test
    public void testVarargs() throws Exception {
        LuaTable table = new LuaTable();
        Luaify.convert(new Object() {
            @LuaFunction("add")
            public Varargs testFunction(Varargs varargs) {
                return LuaValue.valueOf(varargs.checkint(1) + varargs.checkint(2));
            }
        }, table);

        assertEquals("function should work correctly", 3, table.get("add").invoke(LuaValue.valueOf(1), LuaValue.valueOf(2)).checkint(1));
    }

    @Test
    public void testMultipleArguments() throws Exception {
        LuaTable table = new LuaTable();
        Luaify.convert(new Object() {
            @LuaFunction("add")
            public Varargs testFunction(LuaValue a, LuaValue b) {
                return LuaValue.valueOf(a.checkint() + b.checkint());
            }
        }, table);

        assertEquals("function should work correctly", 3, table.get("add").invoke(LuaValue.valueOf(1), LuaValue.valueOf(2)).checkint(1));
    }

    @Test
    public void testVoidMethods() throws Exception {
        LuaTable table = new LuaTable();
        Luaify.convert(new Object() {
            @LuaFunction("test")
            public void testFunction() {
            }
        }, table);

        assertEquals("void methods should return LuaValue.NONE", LuaValue.NONE, table.get("test").invoke());
    }

    @Test(expected = LuaError.class)
    public void testInvalidArguments() throws Exception {
        LuaTable table = new LuaTable();
        Luaify.convert(new Object() {
            @LuaFunction("test")
            public void testFunction(LuaNumber number) {
            }
        }, table);

        assertEquals("void methods should return LuaValue.NONE", LuaValue.NONE, table.get("test").invoke(LuaValue.valueOf("test")));
    }
}