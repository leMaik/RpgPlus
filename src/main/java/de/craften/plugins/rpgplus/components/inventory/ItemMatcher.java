package de.craften.plugins.rpgplus.components.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * A matcher for {@link org.bukkit.inventory.ItemStack}s.
 */
public class ItemMatcher {
    private Material type;
    private Integer data;
    private Integer amount;

    public boolean matches(ItemStack itemStack, boolean ignoreAmount) {
        return typeMatches(itemStack)
                && dataMatches(itemStack)
                && (ignoreAmount || amountMatches(itemStack));
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

    public void putInto(Inventory inventory) {
        inventory.addItem(new ItemStack(type, amount != null ? amount : 1, data != null ? data.shortValue() : 0));
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

        public ItemMatcher build() {
            return matcher;
        }
    }
}
