package net.theevilreaper.aves.util.functional;

import net.theevilreaper.aves.util.exception.ThrowingException;

import java.util.function.Function;

/**
 * The class is an extension for the {@link Function} which allows to throw an exception
 *
 * @param <T> the input type
 * @param <R> the output type
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.2.0
 */
@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

    /**
     * Applies this function to the given argument, allowing to throw an exception.
     *
     * @param t the input value
     * @return the result of the function
     * @throws ThrowingException if an exception occurs during the execution
     */
    @Override
    default R apply(T t) {
        try {
            return acceptThrows(t);
        } catch (Exception exception) {
            throw new ThrowingException(exception);
        }
    }

    /**
     * Accepts an input and returns a result, allowing to throw an exception.
     *
     * @param t the input value
     * @return the result of the function
     * @throws ThrowingException if an exception occurs during the execution
     */
    R acceptThrows(T t) throws ThrowingException;
}
