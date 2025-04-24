package net.theevilreaper.aves.util.functional;

import net.theevilreaper.aves.map.BaseMap;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a function that accepts a {@link BaseMap}-valued argument and another argument,
 * producing a result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(BaseMap, Object)}.
 *
 * @param <G> the type of the BaseMap argument
 * @param <U> the type of the additional argument to the function
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @since 1.5.1
 * @version 1.0.0
 */
@FunctionalInterface
public interface BaseMapBiFunction<G extends BaseMap, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param baseMap the function argument
     * @param u the additional argument to the function
     * @return the function result
     */
    @Nullable R apply(@Nullable G baseMap, @Nullable U u);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <T> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if {@code after} is null
     */
    default <T> BaseMapBiFunction<G, U, T> andThen(Function<? super R, ? extends T> after) {
        if (after == null) {
            throw new NullPointerException("After function must not be null");
        }
        return (G baseMap, U u) -> after.apply(apply(baseMap, u));
    }
}
