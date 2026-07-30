package net.theevilreaper.aves.instance.anvil;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the compression schemes which the Anvil format supports for a chunk payload.
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
class ChunkCompressionTest {

    private static final byte[] PAYLOAD = "a chunk payload which repeats a chunk payload".getBytes(StandardCharsets.UTF_8);

    @ParameterizedTest
    @EnumSource(ChunkCompression.class)
    void testCompressAndDecompressAreInverse(ChunkCompression compression) throws IOException {
        byte[] compressed = compression.compress(PAYLOAD);

        assertArrayEquals(PAYLOAD, compression.decompress(compressed));
    }

    @ParameterizedTest
    @EnumSource(ChunkCompression.class)
    void testEveryCompressionExposesItsFormatIdentifier(ChunkCompression compression) throws IOException {
        assertEquals(compression, ChunkCompression.fromId(compression.id()));
    }

    @Test
    void testGzipIsIdentifiedByOne() throws IOException {
        assertEquals(ChunkCompression.GZIP, ChunkCompression.fromId(1));
    }

    @Test
    void testZlibIsIdentifiedByTwo() throws IOException {
        assertEquals(ChunkCompression.ZLIB, ChunkCompression.fromId(2));
    }

    @Test
    void testNoneIsIdentifiedByThree() throws IOException {
        assertEquals(ChunkCompression.NONE, ChunkCompression.fromId(3));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 4, 5, 127})
    void testUnsupportedSchemesAreRejectedWithTheirIdentifier(int id) {
        IOException exception = assertThrows(IOException.class, () -> ChunkCompression.fromId(id));

        assertTrue(exception.getMessage().contains(String.valueOf(id)));
    }

    @Test
    void testTheExternalFlagIsDetected() {
        assertTrue(ChunkCompression.isExternal(2 | ChunkCompression.EXTERNAL_FLAG));
        assertFalse(ChunkCompression.isExternal(2));
    }

    @Test
    void testTheExternalFlagIsStrippedBeforeResolving() throws IOException {
        assertEquals(ChunkCompression.ZLIB, ChunkCompression.fromId(2 | ChunkCompression.EXTERNAL_FLAG));
    }

    @Test
    void testNoneKeepsThePayloadUntouched() throws IOException {
        assertArrayEquals(PAYLOAD, ChunkCompression.NONE.compress(PAYLOAD));
    }

    @Test
    void testZlibActuallyShrinksARepetitivePayload() throws IOException {
        byte[] repetitive = new byte[4096];

        assertTrue(ChunkCompression.ZLIB.compress(repetitive).length < repetitive.length);
    }
}
