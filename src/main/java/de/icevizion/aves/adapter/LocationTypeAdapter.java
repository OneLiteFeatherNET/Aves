package de.icevizion.aves.adapter;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

/**
 * The LocationTypeAdapter allows to serialize and deserialize from {@link Location} into a valid json object or from a json object into {@link Location}
 * The adapter use the {@link GsonBuilder} to register a custom adapter with the {@link GsonBuilder#registerTypeAdapter(Type, Object)} method
 * with the parameters {@link Location} class and a instance of the {@link LocationTypeAdapter}
 */
public final class LocationTypeAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        object.addProperty("world", location.getWorld().getName());
        object.addProperty("x", location.getX());
        object.addProperty("y", location.getY());
        object.addProperty("z", location.getZ());
        object.addProperty("yaw", location.getYaw());
        object.addProperty("pitch", location.getPitch());
        return object;
    }

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object = jsonElement.getAsJsonObject();
        String worldName = object.get("world").getAsString();
        double x = object.get("x").getAsDouble();
        double y = object.get("y").getAsDouble();
        double z = object.get("z").getAsDouble();
        float yaw = 0;
        float pitch = 0;
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            if (Bukkit.getWorlds().size() == 0) {
                throw new JsonParseException("Cannot deserialize bukkit location without any worlds loaded!");
            } else {
                world = Bukkit.getWorlds().get(0);
            }
        }

        if (object.has("yaw") && object.has("pitch")) {
            yaw = object.get("yaw").getAsFloat();
            pitch = object.get("pitch").getAsFloat();
        }
        return new Location(world, x, y, z, yaw, pitch);
    }
}