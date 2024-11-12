package de.icevizion.aves.util.functional;

/**
 * The interface represents a consumer which does not return a value after its execution.
 * It can be used for method references or lambda expressions where no return value is needed.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.6.0
 */
@FunctionalInterface
public interface VoidConsumer {

    /**
     * Triggers the logic which is implemented in the method.
     */
    void apply();

}
