package de.icevizion.aves.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public final class ActionBar {

    private final static Class<?> chatserial = getNMSClass("IChatBaseComponent$ChatSerializer");

    private ActionBar() {}

    public static void sendActionBar(Player p, String text) {
        try {
            sendPacket(p, "PacketPlayOutChat", new Class[] { getNMSClass("IChatBaseComponent"), byte.class }, chatserial.getMethod("a", String.class).invoke(null, "{\"text\": \"" + text + "\"}"), (byte)2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendPacket(Player p, String packetName, Class<?>[] parameterclass, Object... parameters) {
        try {
            Object nmsPlayer = getNMSPlayer(p);
            Object connection = nmsPlayer.getClass().getField("playerConnection").get(nmsPlayer);
            Object packet = Class.forName(nmsPlayer.getClass().getPackage().getName() + "." + packetName).getConstructor(parameterclass).newInstance(parameters);
            connection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1) + ".";
    }

    private static Class<?> getNMSClass(String className) {
        String fullName = "net.minecraft.server." + getVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    private static Object getNMSPlayer(Player p) {
        try {
            return p.getClass().getMethod("getHandle").invoke(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}