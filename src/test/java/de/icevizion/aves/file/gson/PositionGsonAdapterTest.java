package de.icevizion.aves.file.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PositionGsonAdapterTest {

    private static final String POS_JSON = """
                    {"x":0.0,"y":0.0,"z":0.0,"yaw":0.0,"pitch":0.0}
            """;
    private static final String POS_JSON_2 = """
            {"x":0.0,"y":0.0,"z":0.0,"yaw":1.0}
            """;
    private static final String VEC_JSON = """
            {"x":2.0,"y":1.0,"z":1.0}
            """;
    Pos writePos;
    Vec writeVec;
    Gson gson;

    @BeforeAll
    void init() {
        this.writePos = Pos.ZERO;
        this.writeVec = Vec.ONE;
        var adapter = new PositionGsonAdapter();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Pos.class, adapter)
                .registerTypeAdapter(Vec.class, adapter)
                .create();
    }

    @Test
    void testPointWrite() {
        var output = this.gson.toJson(this.writePos, Pos.class);
        assertNotNull(output);
        assertEquals(POS_JSON, output);
    }

    @Test
    void testPointRead() {
        var pos = this.gson.fromJson(POS_JSON, Pos.class);
        assertNotNull(pos);
        assertEquals(Pos.ZERO, pos);
    }

    @Test
    void testPointReadWithoutYawAndPitch() {
        assertThrows(ClassCastException.class, () -> this.gson.fromJson(POS_JSON_2, Pos.class));
    }

    @Test
    void testVecWrite() {
        var output = this.gson.toJson(this.writeVec, Vec.class);
        assertNotNull(output);
    }

    @Test
    void testVecRead() {
        var vec = this.gson.fromJson(VEC_JSON, Vec.class);
        assertNotNull(vec);
        assertNotSame(Vec.ONE, vec);
        assertEquals(new Vec(2.0, 1.0, 1.0), vec);
    }

}