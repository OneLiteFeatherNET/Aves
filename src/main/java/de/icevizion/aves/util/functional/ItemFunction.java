package de.icevizion.aves.util.functional;

import de.icevizion.aves.item.IItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a function that accepts an {@link IItem}-valued argument and produces a
 * result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(IItem)}.
 *
 * @param <R> the type of the result of the function
 *
 * @see Function
 * @author theEvilReaper
 * @since 1.5.1
 * @version 1.0.0
 */
@FunctionalInterface
public interface ItemFunction<T extends IItem, R> {

    /**
     * Applies this function to the given argument.
     *
     * @param item the function argument
     * @return the function result
     */
    @Nullable R apply(@Nullable T item);
}
