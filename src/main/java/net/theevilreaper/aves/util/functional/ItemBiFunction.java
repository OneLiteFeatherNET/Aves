package net.theevilreaper.aves.util.functional;

import net.theevilreaper.aves.item.IItem;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

/**
 * Represents a function that accepts an {@link IItem} and another argument, producing a result.
 * This is a two-arity specialization of {@link Function} with the first argument predefined as {@link IItem}.
 * It allows for operations that involve an {@link IItem} and an additional parameter.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(IItem, Object)}.
 *
 * @param <T> the type of the item argument to the function, extending {@link IItem}
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @since 1.5.1
 * @version 1.0.0
 */
@FunctionalInterface
public interface ItemBiFunction<T extends IItem, U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param item the {@link IItem} to be processed by the function
     * @param u the additional argument to be used in the function
     * @return the result of the function
     */
    @Nullable R apply(@Nullable T item, @Nullable U u);

    /**
     * Returns a composed function that first applies this function to its input,
     * and then applies the after function to the result.
     *
     * @param <V> the type of output of the after function, and of the composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then applies the after function
     * @throws NullPointerException if after is null
     */
    default <V> ItemBiFunction<T, U, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U v) -> after.apply(this.apply(t, v));
    }
}
