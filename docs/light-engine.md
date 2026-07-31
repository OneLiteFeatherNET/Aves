# Light engine

A light propagation for a single section that resolves the properties of a block once instead of on
every visit, and stores a uniform section without an array. The algorithm itself knows nothing about
Minestom, which is what makes it testable without a running server.

> **Experimental.** Every public type of `net.theevilreaper.aves.instance.light` is annotated
> `@ApiStatus.Experimental`. The API may still change.

> **Scope.** Block light and sky light for a chunk, across section borders, across chunk borders,
> and incrementally after a single block changed. Works with any chunk of any loader.
> See [Limits](#limits) for what is still missing.

## When this is worth using

Honest answer first, because it decides whether the package is relevant at all:

| Workload | Does light computation run? |
| --- | --- |
| Loading pre-lit worlds from `.mca` | **No.** The stored light is applied with `Light#set`, which clears `requiresUpdate()`. Nothing is recomputed — unless you call this engine explicitly. |
| Generated worlds without stored light | Yes |
| Runtime block placement | Yes |

For loading pre-built maps from region files — the dominant Aves use case — a light engine
contributes nothing, because that code path never executes. The measured cost of loading such a
chunk is dominated by NBT parsing and zlib inflation instead.

## Design

Seven types, each with one responsibility. Only the two on the right know Minestom exists.

```
                      ┌─ engine, no Minestom ─────────────┐   ┌─ adapter ──────────────┐
                      │                                   │   │                        │
  Chunk ──────────────┼──► int[] stateIds                 │   │  ChunkLightService     │
                      │         │                         │   │        │               │
                      │         ▼                         │   │        │ uses          │
                      │   SectionOpacity ◄── BlockLightSource ◄── MinestomBlockLight-  │
                      │         │            (one lookup   │   │        Source          │
                      │         ▼             per state)   │   │                        │
                      │   ChunkLightPropagator             │   │  writes back through   │
                      │         │  (crosses section        │   │  Light#set(byte[])     │
                      │         ▼   borders)               │   │                        │
                      │   List<LightNibbles> ──────────────┼───┼──►  Chunk sections     │
                      └───────────────────────────────────┘   └────────────────────────┘
```

| Type | Responsibility |
| --- | --- |
| `LightNibbles` | Storage. Two levels per byte, uniform sections without an array. |
| `BlockLightSource` | Abstraction over "how bright is this block and which faces does it block". |
| `SectionOpacity` | Precomputed table of those properties for one section. |
| `LightPropagator` | The breadth-first propagation, with reusable buffers. |
| `ChunkLightPropagator` | The same search across all sections of a chunk, so light crosses their borders. |
| `MinestomBlockLightSource` | Answers `BlockLightSource` from the block registry. |
| `ChunkLightState` | Keeps a calculated result and updates it incrementally, including the retraction pass and the sky heightmap. |
| `ChunkLightService` | Reads a chunk, runs the propagation, settles the borders against the neighbours, writes the result back. |

`BlockLightSource` exists for the same reason `PaletteEntryResolver` does in the Anvil package: it
keeps the registry out of the algorithm, so the propagation is verified with a handful of fake
blocks and no server at all.

## Where the resources are saved

### Time

The dominant cost of a naive propagation is not the search but the block lookups. A breadth-first
search reaches a block from up to six directions, and resolving palette → block → registry →
occlusion shape on each of those visits repeats the same work.

`SectionOpacity` resolves every **distinct state id** of a section exactly once when the table is
built and answers from two flat `byte[]` afterwards — an array index instead of a registry walk.
A section of 4096 stone blocks costs one lookup, which
`SectionOpacityTest#testEveryDistinctStateIsResolvedOnlyOnce` pins down.

Two further short cuts:

- A section without any emitting block returns immediately with a uniform dark result. No buffer is
  touched, no queue is built.
- Because a level drops by exactly one per block and the search is breadth-first, every position is
  reached with its final level on the first visit. No position is ever revisited or re-queued.

### Memory

- **Uniform sections carry no array.** `LightNibbles` keeps a single level and allocates the
  2048-byte array only when a level actually differs. Most sections of a world are either fully dark
  or fully lit, so this is the common case rather than an edge case. `fill` releases the array again.
- **A fully dark section reports an empty array** (`toArray().length == 0`), which is how the file
  format stores "no light" — nothing is written for it.
- **The propagator reuses its buffers.** The level buffer and the queue are allocated once per
  instance and cleared per run, so repeated propagation allocates nothing beyond the result. An
  instance is therefore reusable but thread confined — use one per worker rather than sharing one.
- **The queue is an `int[]`, not a collection.** No boxing, no growth: a section has 4096 positions
  and each is queued at most once, so the array is sized exactly once.

## Correctness details worth knowing

**Occlusion is per face, not per block.** Roughly one in seven block types of the game occludes some
faces and not others — slabs, stairs, snow, farmland, dirt paths, lecterns, stonecutters. A design
storing one flag per block answers those wrongly. `SectionOpacity` stores a six-bit mask per block;
`MinestomBlockLightSourceTest` pins the behaviour down on real bottom and top slabs.

**Only the entered face is tested.** Light passing from A to B is blocked by the face of **B** it
enters, not by the face of A it leaves. Testing both would leave every emitting block that is opaque
itself dark — and glowstone is exactly that. This is checked end to end against the real registry.

**Unknown block states are transparent, not fatal.** `Block.fromStateId` indexes an array without a
bounds check and throws for an id outside the known range. The adapter turns that into an absent
block, because a propagation must not lose a whole section over one unknown state.

**The face mapping is pinned by a test.** The adapter maps its faces onto the server's by ordinal.
`testTheFaceOrderMatchesTheOneOfTheServer` fails if Minestom ever reorders its enum, which would
otherwise silently shift every occlusion answer to the wrong face.

## Usage

```java
import net.theevilreaper.aves.instance.light.LightNibbles;
import net.theevilreaper.aves.instance.light.LightPropagator;
import net.theevilreaper.aves.instance.light.MinestomBlockLightSource;
import net.theevilreaper.aves.instance.light.SectionOpacity;

// One per worker thread; it keeps reusable buffers.
LightPropagator propagator = new LightPropagator();
MinestomBlockLightSource source = new MinestomBlockLightSource();

int[] stateIds = new int[LightNibbles.BLOCK_COUNT];  // block states of one section
// ... fill stateIds from a palette ...

LightNibbles light = propagator.propagate(SectionOpacity.of(stateIds, source));

int level = light.get(8, 8, 8);
byte[] stored = light.toArray();                     // empty when the section is dark
```

`BlockLightSource` can be implemented directly to run the engine without a server:

```java
BlockLightSource fake = new BlockLightSource() {
    @Override public int emission(int stateId) { return stateId == LAMP ? 15 : 0; }
    @Override public boolean blocksFace(int stateId, BlockFace face) { return stateId == STONE; }
};
```

## Using it with a chunk loader or an instance

`ChunkLightService` is the entry point. It reads the block states of a chunk, propagates, and hands
the result to the sections through `Light#set(byte[])`:

```java
import net.theevilreaper.aves.instance.light.ChunkLightService;

ChunkLightService lighting = new ChunkLightService();   // one per worker thread

Chunk chunk = instance.loadChunk(0, 0).join();
lighting.calculate(chunk);

int level = lighting.blockLightAt(chunk, 8, 40, 8);
```

This works with **any** chunk, regardless of which loader produced it — the Anvil loader of Aves,
the one Minestom ships with, or a generated chunk. A test covers the round trip through
`AvesAnvilLoader` explicitly.

Two properties make this the stable way in:

- `Light#set(byte[])` is not marked internal, unlike `calculateInternal` / `calculateExternal`. The
  service therefore does not implement the `Light` interface and cannot break when the signatures of
  those internal methods change.
- `set` clears the update flag of the section, so the server does not recompute what was just
  written.

Locking follows the same three-stage split the Anvil loader uses: block states are read under the
read lock, the propagation runs with **no** lock held, and only the transfer of the result takes the
write lock.

### Sky light

```java
lighting.calculateSky(chunk);
```

Sky light enters from above and falls straight down **without losing a level** until something stops
it — which is why an open field is fully lit at every height while a cave is dark. Only after the
fall is interrupted does it spread like any other light, losing one level per block.

### Across chunk borders

```java
lighting.calculateWithNeighbours(instance, chunkX, chunkZ);
```

Lighting a chunk on its own ends its light at the border, which shows up as a straight dark line
every sixteen blocks. This method exchanges the border levels with every already loaded neighbour in
both directions. Neighbours that are not loaded are skipped rather than forced to load.

One round of that exchange is not enough. A source in the corner of a chunk sends light through two
borders, and the light that entered a neighbour has to leave it again on another side to arrive in
the chunk diagonally behind it. The exchange therefore repeats over the whole area — the chunk and
the eight positions around it — until no chunk of it raises a level any more:

| Property | How it is reached |
| --- | --- |
| Terminates | An injection only ever raises a level, and a level is capped at fifteen, so the repetition walks towards a fixed point. |
| Same result every time | The area is a fixed-size array walked in a fixed order, not a map. Since every step only raises levels, the fixed point does not depend on the order either. |
| Reads a chunk once | The opacity tables of every participating chunk are built once, before the first round, and reused by all of them. |
| Cannot loop forever | The amount of rounds is capped at sixteen, which is one more than the highest level that can exist. Hitting the cap is reported through `LOGGER.warn` instead of being accepted silently. |

A radius of one chunk is enough because a level of fifteen cannot survive sixteen blocks of travel,
so nothing the middle chunk emits can reach a second ring.

### Incremental updates

`ChunkLightService#calculate` always recomputes the whole chunk. For a single block change that is
wasteful, and `ChunkLightState` exists for that case:

```java
ChunkLightState state = ChunkLightState.blockLight(opacityTables);

// after a block changed at that position
state.update(updatedOpacityTables, x, y, z);
List<LightNibbles> light = state.toSections();
```

Adding brightness is straightforward — it only spreads. **Removing** it is the hard case and the
reason this class exists: when a light source disappears, the brightness it had spread is still
stored in every block around it, and spreading again would keep that glow forever. The update
therefore runs two passes. The first retracts every level that originated from the changed position
and collects the still valid levels it meets at the edge of the retracted area; the second spreads
those back in.

`ChunkLightStateTest#testTheIncrementalResultMatchesAFullRecalculation` asserts that the incremental
result is identical to a full recalculation, block for block.

#### Sky light updates

Sky light has an origin no block holds: it falls in from above. An update can therefore not tell
from the levels alone which positions lost their origin and which gained one, and a state that holds
sky light keeps a heightmap for that reason — the highest position that stops the sky, per column.

A block change moves exactly one column of that heightmap, and the difference between the old and
the new height names the positions whose origin changed:

| Change | Effect on the column |
| --- | --- |
| A block is placed above the current height | Everything between the old and the new height falls out of the open sky and gives its level back. What is left is refilled from the sides, which is why a single pillar leaves a level of fourteen below it rather than darkness. |
| The highest blocking block is removed | The column opens down to the next block below, and every position in between receives the full level again and spreads it. |
| The change is below the height | The height stays where it is. The changed position is retracted and refilled from its neighbours, exactly as a block light update works. |

Only the changed column is walked again, so an update no longer re-seeds all two hundred and fifty
six columns of the chunk.

`SkyLightUpdateTest` asserts the result against a full recalculation block for block, for both
directions, for a change that is not in the highest blocking position, and for a seeded sequence of
random changes that verifies the equality after every single one of them.

### When to call what

| Situation | Method |
| --- | --- |
| Chunk loaded without stored light, or generated | `calculate` / `calculateSky` |
| Chunk loaded and neighbours matter | `calculateWithNeighbours` |
| A single block changed | `ChunkLightState#update` |

## Limits

- **No `Light` implementation.** The engine deliberately does not implement
  `net.minestom.server.instance.light.Light`. It writes its result through `set` instead, which
  avoids depending on the internal calculation methods of that interface.
- **The exchange covers one ring of chunks.** `calculateWithNeighbours` settles the chunk and the
  eight positions around it. That is enough for the light of the middle chunk, but the outer chunks
  of the area are not settled against their own neighbours outside of it.
- **`Section.clone()` discards foreign light.** Should an adapter be built later, note that
  `Section.clone()` calls `Light.sky()` / `Light.block()` outright, so any custom implementation is
  silently replaced on copy. `LightingChunk.copy()` would have to be overridden.

## Tests

Everything except `MinestomBlockLightSourceTest` runs without a Minestom server. The adapter test
uses Cyano's `MicrotusExtension` because it needs the real block registry — which is the point: the
directional occlusion of a slab is only meaningful against real block data.
