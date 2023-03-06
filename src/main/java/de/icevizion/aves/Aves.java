package de.icevizion.aves;

import de.icevizion.aves.inventory.InventoryLayout;
import de.icevizion.aves.inventory.InventorySlot;
import de.icevizion.aves.inventory.PersonalInventoryBuilder;
import de.icevizion.aves.inventory.pageable.PageableInventory;
import de.icevizion.aves.inventory.slot.ISlot;
import de.icevizion.aves.inventory.util.InventoryConstants;
import de.icevizion.aves.inventory.util.LayoutCalculator;
import net.kyori.adventure.text.Component;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.player.PlayerLoginEvent;
import net.minestom.server.event.player.PlayerSpawnEvent;
import net.minestom.server.extensions.Extension;
import net.minestom.server.inventory.InventoryType;
import net.minestom.server.item.ItemStack;
import net.minestom.server.item.Material;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public final class Aves extends Extension {

    private static final Logger AVES_LOGGER = LoggerFactory.getLogger(Aves.class);

    @Override
    public void initialize() {
        AVES_LOGGER.info("""

                    /\\                \s
                   /  \\__   _____  ___\s
                  / /\\ \\ \\ / / _ \\/ __|
                 / ____ \\ V /  __/\\__ \\
                /_/    \\_\\_/ \\___||___/""");
        AVES_LOGGER.info("Starting Aves....");

        ItemStack stack = ItemStack.builder(Material.ACACIA_BUTTON).build();



        var layout = new InventoryLayout(InventoryType.CHEST_3_ROW);

        var item =  ItemStack.of(Material.BLUE_STAINED_GLASS_PANE);
        layout.setItems(LayoutCalculator.frame(0, InventoryType.CHEST_3_ROW.getSize() - 1), item, InventoryConstants.CANCEL_CLICK);

        var list = new ArrayList<ISlot>();

        var slotRange = LayoutCalculator.repeat(InventoryType.CHEST_1_ROW.getSize() + 1, InventoryType.CHEST_2_ROW.getSize() - 1);

        for (int i = 0; i < 16; i++) {
            if (i > slotRange.length - 1) {
                list.add(new InventorySlot(stack.withMaterial(Material.STONE_BUTTON).withDisplayName(Component.text(i + 1)), InventoryConstants.CANCEL_CLICK));
            } else {
                if (i >= 14) {
                    list.add(new InventorySlot(stack.withMaterial(Material.DARK_OAK_BUTTON).withDisplayName(Component.text(i + 1)), InventoryConstants.CANCEL_CLICK));
                } else {
                    list.add(new InventorySlot(stack.withDisplayName(Component.text(i + 1)), InventoryConstants.CANCEL_CLICK));
                }
            }
        }

        System.out.println("List size is " + list.size());

        var inventory = PageableInventory.builder()
                .title(Component.text("Test"))
                .type(InventoryType.CHEST_3_ROW)
                .layout(layout)
                .slotRange(slotRange)
                .values(list)
                .build();

        MinecraftServer.getGlobalEventHandler().addListener(PlayerSpawnEvent.class, event ->
                MinecraftServer.getSchedulerManager().buildTask(() ->
                        inventory.open(event.getPlayer())).delay(Duration.of(1, ChronoUnit.SECONDS)).schedule());
    }

    @Override
    public void terminate() {
        // Nothing to do
    }
}
