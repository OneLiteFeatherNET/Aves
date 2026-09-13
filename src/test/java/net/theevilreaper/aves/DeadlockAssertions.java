package net.theevilreaper.aves;

import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * Test helper which runs several tasks in parallel and fails when they don't finish in time.
 * If the tasks are stuck, the {@link ThreadMXBean} is asked for the actual deadlock, so the failure message names
 * the involved threads and monitors instead of only reporting a timeout.
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.0.0
 */
public final class DeadlockAssertions {

    private static final String THREAD_PREFIX = "deadlock-probe-";
    private static final Duration START_TIMEOUT = Duration.ofSeconds(5);

    private DeadlockAssertions() {
        throw new UnsupportedOperationException("This class can not be instantiated");
    }

    /**
     * Starts every given task on its own thread, releases all of them at the same time and waits for their completion.
     * <p>
     * The probe threads are daemon threads on purpose. A thread which is blocked on a monitor can't be interrupted,
     * so a real deadlock would keep a non daemon thread and with it the whole Gradle test JVM alive forever.
     *
     * @param timeout the maximum duration all tasks may take together
     * @param tasks   the tasks to run in parallel
     */
    public static void assertCompletesWithoutDeadlock(@NotNull Duration timeout, @NotNull Runnable... tasks) {
        CyclicBarrier startGate = new CyclicBarrier(tasks.length);
        CountDownLatch finished = new CountDownLatch(tasks.length);
        List<Throwable> failures = Collections.synchronizedList(new ArrayList<>());
        List<Thread> threads = new ArrayList<>(tasks.length);

        for (int i = 0; i < tasks.length; i++) {
            Thread thread = createProbeThread(tasks[i], i, startGate, finished, failures);
            threads.add(thread);
            thread.start();
        }

        if (!awaitCompletion(finished, timeout)) {
            throw new AssertionError(describeStuckThreads(timeout, threads));
        }

        if (!failures.isEmpty()) {
            AssertionError error = new AssertionError("The probe threads reported " + failures.size() + " exception(s)");
            failures.forEach(error::addSuppressed);
            throw error;
        }
    }

    /**
     * Waits on the given barrier and converts every checked exception into an unchecked one.
     * Useful to synchronize probe threads at an exact point inside the code under test.
     *
     * @param barrier the barrier to wait on
     */
    public static void awaitQuietly(@NotNull CyclicBarrier barrier) {
        try {
            barrier.await(START_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting on the barrier", exception);
        } catch (BrokenBarrierException | TimeoutException exception) {
            throw new IllegalStateException("The barrier was not reached by all parties", exception);
        }
    }

    /**
     * Creates a single daemon probe thread which waits at the start gate before it runs the given task.
     *
     * @param task      the task to execute
     * @param index     the index of the task, used for the thread name
     * @param startGate the barrier which releases all probe threads at the same time
     * @param finished  the latch which counts down when the task is done
     * @param failures  the collection which receives every exception thrown by the task
     * @return the created thread which is not started yet
     */
    private static @NotNull Thread createProbeThread(
            @NotNull Runnable task,
            int index,
            @NotNull CyclicBarrier startGate,
            @NotNull CountDownLatch finished,
            @NotNull List<Throwable> failures
    ) {
        Thread thread = new Thread(() -> {
            try {
                startGate.await(START_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
                task.run();
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                failures.add(exception);
            } catch (Throwable throwable) {
                failures.add(throwable);
            } finally {
                finished.countDown();
            }
        }, THREAD_PREFIX + index);
        thread.setDaemon(true);
        return thread;
    }

    /**
     * Waits until every probe thread counted down the latch or the timeout is reached.
     *
     * @param finished the latch to wait for
     * @param timeout  the maximum duration to wait
     * @return true when all threads finished in time otherwise false
     */
    private static boolean awaitCompletion(@NotNull CountDownLatch finished, @NotNull Duration timeout) {
        try {
            return finished.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new AssertionError("Interrupted while waiting for the probe threads", exception);
        }
    }

    /**
     * Builds the failure message for stuck threads.
     * When the JVM detects a real deadlock the involved threads including their monitors are dumped,
     * otherwise the stack traces of all probe threads are used as fallback.
     *
     * @param timeout the timeout which was exceeded
     * @param threads the probe threads
     * @return the message to report
     */
    private static @NotNull String describeStuckThreads(@NotNull Duration timeout, @NotNull List<Thread> threads) {
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        long[] deadlockedIds = threadBean.findDeadlockedThreads();

        // Only report a deadlock which actually involves the threads of this call. A deadlock left behind by an
        // earlier test would otherwise be attached to every following failure and point at the wrong code.
        if (deadlockedIds != null && involvesProbeThreads(deadlockedIds, threads)) {
            ThreadInfo[] infos = threadBean.getThreadInfo(deadlockedIds, true, true);
            return "Deadlock detected after " + timeout + ":\n" + Arrays.stream(infos)
                    .map(ThreadInfo::toString)
                    .collect(Collectors.joining("\n"));
        }

        return "The probe threads did not finish within " + timeout
                + " and the JVM reports no deadlock. This points to a livelock, starvation or a task which is simply too slow:\n"
                + threads.stream()
                .map(DeadlockAssertions::describeThread)
                .collect(Collectors.joining("\n"));
    }

    /**
     * Checks whether at least one of the deadlocked threads reported by the JVM is a probe thread of this call.
     *
     * @param deadlockedIds the thread ids reported by the {@link ThreadMXBean}
     * @param threads       the probe threads of the current call
     * @return true when the reported deadlock involves one of the probe threads otherwise false
     */
    private static boolean involvesProbeThreads(long @NotNull [] deadlockedIds, @NotNull List<Thread> threads) {
        return threads.stream()
                .mapToLong(Thread::threadId)
                .anyMatch(id -> Arrays.stream(deadlockedIds).anyMatch(deadlockedId -> deadlockedId == id));
    }

    /**
     * Renders the name, state and stack trace of a single thread.
     *
     * @param thread the thread to describe
     * @return the rendered thread information
     */
    private static @NotNull String describeThread(@NotNull Thread thread) {
        return thread.getName() + " [" + thread.getState() + "]\n\tat " + Arrays.stream(thread.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n\tat "));
    }
}
