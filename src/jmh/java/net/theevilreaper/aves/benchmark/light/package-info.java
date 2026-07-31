/**
 * Contains the benchmarks of the light engine.
 * <p>
 * The benchmarks cover the nibble storage of a light section, the construction of the opacity table
 * which the propagation reads, and the propagation itself for a single section and for a whole
 * chunk column. None of them starts a server, because a registry lookup would otherwise dominate
 * every measurement.
 * </p>
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.16.0
 */
package net.theevilreaper.aves.benchmark.light;
