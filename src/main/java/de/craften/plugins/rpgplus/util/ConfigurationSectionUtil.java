package de.craften.plugins.rpgplus.util;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility methods for {@link ConfigurationSection}s.
 */
public class ConfigurationSectionUtil {
    /**
     * Flattens the given section to a map that contains the full keys and string values.
     *
     * @param section section to flatten
     * @return flattened section as map
     */
    public static Map<String, String> flatten(ConfigurationSection section) {
        Map<String, String> map = new HashMap<>();
        flattenInto(section, map, null);
        return map;
    }

    private static void flattenInto(ConfigurationSection section, Map<String, String> map, String parentKey) {
        if (section == null) {
            return;
        }
        for (String key : section.getKeys(false)) {
            if (section.isConfigurationSection(key)) {
                flattenInto(section.getConfigurationSection(key), map, parentKey == null ? key : (parentKey + "." + key));
            } else {
                map.put(parentKey == null ? key : (parentKey + "." + key), section.getString(key));
            }
        }
    }
}
