package net.theevilreaper.aves;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.time.Duration;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies {@link DeadlockAssertions} itself.
 * A deadlock assertion which can never turn red would give false confidence to every test using it, so the detection
 * is proven against a real lock order inversion here.
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.0.0
 */
class DeadlockAssertionsTest {

    private static final Duration PROBE_TIMEOUT = Duration.ofSeconds(3);

    /**
     * Two threads take the same two monitors in opposite order, which is the textbook deadlock.
     * The assertion must fail and name the deadlock instead of only reporting the timeout.
     * <p>
     * This test leaves two blocked daemon threads behind, because a thread waiting on a monitor can't be
     * interrupted. That is harmless for the remaining suite, as the detection only reports a deadlock which
     * involves the probe threads of the current call.
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testDetectsLockOrderInversion() {
        Object firstLock = new Object();
        Object secondLock = new Object();
        CyclicBarrier locksHeld = new CyclicBarrier(2);

        AssertionError error = assertThrows(AssertionError.class, () -> DeadlockAssertions.assertCompletesWithoutDeadlock(
                PROBE_TIMEOUT,
                () -> {
                    synchronized (firstLock) {
                        awaitQuietly(locksHeld);
                        synchronized (secondLock) {
                            throw new IllegalStateException("The second lock must not be reachable");
                        }
                    }
                },
                () -> {
                    synchronized (secondLock) {
                        awaitQuietly(locksHeld);
                        synchronized (firstLock) {
                            throw new IllegalStateException("The first lock must not be reachable");
                        }
                    }
                }
        ));

        assertTrue(error.getMessage().startsWith("Deadlock detected"), error.getMessage());
    }

    /**
     * Tasks which simply do their work must pass without any failure.
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testPassesForIndependentTasks() {
        AtomicInteger counter = new AtomicInteger();

        DeadlockAssertions.assertCompletesWithoutDeadlock(
                PROBE_TIMEOUT,
                counter::incrementAndGet,
                counter::incrementAndGet,
                counter::incrementAndGet
        );

        assertEquals(3, counter.get());
    }

    /**
     * An exception inside a task must not be swallowed, otherwise a thread dying early would look like success.
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testReportsExceptionsFromTasks() {
        AssertionError error = assertThrows(AssertionError.class, () -> DeadlockAssertions.assertCompletesWithoutDeadlock(
                PROBE_TIMEOUT,
                () -> {
                    throw new IllegalStateException("boom");
                },
                () -> {
                    // nothing to do, this task only exists to release the start gate
                }
        ));

        assertEquals(1, error.getSuppressed().length);
        assertInstanceOf(IllegalStateException.class, error.getSuppressed()[0]);
    }

    /**
     * Waits on the given barrier and converts the checked exceptions into an unchecked one.
     *
     * @param barrier the barrier to wait on
     */
    private void awaitQuietly(CyclicBarrier barrier) {
        try {
            barrier.await(PROBE_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(exception);
        } catch (BrokenBarrierException | java.util.concurrent.TimeoutException exception) {
            throw new IllegalStateException(exception);
        }
    }
}
