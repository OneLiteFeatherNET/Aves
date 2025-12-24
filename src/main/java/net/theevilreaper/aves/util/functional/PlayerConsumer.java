package net.theevilreaper.aves.util.functional;

import net.minestom.server.entity.Player;

import java.util.function.Consumer;

/**
 * Represents an operation that accepts a single {@code Player}-valued argument and returns no result.
 * This is the type specialization of {@link Consumer} for {@code Player}.
 * Unlike most other functional interfaces, {@code PlayerConsumer} is expected to operate via side effects.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #accept(Player)}.
 *
 * @see Consumer
 * @since 1.0.0
 */
public interface PlayerConsumer {

    /**
     * Performs this operation on the given argument.
     *
     * @param player the input argument
     */
    void accept(Player player);

    /**
     * Returns a composed {@code PlayerConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation.
     * If performing either operation throws an exception, it is relayed to the caller of the composed operation.
     * If performing this operation throws an exception, the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code PlayerConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default PlayerConsumer andThen(PlayerConsumer after) {
        return (Player player) -> {
            accept(player);
            after.accept(player);
        };
    }
}
