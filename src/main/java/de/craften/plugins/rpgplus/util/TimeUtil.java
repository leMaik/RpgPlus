package de.craften.plugins.rpgplus.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for Minecraft times.
 */
public class TimeUtil {
    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d?\\d):(\\d{2})");

    public static int ticksOfTime(String time) throws IllegalArgumentException {
        Matcher matcher = TIME_PATTERN.matcher(time);
        if (matcher.matches()) {
            int t = 50 * 20 * (Integer.parseInt(matcher.group(1)) - 6) + (int) (50d / 60d * 20d * Integer.parseInt(matcher.group(2)));
            return t >= 0 ? t : 24_000 + t;
        } else {
            throw new IllegalArgumentException(time + " is not a valid time string");
        }
    }
}
