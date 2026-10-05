package net.theevilreaper.aves.util.functional;

/**
 * Represents an operation which accepts a single input argument and is allowed to throw an exception.
 *
 * @param <T> the input type
 * @author theEvilReaper
 * @version 1.0.0
 * @since 2.0.0
 */
@FunctionalInterface
public interface ThrowingConsumer<T> {

    /**
     * Performs this operation on the given argument, allowing to throw an exception.
     *
     * @param t the input value
     * @throws Exception if an exception occurs during the execution
     */
    void acceptThrows(T t) throws Exception;
}
