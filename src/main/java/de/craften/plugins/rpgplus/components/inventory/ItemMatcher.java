package de.craften.plugins.rpgplus.components.inventory;

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

    public boolean matches(ItemStack itemStack, boolean ignoreAmount) {
        return typeMatches(itemStack)
                && dataMatches(itemStack)
                && (ignoreAmount || amountMatches(itemStack))
                && nameMatches(itemStack)
                && loreMatches(itemStack);
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

    public static Builder builder() {
        return new Builder();
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

        public ItemMatcher build() {
            return matcher;
        }
    }
}
