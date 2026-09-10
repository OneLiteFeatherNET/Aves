package net.theevilreaper.aves.inventory;

import net.kyori.adventure.text.Component;
import net.minestom.server.instance.Instance;
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
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks that {@link GlobalInventoryBuilder#applyDataLayout()} can't deadlock against the other synchronized
 * sections of the builder.
 * <p>
 * All monitors of the builder hierarchy currently guard the same object ({@code this}), which makes the nesting
 * {@link InventoryBuilder#updateInventory(net.minestom.server.inventory.Inventory, java.util.Locale, boolean)}
 * to {@link InventoryBuilder#retrieveDataLayout()} reentrant and therefore deadlock free. This test pins that
 * property down, so introducing a second lock or a blocking wait inside the monitor turns red instead of
 * silently hanging a production server.
 *
 * @author TheMeinerLP
 * @version 1.0.0
 * @since 1.0.0
 */
@ExtendWith(MicrotusExtension.class)
class GlobalInventoryBuilderDeadlockIntegrationTest {

    private static final Component TITLE_COMPONENT = Component.text("Deadlock");
    private static final InventoryType INVENTORY_TYPE = InventoryType.CHEST_2_ROW;
    private static final Duration PROBE_TIMEOUT = Duration.ofSeconds(10);
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
     * Hammers every synchronized section of the builder from four threads at once.
     * Two threads call {@link GlobalInventoryBuilder#applyDataLayout()} directly, one drives the nested
     * {@code updateInventory -> retrieveDataLayout} path and one keeps invalidating the data layout so that
     * nested path is actually taken instead of short circuiting on a valid layout.
     *
     * @param env the environment from the test extension
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testApplyDataLayoutUnderConcurrentAccess(@NotNull Env env) {
        GlobalInventoryBuilder builder = createPreparedBuilder(env);

        DeadlockAssertions.assertCompletesWithoutDeadlock(
                PROBE_TIMEOUT,
                repeat(builder::applyDataLayout),
                repeat(builder::applyDataLayout),
                repeat(() -> {
                    builder.invalidateLayout();
                    builder.updateInventory();
                }),
                repeat(builder::invalidateDataLayout)
        );

        assertNotNull(builder.getDataLayout(), "The data layout must survive the concurrent access");
        assertNotNull(builder.getInventory(), "The inventory must survive the concurrent access");
    }

    /**
     * Verifies that a single thread may enter the nested monitors of the builder without blocking itself.
     * This is the reentrancy guarantee the production code relies on when {@code updateInventory} calls
     * {@code retrieveDataLayout} while already holding the monitor.
     *
     * @param env the environment from the test extension
     */
    @Test
    @Timeout(value = 30, unit = TimeUnit.SECONDS, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
    void testNestedMonitorEntryIsReentrant(@NotNull Env env) {
        GlobalInventoryBuilder builder = createPreparedBuilder(env);

        DeadlockAssertions.assertCompletesWithoutDeadlock(PROBE_TIMEOUT, () -> {
            synchronized (builder) {
                builder.applyDataLayout();
                builder.invalidateLayout();
                builder.updateInventory();
            }
        });

        assertTrue(true, "Reaching this point means the nested monitor entry returned");
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
     * Both are required, because {@link GlobalInventoryBuilder#applyDataLayout()} returns immediately when the
     * data layout is still null, which would make the whole test vacuous.
     *
     * @param env the environment used to tick the scheduler
     * @return the prepared builder
     */
    private @NotNull GlobalInventoryBuilder createPreparedBuilder(@NotNull Env env) {
        GlobalInventoryBuilder builder = new GlobalInventoryBuilder(TITLE_COMPONENT, INVENTORY_TYPE);
        InventoryLayout layout = InventoryLayout.fromType(INVENTORY_TYPE);
        InventoryLayout dataLayout = InventoryLayout.fromType(INVENTORY_TYPE);

        layout.setItem(0, ItemStack.of(Material.STONE));
        dataLayout.setItems(LayoutCalculator.fillRow(INVENTORY_TYPE), ItemStack.of(Material.DIAMOND));

        builder.setLayout(layout);
        builder.setDataLayoutFunction(previousLayout -> dataLayout);

        // Creates the inventory and schedules the data layout retrieval for the next tick
        assertNotNull(builder.getInventory());

        env.tick();

        assertNotNull(builder.getDataLayout(), "The scheduled data layout retrieval did not run");
        return builder;
    }
}
