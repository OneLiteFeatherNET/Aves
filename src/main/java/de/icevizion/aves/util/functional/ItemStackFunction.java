package de.icevizion.aves.util.functional;

import net.minestom.server.item.ItemStack;
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
}
