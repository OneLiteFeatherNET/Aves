package de.icevizion.aves.util.functional;

import de.icevizion.aves.util.exception.ThrowingException;

import java.util.function.Function;

/**
 * The class is an extension for the {@link Function} which allows to throw an exception
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.2.0
 * @param <T> the input type
 * @param <R> the output type
 */
@FunctionalInterface
public interface ThrowingFunction<T,R> extends Function<T,R> {

    @Override
    default R apply(T t) {
        try {
            return acceptThrows(t);
        } catch (Exception exception) {
            throw new ThrowingException(exception);
        }
    }

    R acceptThrows(T t) throws ThrowingException;
}
