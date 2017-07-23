package de.craften.plugins.rpgplus.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.UUID;

/**
 * Methods to create custom skulls.
 */
public class CustomSkull {
    public static ItemStack getSkullUrl(String skinURL) {
        return getSkullBase64(Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinURL).getBytes()));
    }

    private static ItemStack getSkullBase64(byte[] encoded) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        setTexture(head, new String(encoded));
        return head;
    }

    public static ItemStack getSkullBase64(String encoded) {
        return getSkullBase64(encoded.getBytes());
    }

    public static String getTexture(ItemStack itemStack) {
        ItemMeta skullMeta = itemStack.getItemMeta();
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            GameProfile profile = (GameProfile) profileField.get(skullMeta);
            if (profile != null) {
                Collection<Property> textures = profile.getProperties().get("textures");
                if (textures.size() > 0) {
                    return textures.iterator().next().getValue();
                }
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setTexture(ItemStack itemStack, String texture) {
        ItemMeta skullMeta = itemStack.getItemMeta();
        setTexture(skullMeta, texture);
        itemStack.setItemMeta(skullMeta);
    }

    public static void setTexture(ItemMeta skullMeta, String texture) {
        GameProfile profile = new GameProfile(UUID.nameUUIDFromBytes(texture.getBytes()), null);
        profile.getProperties().put("textures", new Property("textures", texture));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}