package de.craften.plugins.rpgplus.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an item. This doesn't need to be a real 'item', but anything that has an id and data.
 */
public class Item {
    private final short id;
    private final byte data;
    private final int amount;

    public Item(int id, int data, int amount) {
        this.id = (short) id;
        this.data = (byte) data;
        this.amount = amount;
    }

    public Item(int id, int data) {
        this(id, data, 1);
    }

    public Item(String idColonData) {
        if (idColonData.matches("\\d+(:\\d+(:\\d+)?)?")) {
            String[] parts = idColonData.split(":");
            id = (short) Integer.parseInt(parts[0]);
            data = parts.length == 2 ? (byte) Integer.parseInt(parts[1]) : 0;
            amount = parts.length == 3 ? (byte) Integer.parseInt(parts[2]) : 0;
        } else
            throw new IllegalArgumentException("String '" + idColonData + "' has illegal format, expected 'id:data'");
    }

    public short getId() {
        return id;
    }

    public byte getData() {
        return data;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public int hashCode() {
        return id * 13 + data;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            Item other = (Item) obj;
            return other.id == id && other.data == data;
        }
        return false;
    }

    @Override
    public String toString() {
        return id + ":" + data;
    }

    /**
     * Gets an item stack of this item.
     *
     * @param amount amount
     * @return item stack with the given amount of items
     */
    public ItemStack getItemStack(int amount) {
        return new ItemStack(id, amount, (short) 0, data);
    }

    /**
     * Gets an item stack with the previously set amount of this item.
     *
     * @return item stack with the previously set amount of this item
     */
    public ItemStack getItemStack() {
        return getItemStack(amount);
    }

    /**
     * Gets the {@link org.bukkit.Material} of this item.
     *
     * @return material of this item
     */
    public Material getMaterial() {
        return Material.getMaterial(id);
    }
}