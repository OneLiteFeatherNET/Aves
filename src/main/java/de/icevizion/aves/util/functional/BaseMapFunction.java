package de.icevizion.aves.util.functional;

import de.icevizion.aves.map.BaseMap;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a function that accepts any {@link BaseMap}-valued argument and produces a
 * result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(BaseMap)}.
 *
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @author theEvilReaper
 * @since 1.5.1
 * @version 1.0.0
 */
@FunctionalInterface
public interface BaseMapFunction<G extends BaseMap, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param baseMap the function argument
     * @return the function result
     */
    @Nullable R apply(@Nullable G baseMap);
}
