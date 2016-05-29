package de.craften.plugins.rpgplus.components.inventory;

import de.craften.plugins.rpgplus.util.CustomSkull;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    public boolean matches(ItemStack itemStack, boolean ignoreAmount) {
        return typeMatches(itemStack)
                && dataMatches(itemStack)
                && (ignoreAmount || amountMatches(itemStack))
                && itemStack.getItemMeta().spigot().isUnbreakable() == unbreakable
                && nameMatches(itemStack)
                && loreMatches(itemStack)
                && skullTextureMatches(itemStack);
        //TODO check for more properties, i.e. book content
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
        ItemMeta meta = itemStack.getItemMeta();

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
        return name == null || itemStack.getItemMeta().getDisplayName().equals(name);
    }

    private boolean loreMatches(ItemStack itemStack) {
        if (lore == null) {
            return true;
        }

        List<String> actualLore = itemStack.getItemMeta().getLore();
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
        return skullTexture == null || skullTexture.equalsIgnoreCase(CustomSkull.getTexture(itemStack));
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

        public ItemMatcher build() {
            return matcher;
        }
    }
}
