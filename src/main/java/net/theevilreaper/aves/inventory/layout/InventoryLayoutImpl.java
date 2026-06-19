package net.theevilreaper.aves.inventory.layout;

import net.theevilreaper.aves.inventory.InventorySlot;
import net.theevilreaper.aves.inventory.function.ApplyLayoutFunction;
import net.theevilreaper.aves.inventory.function.DefaultApplyLayoutFunction;
import net.theevilreaper.aves.inventory.function.InventoryClick;
import net.theevilreaper.aves.inventory.slot.ISlot;
import net.theevilreaper.aves.inventory.slot.TranslatedSlot;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.utils.validate.Check;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Locale;

import static net.theevilreaper.aves.inventory.util.InventoryConstants.CANCEL_CLICK;
import static net.theevilreaper.aves.inventory.util.InventoryConstants.BLANK_SLOT;

/**
 * Represents a layout which contains all items for an inventory.
 *
 * @author Patrick Zdarsky / Rxcki
 * @version 1.0.3
 * @since 1.0.12
 */
@SuppressWarnings("java:S3776")
@ApiStatus.Internal
public final class InventoryLayoutImpl implements InventoryLayout {

    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryLayoutImpl.class);

    private static final String INDEX_ERROR = "The given slot index is out of range";
    private final ISlot[] contents;

    private ApplyLayoutFunction applyLayoutFunction;

    /**
     * Creates a new instance from the {@link InventoryLayoutImpl}.
     *
     * @param type the given size for the layout
     */
    InventoryLayoutImpl(InventoryType type) {
        this.contents = new ISlot[type.getSize()];
        Arrays.fill(this.contents, BLANK_SLOT);
        this.applyLayoutFunction = new DefaultApplyLayoutFunction(this.contents);
    }

    /**
     * Creates a deep copy from a given {@link InventoryLayoutImpl}.
     *
     * @param layout the layout to copy
     */
    InventoryLayoutImpl(InventoryLayoutImpl layout) {
        this.contents = new ISlot[layout.getContents().length];
        for (int i = 0; i < layout.getContents().length; i++) {
            ISlot slotEntry = layout.getContents()[i];
            switch (slotEntry) {
                case InventorySlot inventorySlot -> this.contents[i] = InventorySlot.of(inventorySlot);
                case TranslatedSlot translatedSlot -> this.contents[i] = TranslatedSlot.of(translatedSlot);
                default -> LOGGER.info("Slot: " + slotEntry + "is unknown and can't be converted");
            }
        }
        this.applyLayoutFunction = layout.getApplyLayoutFunction();
    }

    /**
     * Set a new reference from an implementation of the {@link ApplyLayoutFunction} functional interface.
     *
     * @param applyLayoutFunction the new implementation to set
     */
    @Override
    public void setApplyLayoutFunction(ApplyLayoutFunction applyLayoutFunction) {
        this.applyLayoutFunction = applyLayoutFunction;
    }

    /**
     * Applies a given {@link ItemStack} array to the layout.
     *
     * @param itemStacks the array that should be applied
     */
    @Override
    public void applyLayout(ItemStack[] itemStacks) {
        this.applyLayoutFunction.applyLayout(itemStacks, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyLayout(ItemStack[] itemStacks, @Nullable Locale locale) {
        this.applyLayoutFunction.applyLayout(itemStacks, locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl setItem(int slot, ItemStack.Builder itemBuilder, @Nullable InventoryClick clickEvent) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        this.contents[slot] = new InventorySlot(itemBuilder, clickEvent);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl setItem(int slot, ItemStack itemStack, @Nullable InventoryClick clickEvent) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        this.contents[slot] = new InventorySlot(itemStack, clickEvent);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl setItem(int slot, ISlot iSlot, InventoryClick clickEvent) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        iSlot.setClick(clickEvent);
        contents[slot] = iSlot;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl setItem(int slot, ISlot slotItem) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        contents[slot] = slotItem;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl blank(int slot) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        this.contents[slot] = BLANK_SLOT;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl clear(int slot) {
        Check.argCondition(slot < 0 || slot > contents.length, INDEX_ERROR);
        contents[slot] = BLANK_SLOT;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl update(int index, @Nullable InventoryClick listener) {
        Check.argCondition(index < 0 || index > contents.length, INDEX_ERROR);
        contents[index].setClick(listener == null ? CANCEL_CLICK : listener);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl update(int index, @Nullable ItemStack stack) {
        Check.argCondition(index < 0 || index > contents.length, INDEX_ERROR);
        contents[index].setItemStack(stack);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayoutImpl update(int index, ItemStack stack, @Nullable InventoryClick click) {
        Check.argCondition(index < 0 || index > contents.length, INDEX_ERROR);
        var slot = contents[index];
        slot.setItemStack(stack);
        slot.setClick(click == null ? CANCEL_CLICK : click);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InventoryLayout remove(int index) {
        Check.argCondition(index < 0 || index > this.contents.length,
                "The given index does not fit into the array (0, " + this.contents.length + ")");
        this.contents[index] = BLANK_SLOT;
        return this;
    }

    /**
     * Returns the array which contains all valid {@link ISlot}.
     *
     * @return the underlying array
     */
    @Override
    public ISlot[] getContents() {
        return contents;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ApplyLayoutFunction getApplyLayoutFunction() {
        return applyLayoutFunction;
    }

    /**
     * Returns a textual representation from the layout class.
     *
     * @return the textual representation
     */
    @Override
    public String toString() {
        return "InventoryLayout{" + "contents=" + Arrays.toString(contents) + '}';
    }

    /**
     * Checks if two layouts are equals.
     *
     * @param o the object to check
     * @return True when they are equal otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryLayoutImpl that)) return false;
        return Arrays.equals(contents, that.contents);
    }

    /**
     * Returns a hash value as int from the content which is in the layout array.
     *
     * @return the determined value
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(contents);
    }
}
