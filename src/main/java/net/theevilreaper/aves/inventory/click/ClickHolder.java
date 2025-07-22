package net.theevilreaper.aves.inventory.click;

import net.minestom.server.inventory.click.Click;
import org.jetbrains.annotations.NotNull;

/**
 * The {@link ClickHolder} interface represents a holder for click actions in the Aves inventory system.
 * It can hold different types of clicks, including a Minestom click, a cancel click, or a no-operation click.
 * This allows for flexible handling of click actions within the inventory system.
 *
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.9.0
 */
public sealed interface ClickHolder permits ClickHolder.MinestomClick, ClickHolder.CancelClick, ClickHolder.NOPClick {

    /**
     * Returns a {@link ClickHolder} that represents no click.
     *
     * @return the no-click reference
     */
    static @NotNull ClickHolder noClick() {
        return InternalClickRegistry.NOP_CLICK;
    }

    /**
     * Returns a {@link ClickHolder} that represents a cancel click.
     *
     * @return the cancel-click reference
     */
    static @NotNull ClickHolder cancelClick() {
        return InternalClickRegistry.CANCEL_CLICK;
    }

    /**
     * Creates a {@link ClickHolder} from a {@link Click}.
     *
     * @param click the click to wrap
     * @return the wrapped click holder
     */
    static @NotNull ClickHolder of(@NotNull Click click) {
        return new MinestomClick(click);
    }

    /**
     * The {@link MinestomClick} represents a click that holds the {@link Click} instance from Minestom.
     *
     * @param click instance to hold
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.9.0
     */
    record MinestomClick(@NotNull Click click) implements ClickHolder {
    }

    /**
     * The {@link CancelClick} represents a click used to cancel an action.
     * This is used when a click should not perform any action, such as in the case of a canceled inventory action.
     *
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.9.0
     */
    final class CancelClick implements ClickHolder {
    }

    /**
     * The {@link NOPClick} represents a no-operation click.
     * This is used when no click action is desired, effectively acting as a placeholder.
     *
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.9.0
     */
    final class NOPClick implements ClickHolder {

    }

    /**
     * InternalClickRegistry is a utility class that holds references to the internal click types.
     * It provides static instances for cancel and no-operation clicks to avoid unnecessary object creation.
     *
     * @author theEvilReaper
     * @version 1.0.0
     * @since 1.9.0
     */
    final class InternalClickRegistry {

        private static final ClickHolder CANCEL_CLICK;
        private static final ClickHolder NOP_CLICK;

        static {
            CANCEL_CLICK = new CancelClick();
            NOP_CLICK = new NOPClick();
        }

        private InternalClickRegistry() {
        }
    }
}
