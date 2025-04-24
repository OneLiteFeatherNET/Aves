package net.theevilreaper.aves.util.functional;

import net.theevilreaper.aves.map.BaseMap;
import net.minestom.server.coordinate.Pos;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class BaseMapFunctionTest {

    @Test
    void testBaseMapFunctionalInterface() {
        final BaseMapFunction<BaseMap, Pos> function = baseMap -> baseMap == null ? null : baseMap.getSpawn();
        assertNull(function.apply(null));
        final BaseMap baseMap = new BaseMap();
        final Pos spawn = function.apply(baseMap);
        assertNull(spawn);
    }

    @Test
    void testBaseMapBiFunctionApply() {
        // Create an instance of BaseMapBiFunction
        BaseMapBiFunction<BaseMap, String, Integer> biFunction = (baseMap, str) -> {
            // Example implementation: returning the length of the string
            return str != null ? str.length() : 0;
        };

        // Create a dummy BaseMap instance
        BaseMap baseMap = new BaseMap();

        // Test the apply method
        assertEquals(7, biFunction.apply(baseMap, "example"));
        assertEquals(0, biFunction.apply(baseMap, null));
    }

    @Test
    void testBaseMapBiFunctionApplyAndThen() {
        // Create an instance of BaseMapBiFunction
        BaseMapBiFunction<BaseMap, String, Integer> biFunction = (baseMap, str) -> {
            // Example implementation: returning the length of the string
            return str != null ? str.length() : 0;
        };

        // Create an after function to convert the length to a string
        Function<Integer, String> afterFunction = Object::toString;

        // Create a composed function using andThen
        BaseMapBiFunction<BaseMap, String, String> composedFunction = biFunction.andThen(afterFunction);

        // Create a dummy BaseMap instance
        BaseMap baseMap = new BaseMap();

        // Test the composed function
        assertEquals("7", composedFunction.apply(baseMap, "example"));
        assertEquals("0", composedFunction.apply(baseMap, null));
    }

    @Test
    void testAndThenWithNullAfter() {
        // Create an instance of BaseMapBiFunction
        BaseMapBiFunction<BaseMap, String, Integer> biFunction = (baseMap, str) -> {
            // Example implementation: returning the length of the string
            return str != null ? str.length() : 0;
        };

        // Test the andThen method with a null after function
        assertThrows(NullPointerException.class, () -> biFunction.andThen(null));
    }
}
