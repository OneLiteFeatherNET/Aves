# Status — Anvil chunk loader and light engine

Branch `feat/aves-anvil-chunk-loader` · PR [#91](https://github.com/OneLiteFeatherNET/Aves/pull/91)
against `develop` · 17 commits · **552 tests** · `clean build` green three times in a row.

Everything below is experimental and opt-in. `AbstractMapProvider` still uses the loader Minestom
ships with unless a `ChunkLoaderFactory` is passed explicitly, so no existing consumer changes
behaviour by upgrading.

---

## What this is and why

Aves is OneLiteFeather's utility library for Minestom servers. This branch adds two things it did
not have:

1. **An Anvil chunk loader** that replaces `net.minestom.server.instance.anvil.AnvilLoader`. The
   goal was a loader that is genuinely parallel, that does not silently lose data, and that stays
   maintainable — developed test-first, on Java 25, using Adventure NBT and JetBrains annotations.
2. **A light engine**, because once chunks are loaded, lighting is the next thing a server pays for.

The motivating observation for the loader: Minestom's `AnvilLoader` reports
`supportsParallelLoading() == true`, but its `RegionFile` serialises reading, decompression **and**
NBT parsing through a single `ReentrantLock`. The parallelism is largely nominal. The gain is not in
starting more threads but in moving the CPU work out of the lock — which is what the three-stage
pipeline below does, and what the measurements confirmed.

## Environment

| | |
| --- | --- |
| Java | 25 (toolchain and `release`), no `--enable-preview` anywhere |
| Minestom | `2026.06.20-26.1.2`, `compileOnly` |
| Adventure | `5.1.1`, `adventure-nbt` used directly — Minestom speaks `CompoundBinaryTag` natively, so there is no conversion layer |
| Annotations | `org.jetbrains:annotations:26.1.0` |
| Tests | JUnit 6.1.0, Cyano `0.6.2` (`MicrotusExtension`) for anything needing a server |
| Benchmarks | JMH 1.37 via `me.champeau.jmh` 0.7.3, own `src/jmh/java` source set |
| Build | Gradle 9.6.1 |

`adventure-nbt`, `jetbrains-annotations` and `fastutil` were added to the version catalog by this
branch. The first two were only reaching the classpath transitively through `compileOnly(minestom)`,
so any direct use compiled by coincidence. `fastutil` is `runtime` scope in Minestom's POM and is
needed only by the comparison benchmark.

## Working on this

```bash
./gradlew clean build                 # compile, javadoc, tests — javadoc failures break the build
./gradlew test --tests "*Anvil*"      # a subset
./gradlew jmhJar                      # build the benchmarks (they never run during build)
java -jar build/libs/aves-*-jmh.jar ScalingBenchmark -f 1 -wi 2 -i 3
```

Two things that will otherwise cost an hour:

- **Do not run two Gradle builds in the same checkout at once.** They corrupt `build/test-results`
  and surface as `EOFException`, `NoClassDefFoundError` or missing `jacoco/test.exec` — failures
  that look like real test breakage. `rm -rf build` and rerun.
- **JMH allows one instance at a time.** A crashed run leaves `/tmp/jmh.lock` behind and every later
  run fails with *"Another JMH instance might be running"*. Delete the file.

## Conventions

Match these; the build enforces some of them.

- **Javadoc on every class and method**, with `@param` / `@return` / `@throws`. `withJavadocJar()`
  means an incomplete comment fails CI. Class comments explain *why*, not *what*, and carry
  `@author` / `@version` / `@since`.
- **Never write `@NotNull`.** Packages carry `@NotNullByDefault` in `package-info.java`; only
  `@Nullable`, `@Contract` and `@UnmodifiableView` appear explicitly.
- **Test-first, strictly.** Every type here was built by writing a failing test, confirming it fails
  for the right reason, then implementing. Several bugs in this branch were found precisely because
  a test was written before the code.
- Tests are package-private, named `test<What><Expectation>`, use plain JUnit assertions, and avoid
  `@Nested`. Anything needing a server uses `@ExtendWith(MicrotusExtension.class)`.
- Commits follow conventional commits, scoped `(anvil)`, `(light)`, `(map)`.

## Facts that cost real effort to establish

Verified against the sources or by running probe code. Knowing these prevents repeating the work.

**Minestom's loader interface**
- `ChunkLoader#loadChunk` is **synchronous** — it returns `@Nullable Chunk`, not a future.
  Parallelism happens because Minestom starts a virtual thread per chunk when
  `supportsParallelLoading()` is true. A loader must be thread-safe, not asynchronous.
- The default `saveChunks` starts **one virtual thread per chunk**, unbounded, and its `catch`
  branch never deregisters from the `Phaser`, so one exception hangs it forever. Override it.
- `unloadChunk` is documented as arriving for chunks the loader never loaded, which makes reference
  counting on it unreliable.
- `setChunkLoader` does **not** call `loadInstance` — only the constructor does. `AbstractMapProvider`
  sets the loader afterwards, so `level.dat` is never read there. Pre-existing, not introduced here.

**What can and cannot be replaced**
- `Palette` is `sealed ... permits PaletteImpl`. A foreign implementation is a hard compiler error,
  and `Section` is a record holding that exact type. Verified with javac and at runtime.
- `Light` is **not** sealed, and `Section`'s canonical constructor is public — a custom light
  implementation compiles and runs end to end. But `Section.clone()` calls `Light.sky()` / `Light.block()`
  outright, so a custom implementation is silently replaced on copy.
- `Instance` and `InstanceContainer` are not sealed either, but four `instanceof InstanceContainer`
  sites in Minestom make a foreign instance silently take a different path.

**Library traps**
- `adventure-nbt` 5.1.1: the iterators of `LongArrayBinaryTag`, `IntArrayBinaryTag` and
  `ByteArrayBinaryTag` **skip the last element** (`index < length - 1`). A for-each over packed block
  data corrupts every chunk. Use `size()` + `get(i)`. `NbtReadsTest` documents this as a live check.
- `BinaryTagIO.reader()` caps at 131 082 bytes, far too small for chunk NBT. Use `unlimitedReader()`.
- Every `CompoundBinaryTag` getter silently returns a default for a missing or mistyped key, which
  turns a broken region file into an empty chunk. `NbtReads` exists to make that an error.
- `Block.fromStateId` indexes an array **without a bounds check** and throws for an unknown id
  instead of returning null.

**Test environment**
- `MinecraftServer.getExceptionManager()` throws before `MinecraftServer.init()`. Anything resolving
  a registry in a constructor becomes untestable — this is why the biome resolver is lazy.
- Cyano's exception handler turns a reported exception into a **test failure**. Code that reports to
  the `ExceptionManager` cannot be asserted on by exception type in tests.

**Java 25**
- `StructuredTaskScope` (JEP 505) and `StableValue` (JEP 502) are still **preview** and therefore
  unusable in a published library — preview class files only run on the exact JDK they were built
  with, and would force `--enable-preview` on every consumer. Concurrency here uses
  `Executors.newVirtualThreadPerTaskExecutor()`, `Semaphore` and `Phaser`.
- Scoped Values, record patterns, sealed interfaces, FFM and stream gatherers are final and usable.
- File I/O does **not** unmount a virtual thread from its carrier (JEP 444), so unbounded virtual
  threads over file work do not scale — bound them.

## Decisions that shape everything else

These were explicit calls, not defaults. Changing one means revisiting the work that followed it.

| Decision | Choice | Why |
| --- | --- | --- |
| Format coverage | Core compression plus external `.mcc`, **no** LZ4, no corruption recovery | Covers real worlds without an extra dependency; Minestom fails hard on oversized chunks, which this does not |
| Integration | Opt-in via `ChunkLoaderFactory` | No breaking change; existing providers behave exactly as before |
| Own palette | **Not built** — codec-internal representation only | `Palette` is sealed, and it is 4.5 % of the load path anyway |
| Own `InstanceContainer` | **Not built** | Compiles, but four `instanceof` sites break silently and the tick parallelism lives elsewhere |
| Light `Light` implementation | **Not built** — results handed over via `Light#set` | Avoids the `@ApiStatus.Internal` calculation methods and the `Section.clone()` trap |
| Read failure | Throws, never returns `null` | `null` means "absent", so the server regenerates and overwrites real data on the next save |
| Compression level | 2, not the platform default 6 | 1.83× faster for ~3 % more bytes; compression is 63 % of a save |

## Where things live

```
src/main/java/net/theevilreaper/aves/
  instance/anvil/     RegionConstants, SectorAllocator, BitPacker, ChunkCompression,
                      RegionFile, NbtReads, PaletteData, PaletteEntryResolver, SectionCodec,
                      BlockPaletteResolver, BiomePaletteResolver, AnvilDiagnostics,
                      AvesAnvilLoader, AnvilChunkException
  instance/light/     LightNibbles, BlockFace, BlockLightSource, SectionOpacity,
                      LightPropagator, ChunkLightPropagator, ChunkLightState,
                      ChunkLightService, MinestomBlockLightSource
  map/provider/       ChunkLoaderFactory  (+ registerInstance overload in AbstractMapProvider)

src/test/java/...     mirrors the above; *ConcurrencyTest are the stress tests
src/jmh/java/         benchmarks; LightEngineComparisonBenchmark lives in
                      net.minestom.server.instance.light because the methods it measures
                      are package-private there
```

Reading order for someone new: `RegionFile` (the byte container), then `AvesAnvilLoader`
(the three stages), then `SectionOpacity` and `ChunkLightPropagator` for the light side.

## Charts

Published from the measurements in this branch. They are snapshots, not live views — re-run the
benchmarks before trusting them after a change.

| Chart | Shows |
| --- | --- |
| [Scaling and comparison](https://claude.ai/code/artifact/38131b5a-42f8-43c6-a843-f845802d78ae) | 1 to 256 sections, and the head-to-head against Minestom |
| [Optimisation](https://claude.ai/code/artifact/a11c1e46-7310-40ed-84e5-0c4d650cbcc1) | Where save time goes, the compression trade-off, the uniform-section fast paths |
| [Vanilla · Minestom · Aves](https://claude.ai/code/artifact/9d3b6d0d-ced3-4f0b-b675-6fb1640f262f) | 22 behaviours scored against the format reference |

---

## What is in the branch

| Package | Types | Tests | What it does |
| --- | ---: | ---: | --- |
| `instance.anvil` | 14 | 13 classes | Reads and writes Anvil region files, replacing `AnvilLoader` |
| `instance.light` | 11 | 10 classes | Computes block light and sky light for a chunk |
| `map.provider` | +1 | 1 class | `ChunkLoaderFactory`, the opt-in seam |
| `src/jmh` | 20 files | — | Benchmarks, in their own source set |

### Anvil loader

Three stages, so the expensive work never happens under a lock: the chunk state is copied under its
read lock, the conversion to compressed bytes runs lock-free, and only the transfer into the region
file is guarded. `saveChunks` is grouped per region and bounded by a semaphore rather than starting
one virtual thread per chunk.

Region files use positional `FileChannel` operations, so reads of different chunks proceed in
parallel. A file is closed once the last chunk this loader read from it is unloaded, with a hard cap
on open handles as a backstop.

### Light engine

Block light and sky light, across section borders, across chunk borders, and incrementally after a
single block changed. The algorithm has no Minestom dependency — the registry sits behind
`BlockLightSource`, the same separation the Anvil codec uses for palettes — and results are handed to
a chunk through `Light#set`, which is the stable part of that interface rather than its internal
calculation methods.

---

## Measured

All figures from one machine that was **not idle**. Ratios are meaningful, absolute microseconds
carry a wide error. Reproduce with `./gradlew jmhJar` and the benchmark names below.

### Where the time goes when saving a chunk

`ChunkSaveStageBenchmark`, 24 sections, 200 block states:

| Stage | Time | Lock held |
| --- | ---: | --- |
| Snapshot | 64 µs | chunk read lock |
| Codec, without compression | 1 356 µs | none |
| zlib compression | 2 701 µs | none |
| Transfer | 17 µs | region lock |

**About 97 % of a save runs outside any lock**, and compression is 63 % of the whole operation. This
is the measurement that turned the design claim into a number, and it is what made compression the
optimisation target.

### Against the engine Minestom ships with

`LightEngineComparisonBenchmark`, one section, both engines run to a **byte-identical** result
(54 scenarios, zero differing cells, maximum level delta 0):

| Scenario | Factor |
| --- | ---: |
| 1 source, no solid blocks | 0.69× |
| 8 sources, no solid blocks | 0.92× |
| 64 sources, no solid blocks | 0.89× |
| 1 source, 30 % solid | 0.93× |
| 8 sources, 30 % solid | **1.31×** |
| 64 sources, 30 % solid | **1.34×** |

Systematic, not noise: an empty section favours Minestom because our opacity table is built
unconditionally, a section with solid blocks favours us because that table is then read many times.

### Scaling by world height

`ScalingBenchmark`, 1 to 256 sections:

- **Block light is linear** across the whole range — 33 µs per section at 1 section and at 256.
- **Sky light is not.** Cost per section rises from 84 µs to 104 µs past roughly 64 sections.
- A least-squares fit over the vanilla range (≤ 24 sections) predicts 21 423 µs of sky light at 256
  sections. The measured value is 26 597 µs — **the forecast understates it by 19.5 %**. For block
  light the same method lands within 1.8 %.

Measuring the exotic sizes rather than extrapolating from common ones is the only reason this is
known.

### Optimisations these numbers produced

| Change | Effect |
| --- | --- |
| zlib level 2 instead of the platform default 6 | 1.83× faster compression, ~3 % larger files |
| Fast path for uniform sections, palette encode | 27.9 µs → 0.54 µs (**51×**) |
| Fast path for uniform sections, opacity table | 40.8 µs → 0.54 µs (**76×**), and no arrays allocated |

---

## Known deviations from vanilla

Vanilla defines the Anvil format, so these are gaps in this implementation, not preferences:

| Gap | Consequence |
| --- | --- |
| Heightmaps are neither written nor restored | Minestom at least restores them on load |
| Unknown chunk-level tags are dropped | `structures`, `block_ticks`, `fluid_ticks` and others are lost on save |
| `entities/` and `poi/` are ignored | Saving a vanilla world produces inconsistent world data |
| `level.dat` is not handled | `loadInstance` / `saveInstance` are not overridden |
| No LZ4 (type 4) or custom (type 127) compression | A world written with `region-file-compression=lz4` cannot be read |
| No corruption recovery | A damaged header makes the whole region unreadable |
| An unknown block becomes air | Better than discarding the chunk, but not what a data fixer does |

---

## Defects found and fixed

### In this code

- **Block entities were stored at chunk-local coordinates.** The format specifies world coordinates.
  The round trip through this loader worked anyway because `Chunk#setBlock` masks them, so only a
  test that read the stored NBT directly could catch it. Files were not interchangeable with vanilla.
- **Block handlers were lost on load.** The `id` tag was written on save but discarded on load.
- **The propagation queue could overflow.** It was sized on the assumption that a position is queued
  at most once, which is false when sources of different brightness reach the same area.
- **`AvesAnvilLoader` could lose a chunk.** A region file could be evicted between obtaining the
  handle and writing to it.
- **The name cap in `AnvilDiagnostics` was a check-then-act**, so racing threads could exceed it.
- **The biome registry was resolved eagerly**, which made a loader impossible to construct before
  `MinecraftServer.init`.

### In Minestom, avoided here

Length field written as `5 + N` instead of `1 + N`; `status` in lower case where the game writes
`Status`; the return value of `read` ignored; an unknown block turning into an NPE that discards the
whole chunk; block entities dropped in uniform sections; the read failure path returning `null`,
which makes the server regenerate the chunk and overwrite the real data on the next save.

---

## Open

Ordered by consequence, not by effort.

### 1. `ChunkLightService` is not thread safe — and looks like it is

It holds a `ChunkLightPropagator` in a field, so two threads sharing one service share its buffers.
A probe reproduced **wrong light in ~99 % of concurrent calls** plus occasional
`ArrayIndexOutOfBoundsException`, and the wrong light is written with the update flag cleared, so
the server never recomputes it. Nothing in the class documentation warns about this; one sentence in
it actively suggests the opposite.

The fix is about five lines — drop the field, build a propagator per call, which is what
`calculateWithNeighbours` already does. Explicitly **not** the fix: `ThreadLocal` (costly under
virtual threads), `volatile` (removes the exceptions and leaves the silent corruption), or
`synchronized` (serialises exactly the parallelism the type exists for).

The engine is fully parallelisable per chunk today — one propagator per thread gave 0 wrong results
in 4000 concurrent runs. Only the shared scratch buffer stands in the way.

### 2. Exception hierarchy

Design complete in [`docs/research/exception-hierarchy.md`](docs/research/exception-hierarchy.md),
six types, not implemented. **One open decision:** whether the checked root extends `IOException`.
Extending it keeps roughly 40 signatures and 14 test assertions untouched; not extending it stops
every existing `catch (IOException)` from silently swallowing the new types. Both arguments hold —
this needs a call, not more analysis.

### 3. Smaller items

- Border exchange between chunks settles one ring deep; a fully converged result over a large area
  needs the exchange repeated.
- Sky light updates re-seed open columns rather than tracking a heightmap incrementally.
- `SectionOpacity` builds its table unconditionally for non-uniform sections, which is why an empty
  section with one light source loses to Minestom.

---

## Investigated and deliberately not built

Three "replace this part of Minestom" questions were researched before any code was written. The
answers differed sharply and none was obvious in advance — see [`docs/research/`](docs/research/).

| Subject | Verdict |
| --- | --- |
| **Palette** | Impossible. `sealed interface Palette permits PaletteImpl` is a hard compiler error, and `Section` is a record holding that exact type. |
| **`InstanceContainer`** | Possible but pointless as asked. It compiles and runs, but four `instanceof InstanceContainer` sites silently take another path for a foreign type, and the tick parallelism the request targeted lives in the global `ThreadDispatcher`, not in the container. |
| **Light engine** | Possible and worth it — this is what was built. |

The recurring lesson: **sealed-ness decides whether it is possible, and the profile decides whether
it is worth it.** Both have to be checked before designing anything, and neither can be guessed.

---

## Documents

| File | Contents |
| --- | --- |
| [`docs/anvil-chunk-loader.md`](docs/anvil-chunk-loader.md) | Usage, architecture, 20-row comparison with the built-in loader, limits |
| [`docs/light-engine.md`](docs/light-engine.md) | Usage, design, where resources are saved, limits |
| [`docs/benchmarks.md`](docs/benchmarks.md) | How to run the benchmarks and what each measures |
| [`docs/research/`](docs/research/) | The three investigations, with both positions where agents disagreed |
