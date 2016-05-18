package de.craften.plugins.rpgplus.scripting.util;

import org.bukkit.entity.Horse;
import org.junit.Test;
import org.luaj.vm2.LuaValue;

import static org.junit.Assert.assertEquals;

public class ScriptUtilTest {
    @Test
    public void enumValue() throws Exception {
        assertEquals(Horse.Color.BROWN, ScriptUtil.enumValue(LuaValue.valueOf("BROWN"), Horse.Color.class));
        assertEquals(Horse.Color.BROWN, ScriptUtil.enumValue(LuaValue.valueOf("brown"), Horse.Color.class));
    }
}