package de.craften.plugins.rpgplus.components.inventory;

import de.craften.plugins.rpgplus.util.CustomSkull;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A matcher for {@link org.bukkit.inventory.ItemStack}s.
 */
public class ItemMatcher {
    private Material type;
    private Integer data;
    private Integer amount;
    private String name;
    private List<String> lore;
    private boolean unbreakable;
    private String skullTexture;
    private String bookTitle;
    private String bookAuthor;
    private List<String> bookPages;
    private boolean hideFlags;
    
    public boolean matches(ItemStack itemStack, boolean ignoreAmount) {
    	
    	boolean isSkull = itemStack.getType() == Material.SKULL_ITEM;
    	
        return typeMatches(itemStack)
                && dataMatches(itemStack)
                && (ignoreAmount || amountMatches(itemStack))
                && (!unbreakable || (itemStack.hasItemMeta() && itemStack.getItemMeta().spigot().isUnbreakable() == unbreakable))
                && nameMatches(itemStack)
                && loreMatches(itemStack)
                && (!isSkull || skullTextureMatches(itemStack))
                && bookPropertiesMatch(itemStack);
    }

    public boolean matches(Inventory inventory) {
        int amount = this.amount != null ? this.amount : 1;
        for (ItemStack itemStack : inventory) {
            if (itemStack != null && matches(itemStack, true)) {
                amount -= itemStack.getAmount();
            }
            if (amount <= 0) {
                return true;
            }
        }
        return false;
    }

    public int putInto(Inventory inventory) {
        Map<Integer, ItemStack> remaining = inventory.addItem(getItemStack());
        return remaining.isEmpty() ? 0 : remaining.get(0).getAmount();
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(type, amount != null ? amount : 1, data != null ? data.shortValue() : 0);
        ItemMeta meta = Bukkit.getItemFactory().getItemMeta(itemStack.getType());

        if (name != null) {
            meta.setDisplayName(name);
        }
        if (lore != null) {
            meta.setLore(lore);
        }
        if (unbreakable) {
            meta.spigot().setUnbreakable(true);
        }
        if (skullTexture != null) {
            CustomSkull.setTexture(meta, skullTexture);
        }

        if (meta instanceof BookMeta) {
            BookMeta book = (BookMeta) meta;
            if (bookTitle != null) {
                book.setTitle(bookTitle);
            }
            if (bookAuthor != null) {
                book.setAuthor(bookAuthor);
            }
            if (bookPages != null) {
                book.setPages(bookPages);
            }
        }
        
        if (hideFlags) {
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
        }
        
        itemStack.setItemMeta(meta);

        return itemStack;
    }

    public int takeFrom(Inventory inventory) {
        int amount = this.amount != null ? this.amount : 1;
        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            ItemStack itemStack = contents[i];
            if (matches(itemStack, true)) {
                if (itemStack.getAmount() <= amount) {
                    amount -= itemStack.getAmount();
                    contents[i] = null;
                } else {
                    itemStack.setAmount(itemStack.getAmount() - amount);
                    amount = 0;
                }
            }
            if (amount == 0) {
                break;
            }
        }
        inventory.setContents(contents);
        return amount;
    }

    private boolean typeMatches(ItemStack itemStack) {
        return (itemStack == null && type == Material.AIR) || (itemStack != null && itemStack.getType() == type);
    }

    private boolean dataMatches(ItemStack itemStack) {
        return data == null || itemStack.getData().getData() == data;
    }

    private boolean amountMatches(ItemStack itemStack) {
        return amount == null || itemStack.getAmount() == amount;
    }

    private boolean nameMatches(ItemStack itemStack) {
        return name == null || (itemStack.hasItemMeta() && itemStack.getItemMeta().getDisplayName().equals(name));
    }

    private boolean loreMatches(ItemStack itemStack) {
        if (lore == null) {
            return true;
        }

        List<String> actualLore = itemStack.hasItemMeta() ? itemStack.getItemMeta().getLore() : Collections.emptyList();
        if (actualLore.size() != lore.size()) {
            return false;
        }

        for (int i = 0; i < actualLore.size(); i++) {
            if (!actualLore.get(i).equals(lore.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean skullTextureMatches(ItemStack itemStack) {
        return itemStack.getType() == Material.SKULL_ITEM && (skullTexture == null || skullTexture.equalsIgnoreCase(CustomSkull.getTexture(itemStack)));
    }

    private boolean bookPropertiesMatch(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        if (meta instanceof BookMeta) {
            BookMeta book = (BookMeta) meta;
            if (bookTitle != null && !bookTitle.equals(book.getTitle())) {
                return false;
            }
            if (bookAuthor != null && !bookAuthor.equals(book.getAuthor())) {
                return false;
            }
            if (bookPages != null && !bookPages.equals(book.getPages())) {
                return false;
            }
        }

        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * Creates a new item matcher from the given string item stack representation.
     *
     * @param idColonData string of the format <code>id[:data[:amount]]</code>
     * @return item matcher for the specified item stack
     * @throws IllegalArgumentException if the string has the wrong format
     */
    public static ItemMatcher fromString(String idColonData) {
        if (idColonData.matches("\\d+(:\\d+(:\\d+)?)?")) {
            String[] parts = idColonData.split(":");

            ItemMatcher itemMatcher = new ItemMatcher();
            itemMatcher.type = Material.getMaterial(Integer.parseInt(parts[0]));

            if (parts.length >= 2) {
                itemMatcher.data = Integer.parseInt(parts[1]);
            }

            if (parts.length == 3) {
                itemMatcher.amount = Integer.parseInt(parts[2]);
            }

            return itemMatcher;
        } else {
            throw new IllegalArgumentException("String '" + idColonData + "' has illegal format, expected 'id:data'");
        }
    }

    public static class Builder {
        private final ItemMatcher matcher;

        private Builder() {
            matcher = new ItemMatcher();
        }

        public Builder type(Material type) {
            matcher.type = type;
            return this;
        }

        public Builder data(Integer data) {
            matcher.data = data;
            return this;
        }

        public Builder amount(Integer amount) {
            matcher.amount = amount;
            return this;
        }

        public Builder name(String name) {
            matcher.name = name;
            return this;
        }

        public Builder lore(List<String> lore) {
            matcher.lore = lore;
            return this;
        }

        public Builder unbreakable(boolean unbreakable) {
            matcher.unbreakable = unbreakable;
            return this;
        }

        public Builder skullTexture(String skullTexture) {
            matcher.skullTexture = skullTexture;
            return this;
        }

        public Builder bookTitle(String title) {
            matcher.bookTitle = title;
            return this;
        }

        public Builder bookPages(List<String> pages) {
            matcher.bookPages = pages;
            return this;
        }

        public Builder bookAuthor(String author) {
            matcher.bookAuthor = author;
            return this;
        }
        
        public Builder hideFlags(boolean hideFlags) {
            matcher.hideFlags = hideFlags;
            return this;
        }
        
        public ItemMatcher build() {
            return matcher;
        }
    }
}
