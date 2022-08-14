package de.icevizion.aves.util;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Taken from <a href="https://github.com/Minestom/Arena/blob/master/src/main/java/net/minestom/arena/utils/ConcurrentUtils.java">...</a>
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
public final class Futures {

    private Futures() {}

    /**
     * Used to add timeout for CompletableFutures
     *
     * @param future the future which has to complete
     * @param timeout duration to wait for the future to complete
     * @param action Action to run after the future completes or the timeout is reached.<br>
     *               Parameter means:
     *               <ul>
     *               <li><b>true</b> - the timeout is reached</li>
     *               <li><b>false</b> - future completed before timeout</li>
     *               </ul>
     * @return the new CompletionStage
     */
    public static CompletableFuture<Void> thenRunOrTimeout(@NotNull CompletableFuture<?> future, @NotNull Duration timeout, BooleanConsumer action) {
        final CompletableFuture<Boolean> f = new CompletableFuture<>();
        CompletableFuture.delayedExecutor(timeout.toNanos(), TimeUnit.NANOSECONDS).execute(() -> f.complete(true));
        future.thenRun(() -> f.complete(false));
        return f.thenAccept(action);
    }

    /**
     * Create a future from a CountDownLatch
     *
     * @return a future that completes when the countdown reaches zero
     */
    public static @NotNull CompletableFuture<Void> futureFromCountdown(@NotNull CountDownLatch countDownLatch) {
        final CompletableFuture<Void> future = new CompletableFuture<>();
        CompletableFuture.runAsync(() -> {
            try {
                countDownLatch.await();
                future.complete(null);
            } catch (InterruptedException e) {
                future.completeExceptionally(new InterruptedException(e.getMessage()));
                Thread.currentThread().interrupt();
            }
        });
        return future;
    }
}
