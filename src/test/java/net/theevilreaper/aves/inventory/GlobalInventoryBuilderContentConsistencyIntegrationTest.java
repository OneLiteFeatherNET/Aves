package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.instance.Instance;
import net.minestom.server.inventory.Inventory;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import net.minestom.testing.Env;
import net.minestom.testing.extension.MicrotusExtension;
import net.theevilreaper.aves.DeadlockAssertions;
import net.theevilreaper.aves.inventory.layout.InventoryLayout;
import net.theevilreaper.aves.inventory.util.LayoutCalculator;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Checks that the inventory content stays consistent while several threads work on the builder.
 * <p>
 * The deadlock test only proves that nothing gets stuck. This test asks the harder question: does the builder
 * monitor actually protect the item stacks it is supposed to guard? Every mutation of the inventory must happen
 * under the same monitor, otherwise a thread holding that monitor can observe a half written inventory.
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.0.0
 */
@ExtendWith(MicrotusExtension.class)
class GlobalInventoryBuilderContentConsistencyIntegrationTest {

    private static final Component TITLE_COMPONENT = Component.text("Consistency");
    private static final InventoryType INVENTORY_TYPE = InventoryType.CHEST_2_ROW;
    private static final Duration PROBE_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration OBSERVATION_WINDOW = Duration.ofSeconds(1);
    private static final int[] DATA_SLOTS = LayoutCalculator.fillRow(INVENTORY_TYPE);
    private static final int LAYOUT_SLOT = 0;
    private static final int ITERATIONS = 250;

    private Instance instance;

    @BeforeEach
    void setup(@NotNull Env env) {
        this.instance = env.createFlatInstance();
    }

    @AfterEach
    void teardown(@NotNull Env env) {
        env.destroyInstance(instance, true);
    }

    /**
     * Holds the builder monitor while another thread is in the middle of applying the inventory layout and reads
     * the inventory from there.
     * <p>
     * The observing thread owns the monitor which {@link GlobalInventoryBuilder#applyDataLayout()} uses to guard
     * its writes, so it must never see anything but the fully applied data layout. Seeing an empty slot means the
     * inventory is mutated without that monitor and the guard is an illusion.
     *
     * @param env the environment from the test extension
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testInventoryContentIsGuardedByTheBuilderMonitor(@NotNull Env env) {
        GlobalInventoryBuilder builder = createPreparedBuilder(env);
        Inventory inventory = builder.getInventory();

        assertDataLayoutApplied(inventory, "before the concurrent access");

        CyclicBarrier layoutEntered = new CyclicBarrier(2);
        CountDownLatch observationDone = new CountDownLatch(1);
        List<ItemStack> observed = new ArrayList<>(DATA_SLOTS.length);

        InventoryLayout layout = builder.getLayout();
        assertNotNull(layout);
        layout.setApplyLayoutFunction((itemStacks, locale) -> {
            // At this point updateInventory already cleared the inventory and has not written anything back yet
            DeadlockAssertions.awaitQuietly(layoutEntered);
            // Give the observer the chance to read a torn state. The wait must not be a barrier: as soon as the
            // inventory is properly guarded, the observer blocks on the monitor and could never release us.
            awaitObservation(observationDone);
            itemStacks[LAYOUT_SLOT] = ItemStack.of(Material.STONE);
        });

        DeadlockAssertions.assertCompletesWithoutDeadlock(
                PROBE_TIMEOUT,
                () -> {
                    builder.invalidateLayout();
                    builder.updateInventory();
                },
                () -> {
                    DeadlockAssertions.awaitQuietly(layoutEntered);
                    synchronized (builder) {
                        for (int slot : DATA_SLOTS) {
                            observed.add(inventory.getItemStack(slot));
                        }
                    }
                    observationDone.countDown();
                }
        );

        assertEquals(DATA_SLOTS.length, observed.size(), "The observer did not read every data layout slot");
        for (int i = 0; i < observed.size(); i++) {
            assertEquals(
                    Material.DIAMOND,
                    observed.get(i).material(),
                    "Slot " + DATA_SLOTS[i] + " was observed while holding the builder monitor, "
                            + "so it must still carry the applied data layout"
            );
        }
    }

    /**
     * Hammers the builder from several threads and verifies that the inventory is in a well defined state
     * afterwards. Once every thread is done and a final {@link GlobalInventoryBuilder#applyDataLayout()} ran,
     * the data layout must be fully present regardless of how the threads interleaved before.
     *
     * @param env the environment from the test extension
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testInventoryConvergesAfterConcurrentAccess(@NotNull Env env) {
        GlobalInventoryBuilder builder = createPreparedBuilder(env);
        Inventory inventory = builder.getInventory();

        DeadlockAssertions.assertCompletesWithoutDeadlock(
                PROBE_TIMEOUT,
                repeat(builder::applyDataLayout),
                repeat(() -> {
                    builder.invalidateLayout();
                    builder.updateInventory();
                }),
                repeat(builder::invalidateDataLayout)
        );

        builder.applyDataLayout();
        assertDataLayoutApplied(inventory, "after the concurrent access");
    }

    /**
     * Waits a bounded time for the observer to finish its read.
     * A timeout is the expected outcome once the inventory is guarded correctly, because the observer is then
     * blocked on the builder monitor which this thread holds.
     *
     * @param observationDone the latch the observer counts down after its read
     */
    private void awaitObservation(@NotNull CountDownLatch observationDone) {
        try {
            observationDone.await(OBSERVATION_WINDOW.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for the observer", exception);
        }
    }

    /**
     * Asserts that every slot of the data layout carries the expected item.
     *
     * @param inventory the inventory to inspect
     * @param phase     the phase name used in the failure message
     */
    private void assertDataLayoutApplied(@NotNull Inventory inventory, @NotNull String phase) {
        for (int slot : DATA_SLOTS) {
            assertEquals(
                    Material.DIAMOND,
                    inventory.getItemStack(slot).material(),
                    "Slot " + slot + " does not carry the data layout " + phase
            );
        }
    }

    /**
     * Wraps the given action into a task which repeats it {@link #ITERATIONS} times.
     *
     * @param action the action to repeat
     * @return the repeating task
     */
    private @NotNull Runnable repeat(@NotNull Runnable action) {
        return () -> {
            for (int i = 0; i < ITERATIONS; i++) {
                action.run();
            }
        };
    }

    /**
     * Creates a builder which already owns an inventory and a resolved data layout.
     *
     * @param env the environment used to tick the scheduler
     * @return the prepared builder
     */
    private @NotNull GlobalInventoryBuilder createPreparedBuilder(@NotNull Env env) {
        GlobalInventoryBuilder builder = new GlobalInventoryBuilder(TITLE_COMPONENT, INVENTORY_TYPE);
        InventoryLayout layout = InventoryLayout.fromType(INVENTORY_TYPE);
        InventoryLayout dataLayout = InventoryLayout.fromType(INVENTORY_TYPE);

        layout.setItem(LAYOUT_SLOT, ItemStack.of(Material.STONE));
        dataLayout.setItems(DATA_SLOTS, ItemStack.of(Material.DIAMOND));

        builder.setLayout(layout);
        builder.setDataLayoutFunction(previousLayout -> dataLayout);

        // Creates the inventory and schedules the data layout retrieval for the next tick
        assertNotNull(builder.getInventory());

        env.tick();

        assertNotNull(builder.getDataLayout(), "The scheduled data layout retrieval did not run");
        return builder;
    }
}
