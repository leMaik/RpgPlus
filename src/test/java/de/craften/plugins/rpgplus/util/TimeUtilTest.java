package de.craften.plugins.rpgplus.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class TimeUtilTest {

    @Test
    public void testTicksOfTime() throws Exception {
        assertEquals(23400, TimeUtil.ticksOfTime("5:24"));
        assertEquals(0, TimeUtil.ticksOfTime("06:00"));
        assertEquals(18000, TimeUtil.ticksOfTime("24:00"));
    }
}