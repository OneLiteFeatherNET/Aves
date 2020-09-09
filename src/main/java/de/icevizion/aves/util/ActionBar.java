package de.icevizion.aves.util;

import org.bukkit.entity.Player;
import org.inventivetalent.reflection.resolver.ConstructorResolver;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
import org.inventivetalent.reflection.resolver.minecraft.OBCClassResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class ActionBar {

    private static final Class<?> iChatBaseComponentClass, chatSerializerClass, packetPlayOutChatClass;
    private static final Field playerConnectionField;
    private static final Method getHandleMethod, sendPacketMethod, serializerAMethod;
    private static final Constructor<?> packetPlayOutChatConstructor;

    static {
        iChatBaseComponentClass = new NMSClassResolver().resolveSilent("IChatBaseComponent");
        chatSerializerClass = new NMSClassResolver().resolveSilent("IChatBaseComponent$ChatSerializer");
        packetPlayOutChatClass = new NMSClassResolver().resolveSilent("PacketPlayOutChat");

        getHandleMethod = new MethodResolver(new OBCClassResolver().resolveSilent("entity.CraftPlayer")).resolveSilent("getHandle");
        playerConnectionField = new FieldResolver(new NMSClassResolver().resolveSilent("EntityPlayer")).resolveSilent("playerConnection");
        sendPacketMethod = new MethodResolver(new NMSClassResolver().resolveSilent("PlayerConnection")).resolveSilent("sendPacket");
        serializerAMethod = new MethodResolver(chatSerializerClass).resolveSilent("a");

        packetPlayOutChatConstructor = new ConstructorResolver(packetPlayOutChatClass).resolveSilent(new Class[] { iChatBaseComponentClass, byte.class });
    }

    private ActionBar() {}

    public static void sendActionBar(Player player, String text) {
        try {
            sendPacket(player, packetPlayOutChatConstructor.newInstance(serializerAMethod.invoke(null, "{\"text\": \"" + text + "\"}"), (byte)2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object nmsPlayer = getNMSPlayer(player);
            Object connection = playerConnectionField.get(nmsPlayer);
            sendPacketMethod.invoke(connection, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Object getNMSPlayer(Player player) {
        try {
            return getHandleMethod.invoke(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}