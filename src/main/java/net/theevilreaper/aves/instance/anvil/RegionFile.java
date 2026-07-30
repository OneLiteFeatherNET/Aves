package net.theevilreaper.aves.instance.anvil;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.locks.ReentrantLock;

/**
 * The {@link RegionFile} class represents a single Anvil region file which stores up to
 * {@code 32 x 32} chunks. The class is a pure byte container. It neither knows the NBT structure
 * of a chunk nor the Minestom chunk model which keeps the file format concern isolated.
 * <p>
 * Reading uses positional channel operations which do not touch the channel position and are
 * therefore safe to run from multiple threads at the same time. Only the sector allocation and
 * the header update need the internal lock, so the expensive work of a caller stays outside of
 * any critical section.
 * </p>
 * <p>
 * A chunk which does not fit into the {@link RegionConstants#MAX_SECTORS_PER_CHUNK} sectors a
 * location entry can address is moved into a separate file next to the region file.
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
public final class RegionFile implements AutoCloseable {

    private final Path path;
    private final Path directory;
    private final FileChannel channel;
    private final ReentrantLock lock;
    private final int[] locations;
    private final int[] timestamps;
    private final SectorAllocator allocator;

    private volatile boolean closed;

    /**
     * Creates a new region file around the given channel and header state.
     *
     * @param path       the path of the region file
     * @param channel    the channel which is used for all read and write operations
     * @param locations  the location table of the region file
     * @param timestamps the timestamp table of the region file
     * @param allocator  the allocator which tracks the sector usage
     */
    private RegionFile(Path path, FileChannel channel, int[] locations, int[] timestamps, SectorAllocator allocator) {
        this.path = path;
        this.directory = path.getParent() == null ? Path.of(".") : path.getParent();
        this.channel = channel;
        this.lock = new ReentrantLock();
        this.locations = locations;
        this.timestamps = timestamps;
        this.allocator = allocator;
    }

    /**
     * Opens the region file under the given path and reads its header.
     * A file which does not exist yet is created with an empty header.
     *
     * @param path the path of the region file
     * @return the opened region file
     * @throws IOException if the file cannot be opened or holds a broken header
     */
    public static RegionFile open(Path path) throws IOException {
        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        FileChannel channel = FileChannel.open(
                path, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE
        );

        try {
            return readHeader(path, channel);
        } catch (IOException | RuntimeException exception) {
            channel.close();
            throw exception;
        }
    }

    /**
     * Reads the header of an already opened region file and rebuilds the sector usage from it.
     *
     * @param path    the path of the region file
     * @param channel the channel of the region file
     * @return the region file which is described by the header
     * @throws IOException if the header is incomplete or describes an invalid layout
     */
    private static RegionFile readHeader(Path path, FileChannel channel) throws IOException {
        long size = channel.size();
        int[] locations = new int[RegionConstants.ENTRY_COUNT];
        int[] timestamps = new int[RegionConstants.ENTRY_COUNT];

        if (size == 0) {
            channel.write(ByteBuffer.allocate(RegionConstants.HEADER_SIZE), 0);
            return new RegionFile(path, channel, locations, timestamps, new SectorAllocator(RegionConstants.HEADER_SECTORS));
        }

        if (size < RegionConstants.HEADER_SIZE) {
            throw new IOException(
                    "The region file " + path + " holds " + size + " bytes which is less than the header size of "
                            + RegionConstants.HEADER_SIZE + " bytes"
            );
        }

        ByteBuffer header = readFully(channel, 0, RegionConstants.HEADER_SIZE, path);

        for (int index = 0; index < RegionConstants.ENTRY_COUNT; index++) {
            locations[index] = header.getInt(RegionConstants.locationOffset(index));
            timestamps[index] = header.getInt(RegionConstants.timestampOffset(index));
        }

        int totalSectors = (int) Math.max(size / RegionConstants.SECTOR_SIZE, RegionConstants.HEADER_SECTORS);
        SectorAllocator allocator = new SectorAllocator(totalSectors);

        for (int index = 0; index < RegionConstants.ENTRY_COUNT; index++) {
            int location = locations[index];

            if (location == 0) {
                continue;
            }

            int offset = location >>> 8;
            int count = location & 0xFF;

            if (offset < RegionConstants.HEADER_SECTORS || count <= 0) {
                locations[index] = 0;
                continue;
            }
            allocator.reserve(offset, count);
        }
        return new RegionFile(path, channel, locations, timestamps, allocator);
    }

    /**
     * Reads the raw payload of the given chunk without decompressing it.
     * The caller is expected to decompress the payload outside of any lock this class holds.
     *
     * @param chunkX the absolute chunk x coordinate
     * @param chunkZ the absolute chunk z coordinate
     * @return the raw chunk or null if the region file does not hold the chunk
     * @throws IOException if the chunk cannot be read or holds an invalid header
     */
    public @Nullable RawChunk readRaw(int chunkX, int chunkZ) throws IOException {
        ensureOpen();
        int index = RegionConstants.index(chunkX, chunkZ);
        int location = this.locations[index];

        if (location == 0) {
            return null;
        }

        int sectorOffset = location >>> 8;
        int sectorCount = location & 0xFF;
        long position = (long) sectorOffset * RegionConstants.SECTOR_SIZE;
        int available = sectorCount * RegionConstants.SECTOR_SIZE;

        ByteBuffer head = readFully(this.channel, position, RegionConstants.LENGTH_FIELD_SIZE + RegionConstants.COMPRESSION_FIELD_SIZE, this.path);
        int length = head.getInt();
        int scheme = head.get() & 0xFF;

        if (length <= 0 || length > available) {
            throw new IOException(
                    "The chunk " + chunkX + "/" + chunkZ + " in " + this.path + " declares a length of " + length
                            + " bytes which does not fit into its " + sectorCount + " sectors"
            );
        }

        ChunkCompression compression = ChunkCompression.fromId(scheme);

        if (ChunkCompression.isExternal(scheme)) {
            return new RawChunk(compression, Files.readAllBytes(externalPath(chunkX, chunkZ)));
        }

        int payloadLength = length - RegionConstants.COMPRESSION_FIELD_SIZE;
        ByteBuffer payload = readFully(
                this.channel, position + RegionConstants.LENGTH_FIELD_SIZE + RegionConstants.COMPRESSION_FIELD_SIZE,
                payloadLength, this.path
        );
        byte[] bytes = new byte[payloadLength];
        payload.get(bytes);
        return new RawChunk(compression, bytes);
    }

    /**
     * Writes the raw payload of the given chunk into the region file.
     * The payload is expected to be compressed already so the compression can happen outside of
     * the lock this method acquires.
     *
     * @param chunkX      the absolute chunk x coordinate
     * @param chunkZ      the absolute chunk z coordinate
     * @param compression the compression scheme of the payload
     * @param payload     the compressed payload of the chunk
     * @throws IOException if the chunk cannot be written
     */
    public void writeRaw(int chunkX, int chunkZ, ChunkCompression compression, byte[] payload) throws IOException {
        ensureOpen();
        int index = RegionConstants.index(chunkX, chunkZ);
        int totalLength = RegionConstants.LENGTH_FIELD_SIZE + RegionConstants.COMPRESSION_FIELD_SIZE + payload.length;
        boolean external = RegionConstants.sectorsFor(totalLength) > RegionConstants.MAX_SECTORS_PER_CHUNK;

        if (external) {
            Files.write(externalPath(chunkX, chunkZ), payload);
        }

        byte[] stored = external ? new byte[0] : payload;
        int scheme = external ? compression.id() | ChunkCompression.EXTERNAL_FLAG : compression.id();
        // The specification defines the length field as the compression byte plus the payload.
        int length = RegionConstants.COMPRESSION_FIELD_SIZE + stored.length;
        int sectorCount = RegionConstants.sectorsFor(RegionConstants.LENGTH_FIELD_SIZE + length);

        ByteBuffer buffer = ByteBuffer.allocate(sectorCount * RegionConstants.SECTOR_SIZE);
        buffer.putInt(length).put((byte) scheme).put(stored);
        buffer.rewind();

        this.lock.lock();
        try {
            int previous = this.locations[index];
            int sectorOffset = this.allocator.allocate(sectorCount);

            writeFully(this.channel, buffer, (long) sectorOffset * RegionConstants.SECTOR_SIZE);

            this.locations[index] = (sectorOffset << 8) | sectorCount;
            this.timestamps[index] = (int) (System.currentTimeMillis() / 1000L);
            writeEntry(index);

            if (previous != 0) {
                this.allocator.free(previous >>> 8, previous & 0xFF);
            }
        } finally {
            this.lock.unlock();
        }

        if (!external) {
            Files.deleteIfExists(externalPath(chunkX, chunkZ));
        }
    }

    /**
     * Removes the given chunk from the region file.
     * The sectors the chunk occupied become available for a later write.
     *
     * @param chunkX the absolute chunk x coordinate
     * @param chunkZ the absolute chunk z coordinate
     * @throws IOException if the header cannot be updated
     */
    public void delete(int chunkX, int chunkZ) throws IOException {
        ensureOpen();
        int index = RegionConstants.index(chunkX, chunkZ);

        this.lock.lock();
        try {
            int previous = this.locations[index];

            if (previous == 0) {
                return;
            }

            this.locations[index] = 0;
            this.timestamps[index] = 0;
            writeEntry(index);
            this.allocator.free(previous >>> 8, previous & 0xFF);
        } finally {
            this.lock.unlock();
        }
        Files.deleteIfExists(externalPath(chunkX, chunkZ));
    }

    /**
     * Checks whether the region file holds the given chunk.
     *
     * @param chunkX the absolute chunk x coordinate
     * @param chunkZ the absolute chunk z coordinate
     * @return true if the chunk is present, otherwise false
     */
    @Contract(pure = true)
    public boolean hasChunk(int chunkX, int chunkZ) {
        return this.locations[RegionConstants.index(chunkX, chunkZ)] != 0;
    }

    /**
     * Returns the path of the region file.
     *
     * @return the path of the region file
     */
    @Contract(pure = true)
    public Path path() {
        return this.path;
    }

    /**
     * Forces all pending changes of the region file to the underlying storage.
     *
     * @throws IOException if the changes cannot be written
     */
    public void flush() throws IOException {
        ensureOpen();
        this.channel.force(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        this.lock.lock();
        try {
            if (this.closed) {
                return;
            }
            this.closed = true;
            this.channel.close();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * Writes the location and the timestamp entry of the given index into the header.
     * Only the eight affected bytes are touched so a crash cannot destroy the whole header.
     *
     * @param index the index of the chunk inside the region tables
     * @throws IOException if the entry cannot be written
     */
    private void writeEntry(int index) throws IOException {
        ByteBuffer location = ByteBuffer.allocate(Integer.BYTES).putInt(this.locations[index]).rewind();
        writeFully(this.channel, location, RegionConstants.locationOffset(index));

        ByteBuffer timestamp = ByteBuffer.allocate(Integer.BYTES).putInt(this.timestamps[index]).rewind();
        writeFully(this.channel, timestamp, RegionConstants.timestampOffset(index));
    }

    /**
     * Builds the path of the file which holds an oversized chunk.
     *
     * @param chunkX the absolute chunk x coordinate
     * @param chunkZ the absolute chunk z coordinate
     * @return the path of the external chunk file
     */
    @Contract(pure = true)
    private Path externalPath(int chunkX, int chunkZ) {
        return this.directory.resolve("c." + chunkX + "." + chunkZ + ".mcc");
    }

    /**
     * Verifies that the region file is still usable.
     *
     * @throws IOException if the region file is already closed
     */
    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("The region file " + this.path + " is already closed");
        }
    }

    /**
     * Reads the requested amount of bytes from the given position.
     * A channel is allowed to return fewer bytes than requested, so the read is repeated until
     * the buffer is filled or the file ends.
     *
     * @param channel the channel to read from
     * @param position the position to start reading at
     * @param length   the amount of bytes to read
     * @param path     the path which is used for the error message
     * @return a buffer which holds the requested bytes and is ready to be read
     * @throws IOException if the file ends before the requested amount of bytes was read
     */
    private static ByteBuffer readFully(FileChannel channel, long position, int length, Path path) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(length);
        long offset = position;

        while (buffer.hasRemaining()) {
            int read = channel.read(buffer, offset);

            if (read < 0) {
                throw new IOException(
                        "The file " + path + " ended after " + buffer.position() + " of " + length + " expected bytes"
                );
            }
            offset += read;
        }
        return buffer.rewind();
    }

    /**
     * Writes the complete buffer to the given position.
     *
     * @param channel  the channel to write to
     * @param buffer   the buffer which holds the bytes to write
     * @param position the position to start writing at
     * @throws IOException if the bytes cannot be written
     */
    private static void writeFully(FileChannel channel, ByteBuffer buffer, long position) throws IOException {
        long offset = position;

        while (buffer.hasRemaining()) {
            offset += channel.write(buffer, offset);
        }
    }

    /**
     * The {@link RawChunk} record holds the untouched payload of a chunk together with the
     * compression scheme which is required to decode it.
     *
     * @param compression the compression scheme of the payload
     * @param payload     the payload as it is stored on disk
     * @author TheMeinerLP
     * @version 1.0.0
     * @since 1.16.0
     */
    public record RawChunk(ChunkCompression compression, byte[] payload) {

        /**
         * Decompresses the payload of the chunk.
         *
         * @return the decompressed payload
         * @throws IOException if the payload cannot be decompressed
         */
        public byte[] decompress() throws IOException {
            return this.compression.decompress(this.payload);
        }
    }
}
