# Light engine

A light propagation for a single section that resolves the properties of a block once instead of on
every visit, and stores a uniform section without an array. The algorithm itself knows nothing about
Minestom, which is what makes it testable without a running server.

> **Experimental.** Every public type of `net.theevilreaper.aves.instance.light` is annotated
> `@ApiStatus.Experimental`. The API may still change.

> **Scope.** This package computes the light of one section from the blocks it holds. It does not
> replace Minestom's light system: it does not implement `net.minestom.server.instance.light.Light`,
> does not propagate across section or chunk borders, and does not compute sky light. See
> [Limits](#limits).

## When this is worth using

Honest answer first, because it decides whether the package is relevant at all:

| Workload | Does light computation run? |
| --- | --- |
| Loading pre-lit worlds from `.mca` | **No.** The stored light is applied with `Light#set`, which clears `requiresUpdate()`. Nothing is recomputed. |
| Generated worlds without stored light | Yes |
| Runtime block placement | Yes |

For loading pre-built maps from region files — the dominant Aves use case — a light engine
contributes nothing, because that code path never executes. The measured cost of loading such a
chunk is dominated by NBT parsing and zlib inflation instead.

## Design

Four types, each with one responsibility.

```
  int[] stateIds ──► SectionOpacity ──► LightPropagator ──► LightNibbles
                          ▲
                          │ resolves each distinct state once
                     BlockLightSource
                          ▲
                          │ implemented by
                  MinestomBlockLightSource   ◄── the only Minestom dependency
```

| Type | Responsibility |
| --- | --- |
| `LightNibbles` | Storage. Two levels per byte, uniform sections without an array. |
| `BlockLightSource` | Abstraction over "how bright is this block and which faces does it block". |
| `SectionOpacity` | Precomputed table of those properties for one section. |
| `LightPropagator` | The breadth-first propagation, with reusable buffers. |
| `MinestomBlockLightSource` | Answers `BlockLightSource` from the block registry. |

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

## Limits

- **No `Light` implementation.** The package does not implement
  `net.minestom.server.instance.light.Light`, so it is not plugged into a chunk yet. That interface
  is not sealed and an adapter is possible, but `calculateInternal` / `calculateExternal` are
  `@ApiStatus.Internal` and may change between Minestom versions.
- **No cross-section propagation.** Light stops at the section border. Real world lighting requires
  propagating into the six neighbouring sections and back.
- **No sky light.** Sky light needs a heightmap and a downward seeding pass, neither of which exists
  here.
- **No decrease pass.** Removing a light source requires retracting the light it had spread, which
  is the hardest part of an incremental light engine and is not implemented.
- **`Section.clone()` discards foreign light.** Should an adapter be built later, note that
  `Section.clone()` calls `Light.sky()` / `Light.block()` outright, so any custom implementation is
  silently replaced on copy. `LightingChunk.copy()` would have to be overridden.

## Tests

Everything except `MinestomBlockLightSourceTest` runs without a Minestom server. The adapter test
uses Cyano's `MicrotusExtension` because it needs the real block registry — which is the point: the
directional occlusion of a slab is only meaningful against real block data.
