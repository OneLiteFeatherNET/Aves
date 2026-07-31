package net.minestom.server.instance.light;

import it.unimi.dsi.fastutil.shorts.ShortArrayFIFOQueue;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.palette.Palette;
import net.theevilreaper.aves.instance.light.LightNibbles;
import net.theevilreaper.aves.instance.light.LightPropagator;
import net.theevilreaper.aves.instance.light.MinestomBlockLightSource;
import net.theevilreaper.aves.instance.light.SectionOpacity;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * The {@link LightEngineComparisonBenchmark} class measures the light engine of Aves against the one
 * Minestom ships with, on the same section and to the same result.
 * <p>
 * The class lives in the Minestom light package because the two methods that form the built-in path,
 * {@code BlockLight.buildInternalQueue} and {@code LightCompute.compute}, are package-private. This
 * is the only way to measure the original rather than a reimplementation of it.
 * </p>
 * <p>
 * Both sides are measured over their full path, from a block palette to a finished light array of
 * {@code 2048} bytes. Neither side gets to skip its preparation: the built-in path builds its seed
 * queue, and the Aves path builds its opacity table through the real block registry rather than a
 * stand-in. Measuring only the searches would flatter whichever side does more of its work up front.
 * </p>
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(value = 1, jvmArgsAppend = {"-Xms512m", "-Xmx512m"})
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
public class LightEngineComparisonBenchmark {

    private static final int DIMENSION = 16;
    private static final int BLOCK_COUNT = DIMENSION * DIMENSION * DIMENSION;
    private static final long SEED = 20260731L;

    /**
     * The amount of light emitting blocks the measured section holds.
     */
    @Param({"1", "8", "64"})
    public int lightSources;

    /**
     * The share of solid blocks in the measured section, in percent.
     */
    @Param({"0", "30"})
    public int occlusionPercent;

    private Palette palette;
    private int[] stateIds;
    private MinestomBlockLightSource source;
    private LightPropagator propagator;

    /**
     * Starts the server once so the block registry is available, and builds the section both sides
     * are measured on.
     */
    @Setup(Level.Trial)
    public void setUp() {
        if (MinecraftServer.process() == null) {
            MinecraftServer.init();
        }

        this.source = new MinestomBlockLightSource();
        this.propagator = new LightPropagator();
        this.stateIds = new int[BLOCK_COUNT];

        Random random = new Random(SEED);
        int air = Block.AIR.stateId();
        int stone = Block.STONE.stateId();
        int glowstone = Block.GLOWSTONE.stateId();

        for (int index = 0; index < BLOCK_COUNT; index++) {
            this.stateIds[index] = random.nextInt(100) < this.occlusionPercent ? stone : air;
        }
        for (int placed = 0; placed < this.lightSources; placed++) {
            this.stateIds[random.nextInt(BLOCK_COUNT)] = glowstone;
        }

        this.palette = Palette.blocks();
        this.palette.setAll((x, y, z) -> this.stateIds[(y << 8) | (z << 4) | x]);
    }

    /**
     * Measures the built-in path: building the seed queue and running the search of Minestom.
     *
     * @return the calculated light array of the section
     */
    @Benchmark
    public byte[] minestom() {
        ShortArrayFIFOQueue queue = BlockLight.buildInternalQueue(this.palette);
        return LightCompute.compute(this.palette, queue);
    }

    /**
     * Measures the Aves path: building the opacity table through the real registry and running the
     * search, ending in the same light array layout.
     *
     * @return the calculated light array of the section
     */
    @Benchmark
    public byte[] aves() {
        int[] states = new int[BLOCK_COUNT];
        this.palette.getAll((x, y, z, value) -> states[(y << 8) | (z << 4) | x] = value);
        LightNibbles light = this.propagator.propagate(SectionOpacity.of(states, this.source));
        return light.toDenseArray();
    }
}
