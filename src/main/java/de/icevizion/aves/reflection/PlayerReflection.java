package de.icevizion.aves.reflection;

import org.bukkit.entity.Player;
import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;
import org.inventivetalent.reflection.resolver.minecraft.OBCClassResolver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0
 * @since 18/04/2020 15:33
 */

public class PlayerReflection {

    private static Class<?> craftPlayerClass, entityPlayerClass;
    private static Method getHandleMethod, sendPacketMethod;
    private static Field playerConnectionField, pingField;

    static {
        craftPlayerClass = new OBCClassResolver().resolveSilent("entity.CraftPlayer");
        getHandleMethod = new MethodResolver(craftPlayerClass).resolveSilent("getHandle");
        entityPlayerClass = new NMSClassResolver().resolveSilent("EntityPlayer");
        playerConnectionField = new FieldResolver(entityPlayerClass).resolveSilent("playerConnection");
        sendPacketMethod = new MethodResolver(new NMSClassResolver().resolveSilent("PlayerConnection")).resolveSilent("sendPacket");
        pingField = new FieldResolver(entityPlayerClass).resolveSilent("ping");
    }

    public static Object getEntityPlayer(Player player) throws InvocationTargetException, IllegalAccessException {
        return getHandleMethod.invoke(player);
    }

    public static void sendPacket(Player p, Object packet) throws InvocationTargetException, IllegalAccessException {
        Object nmsPlayer = getEntityPlayer(p);
        Object connection = playerConnectionField.get(nmsPlayer);
        sendPacketMethod.invoke(connection, packet);
    }

    /**
     * Returns a player's ping.
     * @param player The player from which the ping is queried
     * @return The ping as integer
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */

    public static int getPing(Player player) throws InvocationTargetException, IllegalAccessException {
        return (int) pingField.get(getHandleMethod.invoke(player));
    }
}
