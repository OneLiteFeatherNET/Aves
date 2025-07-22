package net.theevilreaper.aves.util.functional;

import net.theevilreaper.aves.item.IItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * Represents a function that accepts an {@link IItem}-valued argument and produces a
 * result.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #apply(IItem)}.
 *
 * @param <T> the type of the input to the function, which must extend {@link IItem}
 * @param <R> the type of the result of the function
 * @author theEvilReaper
 * @version 1.0.0
 * @see Function
 * @since 1.5.1
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
