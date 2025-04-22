package net.theevilreaper.aves.util.functional;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

/**
 * The {@link PathFilter<T>} is a functional interface that can be used to bind a filter logic to a method reference.
 * It can be used for filtering a {@link Stream} of {@link Path} objects and returning a list of {@link T} objects.
 *
 * @param <T> the type of the objects to be filtered
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 */
@FunctionalInterface
public interface PathFilter<T> {

    /**
     * Filters the given stream of map paths and returns a list of {@link T} objects.
     *
     * @param stream the stream of map paths to filter
     * @return a list of filtered {@link T} objects
     */
    @NotNull List<T> filter(@NotNull Stream<Path> stream);
}
