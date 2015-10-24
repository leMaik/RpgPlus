package de.craften.plugins.rpgplus.util;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ConfigurationSectionUtilTest {

    @Test
    public void testFlatten() throws Exception {
        ConfigurationSection config = new MemoryConfiguration();
        config.set("foo", "bar");
        config.set("deeply.nested.property", "value");

        Map<String, String> flattened = ConfigurationSectionUtil.flatten(config);
        assertEquals(2, flattened.size());
        assertEquals("bar", flattened.get("foo"));
        assertEquals("value", flattened.get("deeply.nested.property"));
    }
}