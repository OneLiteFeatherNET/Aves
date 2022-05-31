package de.icevizion.aves;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Patrick Zdarsky / Rxcki
 */
@Deprecated(forRemoval = true)
public class ServiceRegistry {

    private static final Map<String, Object> serviceMap = new HashMap<>();
    private static final Lock serviceLock = new ReentrantLock();

    public static void registerService(String name, Object service) {
        serviceLock.lock();
        try {
            if (serviceMap.containsKey(name.toLowerCase())) {
                throw new IllegalStateException("A service with the same name is already registered");
            }

            serviceMap.put(name.toLowerCase(), service);
        } finally {
            serviceLock.unlock();
        }
    }

    public static <U> U getService(String name) {
        serviceLock.lock();
        try {
            return (U) serviceMap.get(name.toLowerCase());
        } finally {
            serviceLock.unlock();
        }
    }
}
