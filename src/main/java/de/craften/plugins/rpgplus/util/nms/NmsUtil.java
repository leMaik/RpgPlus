package de.craften.plugins.rpgplus.util.nms;

import org.bukkit.Bukkit;

/**
 * NMS utility methods. These may break with new Bukkit versions.
 */
public class NmsUtil {
    private static final String NMSVER;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        NMSVER = packageName.substring(packageName.lastIndexOf(".") + 1);
    }

    public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + NMSVER + "." + nmsClassName);
    }
}
