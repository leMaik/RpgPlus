package de.craften.plugins.rpgplus.components.phasing;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * A rectangular region.
 */
public class RectangularRegion {
    private final World world;
    private final int x;
    private final int z;
    private final int width;
    private final int length;

    /**
     * Create a new rectangular region.
     *
     * @param world  world
     * @param x      x coordinate of the north west corner
     * @param z      z coordinate of the north west corner
     * @param width  width of the region
     * @param length length of the region
     */
    public RectangularRegion(World world, int x, int z, int width, int length) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.width = width;
        this.length = length;
    }

    /**
     * Get the x-coordinate of the top left corner of this region.
     *
     * @return x-coordinate of the top left corner
     */
    public int getX() {
        return x;
    }

    /**
     * Get the z-coordinate of the top left corner of this region.
     *
     * @return z-coordinate of the top left corner
     */
    public int getZ() {
        return z;
    }

    /**
     * Get the world of this region.
     *
     * @return the world of this region
     */
    public World getWorld() {
        return world;
    }

    /**
     * Get the width of this region.
     *
     * @return the width of this region
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the length of this region.
     *
     * @return the length of this region
     */
    public int getLength() {
        return length;
    }

    /**
     * Get a location inside this region.
     *
     * @param deltaX x offset from the north west corner
     * @param y      absolute y coordinate
     * @param deltaZ z offset from the north west corner
     * @return location with the specified x and z offset and y coordinate
     */
    public Location getLocation(double deltaX, double y, double deltaZ) {
        return new Location(world, x + deltaX, y, z + deltaZ);
    }

    /**
     * Check if this region contains the given location.
     *
     * @param location a location
     * @return true if this region contains the given location, false if not
     */
    public boolean contains(Location location) {
        return world.equals(location.getWorld()) && location.getX() >= x && location.getZ() >= z &&
                location.getX() <= x + width && location.getZ() <= z + length;
    }

    /**
     * Get a block inside this region.
     *
     * @param deltaX x offset from the north west corner
     * @param y      absolute y coordinate
     * @param deltaZ z offset from the north west corner
     * @return block at specified x and z offset and y coordinate
     */
    public Block getBlockRelative(int deltaX, int y, int deltaZ) {
        return world.getBlockAt(x + deltaX, y, z + deltaZ);
    }
}
