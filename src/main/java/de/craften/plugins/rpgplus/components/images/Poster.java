package de.craften.plugins.rpgplus.components.images;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A poster.
 */
public class Poster {
    private final int width;
    private final int height;
    private final short[] mapIds;

    Poster(int width, int height, List<ItemStack> items) {
        this.width = width;
        this.height = height;
        this.mapIds = new short[width * height];

        for (int i = 0; i < items.size(); i++) {
            mapIds[i] = items.get(i).getDurability();
        }
    }

    /**
     * Gets the width of this poster, in blocks.
     *
     * @return width of this poster, in blocks
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets the height of this poster, in blocks.
     *
     * @return height of this poster, in blocks
     */
    public int getHeight() {
        return height;
    }

    /**
     * Create item stacks for all maps. The resulting list contains them line by line, from top to bottom, then left
     * to right.
     *
     * @return items of this poster
     */
    public List<ItemStack> createItemStacks() {
        ArrayList<ItemStack> items = new ArrayList<>(mapIds.length);
        for (short id : mapIds) {
            items.add(new ItemStack(Material.MAP, 1, id));
        }
        return items;
    }
}
