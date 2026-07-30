package net.theevilreaper.aves.instance.anvil;

import net.kyori.adventure.nbt.CompoundBinaryTag;
import net.minestom.server.MinecraftServer;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.RegistryKey;
import net.minestom.server.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link BiomePaletteResolver} class translates between the biome entries of the Anvil format
 * and the biome ids of the server registry.
 * <p>
 * A biome the registry does not know is replaced with the plains biome instead of failing, which
 * follows the behaviour of the built-in loader. Every replaced name is reported once through the
 * diagnostics.
 * </p>
 * <p>
 * The registry is resolved when the resolver is created instead of in a static initializer. A
 * static lookup would require a running server before the class is touched for the first time
 * which makes the surrounding code hard to test.
 * </p>
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.16.0
 */
public final class BiomePaletteResolver implements PaletteEntryResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BiomePaletteResolver.class);

    private static final String NAME_KEY = "Name";

    private final AnvilDiagnostics diagnostics;
    private final DynamicRegistry<Biome> registry;
    private final int fallbackId;

    /**
     * Creates a new resolver which uses the biome registry of the running server.
     *
     * @param diagnostics the diagnostics which throttle the reports
     */
    public BiomePaletteResolver(AnvilDiagnostics diagnostics) {
        this(diagnostics, MinecraftServer.getBiomeRegistry());
    }

    /**
     * Creates a new resolver which uses the given registry.
     *
     * @param diagnostics the diagnostics which throttle the reports
     * @param registry    the registry which holds the known biomes
     */
    public BiomePaletteResolver(AnvilDiagnostics diagnostics, DynamicRegistry<Biome> registry) {
        this.diagnostics = diagnostics;
        this.registry = registry;
        this.fallbackId = registry.getId(Biome.PLAINS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int toId(String name, @Nullable CompoundBinaryTag properties) {
        int id = this.registry.getId(RegistryKey.unsafeOf(name));

        if (id != -1) {
            return id;
        }
        if (this.diagnostics.reportUnknownBiome(name)) {
            LOGGER.warn("The biome '{}' is unknown and is replaced with plains, further chunks with it are not reported", name);
        }
        return this.fallbackId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CompoundBinaryTag toEntry(int id) {
        RegistryKey<Biome> key = this.registry.getKey(id);
        String name = key == null ? Biome.PLAINS.key().asString() : key.key().asString();
        return CompoundBinaryTag.builder().putString(NAME_KEY, name).build();
    }
}
