package de.craften.plugins.rpgplus.scripting.api.title;

import de.craften.plugins.rpgplus.util.nms.NmsUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public final class TitleApi {
    private TitleApi() {
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", NmsUtil.getNmsClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        try {
            if (title != null) {
                title = ChatColor.translateAlternateColorCodes('&', title);
                title = title.replaceAll("%player%", player.getDisplayName());
                Object enumTitle = NmsUtil.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                Object chatTitle = NmsUtil.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor<?> titleConstructor = NmsUtil.getNmsClass("PacketPlayOutTitle").getConstructor(NmsUtil.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0], NmsUtil.getNmsClass("IChatBaseComponent"), int.class, int.class, int.class);
                Object titlePacket = titleConstructor.newInstance(enumTitle, chatTitle, fadeIn, stay, fadeOut);
                sendPacket(player, titlePacket);
            }

            if (subtitle != null) {
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
                subtitle = subtitle.replaceAll("%player%", player.getDisplayName());
                Object enumSubtitle = NmsUtil.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                Object chatSubtitle = NmsUtil.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                Constructor<?> subtitleConstructor = NmsUtil.getNmsClass("PacketPlayOutTitle").getConstructor(NmsUtil.getNmsClass("PacketPlayOutTitle").getDeclaredClasses()[0], NmsUtil.getNmsClass("IChatBaseComponent"), int.class, int.class, int.class);
                Object subtitlePacket = subtitleConstructor.newInstance(enumSubtitle, chatSubtitle, fadeIn, stay, fadeOut);
                sendPacket(player, subtitlePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
        sendTitle(player, fadeIn, stay, fadeOut, message, null);
    }

    public static void sendTabTitle(Player player, String header, String footer) {
        if (header == null) header = "";
        header = ChatColor.translateAlternateColorCodes('&', header);

        if (footer == null) footer = "";
        footer = ChatColor.translateAlternateColorCodes('&', footer);

        header = header.replaceAll("%player%", player.getDisplayName());
        footer = footer.replaceAll("%player%", player.getDisplayName());

        try {
            Object tabHeader = NmsUtil.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + header + "\"}");
            Object tabFooter = NmsUtil.getNmsClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + footer + "\"}");
            Constructor<?> titleConstructor = NmsUtil.getNmsClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(NmsUtil.getNmsClass("IChatBaseComponent"));
            Object packet = titleConstructor.newInstance(tabHeader);
            Field field = packet.getClass().getDeclaredField("b");
            field.setAccessible(true);
            field.set(packet, tabFooter);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void resetTitles(Player player) {
        try {
            Class<?> packetTitle = NmsUtil.getNmsClass("PacketPlayOutTitle");
            Class<?> packetActions = NmsUtil.getNmsClass("PacketPlayOutTitle$EnumTitleAction");
            Class<?> chatBaseComponent = NmsUtil.getNmsClass("IChatBaseComponent");
            Object[] actions = packetActions.getEnumConstants();

            Object packet = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(actions[3], null);
            sendPacket(player, packet);

            packet = packetTitle.getConstructor(packetActions, chatBaseComponent).newInstance(actions[4], null);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //There is no other way to reset tab titles, they can only be set to empty
        sendTabTitle(player, "", "");
    }
}