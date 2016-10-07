package de.craften.plugins.rpgplus.components.cinematic;

import org.bukkit.Location;

/**
 * A keyframe.
 */
public class Keyframe {
    private final double time;
    private final double x;
    private final double y;
    private final double z;
    private final double yaw;
    private final double pitch;

    public Keyframe(double time, double x, double y, double z, double yaw, double pitch) {
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Keyframe(double time, Location location) {
        this(time, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
    }

    public double getTime() {
        return time;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public Keyframe shift(double timeDelta) {
        return new Keyframe(time + timeDelta, x, y, z, yaw, pitch);
    }
}
