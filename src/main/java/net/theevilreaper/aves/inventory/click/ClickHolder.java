package net.theevilreaper.aves.inventory.click;

import net.minestom.server.inventory.click.Click;
import org.jetbrains.annotations.NotNull;

public sealed interface ClickHolder permits ClickHolder.MinestomClick, ClickHolder.CancelClick, ClickHolder.NOPClick {

    static @NotNull ClickHolder noClick() {
        return InternalClickRegistry.NOP_CLICK;
    }

    static @NotNull ClickHolder cancelClick() {
        return InternalClickRegistry.CANCEL_CLICK;
    }

    static @NotNull ClickHolder of(@NotNull Click click) {
        return new MinestomClick(click);
    }

    record MinestomClick(@NotNull Click click) implements ClickHolder {
    }


    final class CancelClick implements ClickHolder {
    }

    final class NOPClick implements ClickHolder {

    }

    final class InternalClickRegistry {

        private static final ClickHolder CANCEL_CLICK;
        private static final ClickHolder NOP_CLICK;

        static {
            CANCEL_CLICK = new CancelClick();
            NOP_CLICK = new NOPClick();
        }

        private InternalClickRegistry() {}
    }

}
