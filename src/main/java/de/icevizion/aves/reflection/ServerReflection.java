package de.icevizion.aves.reflection;

import org.inventivetalent.reflection.resolver.FieldResolver;
import org.inventivetalent.reflection.resolver.MethodResolver;
import org.inventivetalent.reflection.resolver.minecraft.NMSClassResolver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0
 * @since 18/04/2020 15:44
 */

public class ServerReflection {

    private static Class<?> minecraftServerClass;
    private static Field recentTpsField;
    private static Method getServerMethod;

    static {
        minecraftServerClass = new NMSClassResolver().resolveSilent("MinecraftServer");
        getServerMethod = new MethodResolver(minecraftServerClass).resolveSilent("getServer");
        recentTpsField = new FieldResolver(minecraftServerClass).resolveSilent("recentTps");
    }

    public static double[] getTps() throws IllegalAccessException, InvocationTargetException {
        Object minecraftServer = getServerMethod.invoke(null);

        return (double[]) recentTpsField.get(minecraftServer);
    }
}
