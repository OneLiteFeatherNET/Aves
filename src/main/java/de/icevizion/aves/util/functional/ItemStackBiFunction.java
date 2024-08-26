package de.icevizion.aves.util.functional;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a function that accepts an ItemStack and another argument, producing a result.
 * This is a two-arity specialization of {@link Function} with the first argument predefined as {@link ItemStack}.
 * It allows for operations that involve an ItemStack and an additional parameter.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(ItemStack, Object)}.
 *
 * @param <U> the type of the second argument to the function
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @author theEvilReaper
 * @since 1.5.1
 * @version 1.0.0
 */
@FunctionalInterface
public interface ItemStackBiFunction<U, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param itemStack the ItemStack to be processed by the function
     * @param u the additional argument to be used in the function
     * @return the result of the function
     */
    @Nullable R apply(@Nullable ItemStack itemStack, @Nullable U u);

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
    default <T> ItemStackBiFunction<U, T> andThen(Function<? super R, ? extends T> after) {
        if (after == null) {
            throw new NullPointerException("After function must not be null");
        }
        return (ItemStack itemStack, U u) -> after.apply(apply(itemStack, u));
    }
}
