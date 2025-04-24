package net.theevilreaper.aves.util.functional;

import net.minestom.server.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a function that accepts an {@link ItemStack}-valued argument and produces a
 * result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(ItemStack)}.
 *
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @author theEvilReaper
 * @since 1.5.1
 * @version 1.0.0
 */
@FunctionalInterface
public interface ItemStackFunction<R> {

    /**
     * Applies this function to the given argument.
     *
     * @param itemStack the function argument
     * @return the function result
     */
    @Nullable R apply(@Nullable ItemStack itemStack);

    /**
     * Returns a composed {@code ItemStackFunction} that performs, in sequence, this
     * operation followed by the {@code after} operation.
     * If performing either operation throws an exception, it is relayed to the caller of the
     * composed operation.
     * If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code ItemStackFunction} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default ItemStackFunction<R> andThen(@NotNull ItemStackFunction<R> after) {
        return itemStack -> {
            this.apply(itemStack);
            return after.apply(itemStack);
        };
    }
}
