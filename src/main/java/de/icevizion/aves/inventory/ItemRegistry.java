package de.icevizion.aves.inventory;

import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * @author Patrick Zdarsky / Rxcki
 */
public class ItemRegistry {

    private static final Map<ItemStack, UUID> registry = new WeakHashMap<>();

    public static void register(ItemStack itemStack, ItemStack other) {
        var otherUuid = registry.get(other);
        System.out.println("Is the received uuid null? " + (otherUuid == null));
        if (otherUuid == null) {
            otherUuid = UUID.randomUUID();
            System.out.println("Is the generated uuid null? " + (otherUuid == null));
            System.out.println("The generated uuid is: " + otherUuid.toString());
            registry.put(other, otherUuid);
        }
        registry.put(itemStack, otherUuid);
    }

    public static boolean isSame(ItemStack itemStack, ItemStack other) {
        var uuid = registry.get(itemStack);
        if (uuid == null)
            return false;
        return uuid.equals(registry.get(other));
    }
}
