package de.craften.plugins.rpgplus.scripting.api.actionbar;

import de.craften.plugins.rpgplus.RpgPlus;
import de.craften.plugins.rpgplus.util.nms.NmsUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ActionBar {
    private Object packet;
    private Object connection;
    private int age = 0;
    private int scheduler = -1;

    public ActionBar(Player p, String msg, final int duration) {
        try {
            Object chat = NmsUtil.getNmsClass("IChatBaseComponent$ChatSerializer")
                    .getMethod("a", new Class[]{String.class})
                    .invoke(null, "{'text': '" + msg + "'}");
            packet = NmsUtil.getNmsClass("PacketPlayOutChat")
                    .getConstructor(new Class[]{NmsUtil.getNmsClass("IChatBaseComponent"), Byte.TYPE})
                    .newInstance(chat, (byte) 2);
            Object nmsPlayer = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
            connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.scheduler = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(RpgPlus.getPlugin(RpgPlus.class), new Runnable() {

            @Override
            public void run() {
                if (duration > 0) {
                    age++;
                    if (age >= duration - 2) {
                        end();
                    }
                }

                try {
                    connection.getClass()
                            .getMethod("sendPacket", new Class[]{NmsUtil.getNmsClass("Packet")})
                            .invoke(connection, packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 20, 0);

    }

    public void end() {
        Bukkit.getServer().getScheduler().cancelTask(scheduler);
        scheduler = -1;
    }
}
