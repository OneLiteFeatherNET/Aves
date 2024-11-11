package de.icevizion.aves.map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Files;
import java.nio.file.Path;

public final class BaseMapEntry implements MapEntry {

    private final String mapFileNaming;
    private final Path directory;
    private Path mapFilePath;

    BaseMapEntry(@NotNull Path directory, @NotNull String mapFileNaming) {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("The given path must be a directory");
        }

        this.directory = directory;
        this.mapFileNaming = mapFileNaming;
        Path resolvedPath = directory.resolve(mapFileNaming);
        if (Files.exists(resolvedPath)) {
            this.mapFilePath = resolvedPath;
        }
    }

    @Override
    public void refresh() {
        this.mapFilePath = directory.resolve(this.mapFileNaming);
    }

    @Override
    public boolean hasStandardEnding() {
        return this.mapFileNaming.equals(MAP_FILE);
    }

    @Override
    public boolean hasMapFile() {
        return this.mapFilePath != null;
    }

    @Override
    public @Nullable Path getMapFile() {
        return this.mapFilePath;
    }
}
