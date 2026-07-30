package net.theevilreaper.aves.instance.anvil;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * The {@link ChunkCompression} enum describes the compression schemes which a chunk payload
 * inside a region file can use. The scheme is stored as a single byte in front of the payload.
 * <p>
 * A scheme with the {@link #EXTERNAL_FLAG} set marks a chunk which does not live inside the
 * region file itself but in a separate file next to it. The flag only describes the storage
 * location, the remaining bits still name the compression of the payload.
 * </p>
 *
 * <p>
 * This type is experimental. The Anvil loader is new and its API may still change while it is
 * being validated against real worlds.
 * </p>
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
@ApiStatus.Experimental
public enum ChunkCompression {

    /**
     * The payload is compressed with gzip.
     */
    GZIP(1),

    /**
     * The payload is compressed with zlib. This is the scheme which vanilla writes by default.
     */
    ZLIB(2),

    /**
     * The payload is stored without any compression.
     */
    NONE(3);

    /**
     * The bit which marks a chunk that is stored in a separate file next to the region file.
     */
    public static final int EXTERNAL_FLAG = 0x80;

    private static final int BUFFER_SIZE = 8192;

    private final int id;

    /**
     * Creates a new compression scheme with the identifier the format defines for it.
     *
     * @param id the identifier of the scheme inside a region file
     */
    ChunkCompression(int id) {
        this.id = id;
    }

    /**
     * Resolves the compression scheme which belongs to the given identifier.
     * A set {@link #EXTERNAL_FLAG} is stripped before the lookup happens.
     *
     * @param id the identifier to resolve
     * @return the matching compression scheme
     * @throws IOException if no supported scheme uses the given identifier
     */
    public static ChunkCompression fromId(int id) throws IOException {
        return switch (id & ~EXTERNAL_FLAG) {
            case 1 -> GZIP;
            case 2 -> ZLIB;
            case 3 -> NONE;
            default -> throw new IOException(
                    "The compression scheme " + id + " is not supported. Only gzip (1), zlib (2) and none (3) can be read"
            );
        };
    }

    /**
     * Checks whether the given identifier marks a chunk which is stored outside of the region file.
     *
     * @param id the identifier to check
     * @return true if the chunk is stored externally, otherwise false
     */
    @Contract(pure = true)
    public static boolean isExternal(int id) {
        return (id & EXTERNAL_FLAG) != 0;
    }

    /**
     * Returns the identifier which the format uses for this scheme.
     *
     * @return the identifier of the scheme
     */
    @Contract(pure = true)
    public int id() {
        return this.id;
    }

    /**
     * Compresses the given payload with this scheme.
     *
     * @param payload the uncompressed payload
     * @return the compressed payload
     * @throws IOException if the payload cannot be compressed
     */
    public byte[] compress(byte[] payload) throws IOException {
        if (this == NONE) {
            return payload.clone();
        }

        ByteArrayOutputStream target = new ByteArrayOutputStream(Math.max(payload.length / 4, BUFFER_SIZE));

        try (OutputStream stream = wrapForCompression(target)) {
            stream.write(payload);
        }
        return target.toByteArray();
    }

    /**
     * Decompresses the given payload with this scheme.
     *
     * @param payload the compressed payload
     * @return the uncompressed payload
     * @throws IOException if the payload cannot be decompressed
     */
    public byte[] decompress(byte[] payload) throws IOException {
        if (this == NONE) {
            return payload.clone();
        }

        try (InputStream stream = wrapForDecompression(new ByteArrayInputStream(payload))) {
            return stream.readAllBytes();
        }
    }

    /**
     * Wraps the given target stream into the compressing stream of this scheme.
     *
     * @param target the stream which receives the compressed bytes
     * @return the wrapped stream
     * @throws IOException if the wrapping stream cannot be created
     */
    private OutputStream wrapForCompression(OutputStream target) throws IOException {
        return switch (this) {
            case GZIP -> new GZIPOutputStream(target, BUFFER_SIZE);
            case ZLIB -> new DeflaterOutputStream(target);
            case NONE -> target;
        };
    }

    /**
     * Wraps the given source stream into the decompressing stream of this scheme.
     *
     * @param source the stream which holds the compressed bytes
     * @return the wrapped stream
     * @throws IOException if the wrapping stream cannot be created
     */
    private InputStream wrapForDecompression(InputStream source) throws IOException {
        return switch (this) {
            case GZIP -> new GZIPInputStream(source, BUFFER_SIZE);
            case ZLIB -> new InflaterInputStream(source);
            case NONE -> source;
        };
    }
}
