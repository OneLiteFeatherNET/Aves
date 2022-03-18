package de.icevizion.aves.file.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

import java.lang.reflect.Type;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public class PositionGsonAdapter implements JsonSerializer<Point>, JsonDeserializer<Point> {

    @Override
    public Point deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        double x = object.get("x").getAsDouble();
        double y = object.get("y").getAsDouble();
        double z = object.get("z").getAsDouble();
        float yaw = 0;
        float pitch = 0;
        if (object.has("yaw") && object.has("pitch")) {
            yaw = object.get("yaw").getAsFloat();
            pitch = object.get("pitch").getAsFloat();
            return new Pos(x,y,z, yaw, pitch);
        }
        return new Vec(x,y,z);
    }

    @Override
    public JsonElement serialize(Point src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("x", src.x());
        object.addProperty("y", src.y());
        object.addProperty("z", src.z());

        if (src instanceof Pos pos) {
            object.addProperty("yaw", pos.yaw());
            object.addProperty("pitch", pos.pitch());
        }

        return object;
    }
}
