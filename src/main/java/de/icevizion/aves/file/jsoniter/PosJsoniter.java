package de.icevizion.aves.file.jsoniter;

import com.jsoniter.JsonIterator;
import com.jsoniter.ValueType;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Decoder;
import com.jsoniter.spi.Encoder;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.1.0
 **/
public final class PosJsoniter implements Encoder, Decoder {

    /**
     * Decodes a {@link Point} from the {@link JsonIterator}.
     * @param jsonIterator The valid instance to the iterator
     * @return a pos object
     */
    @Override
    public Point decode(JsonIterator jsonIterator) throws IOException {
        var object = jsonIterator.readAny();

        var x = object.get("x").toDouble();
        var y = object.get("y").toDouble();
        var z = object.get("z").toDouble();

        boolean hasRotation = object.get("yaw").valueType() != ValueType.INVALID &&
                object.get("pitch").valueType() != ValueType.INVALID;

        if (hasRotation) {
            var yaw = object.get("yaw").toFloat();
            var pitch = object.get("pitch").toFloat();

            return new Pos(x, y, z, yaw, pitch);
        }

        return new Vec(x, y, z);
    }

    @Override
    public void encode(Object object, JsonStream jsonStream) throws IOException {
        if ((!(object instanceof Point point))) {
            jsonStream.writeNull();
            return;
        }

        HashMap<String, Object> outputMap = new HashMap<>();

        outputMap.put("x", point.x());
        outputMap.put("y", point.y());
        outputMap.put("z", point.z());

        if (point instanceof Pos pos) {
            outputMap.put("yaw", pos.yaw());
            outputMap.put("pitch", pos.pitch());
        }

        jsonStream.writeVal(HashMap.class, outputMap);
    }
}
