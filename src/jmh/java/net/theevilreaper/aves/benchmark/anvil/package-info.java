/**
 * Contains the benchmarks of the Anvil chunk loader.
 * <p>
 * The benchmarks cover the bit packing, the palette encoding, the compression and the byte transfer
 * of a region file, and one benchmark which splits a whole chunk save into those stages so the cost
 * inside a lock can be compared against the cost outside of it.
 * </p>
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
package net.theevilreaper.aves.benchmark.anvil;
