package de.craften.plugins.rpgplus.components.phasing;

import org.bukkit.World;

/**
 * A finder that searches for places to put phased regions.
 */
public class RegionFinder {
    private final World world;
    private int x = 0;

    /**
     * Creates a new region finder.
     *
     * @param world world to use for phased regions (this world can't be used for other things
     */
    public RegionFinder(World world) {
        this.world = world;
    }

    /**
     * Gets a region with the given width and length.
     *
     * @param width  width
     * @param length length
     * @return a region with the given width and length
     */
    public RectangularRegion getRegion(int width, int length) {
        x += width; //TODO this is a very inefficient and naive way to find space
        return new RectangularRegion(world, x, 0, width, length);
    }
}
