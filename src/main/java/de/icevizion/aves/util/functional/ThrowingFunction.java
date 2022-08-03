package de.icevizion.aves.util.functional;

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
            throw new RuntimeException(exception);
        }
    }

    R acceptThrows(T t) throws Exception;
}
