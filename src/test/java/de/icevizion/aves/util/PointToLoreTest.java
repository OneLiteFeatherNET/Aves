package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.coordinate.Vec;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PointToLoreTest {

    private static DecimalFormat decimalFormat;

    @BeforeAll
    static void init() {
        decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
    }

    @Test
    void testInvalidPartArgument() {
        List<Component> partArguments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            partArguments.add(Component.text(i));
        }

        assertEquals(4, partArguments.size());
        IllegalArgumentException exception = assertThrowsExactly(
                IllegalArgumentException.class,
                () -> Components.pointToLore(Pos.ZERO, decimalFormat, partArguments)
                );

        assertEquals(
                """
                        The size of the argument parts doesn't match with the given point implementation.
                        Given point impl is Pos which requires 5 arguments and the size of the partArguments is %s
                        """.formatted(partArguments.size()),
                exception.getMessage()
        );
    }

    @Test
    void testPosToComponentWithParts() {
        List<Component> partArguments = new ArrayList<>();
        partArguments.add(Component.text("X: "));
        partArguments.add(Component.text("Y: "));
        partArguments.add(Component.text("Z: "));

        List<Component> posAsComponents = Components.pointToLore(Vec.ZERO, decimalFormat, partArguments);

        assertEquals(partArguments.size(), posAsComponents.size());
    }

    @Test
    void testPosToComponentConversion() {
        List<Component> posAsComponents = Components.pointToLore(MiniMessage.miniMessage(), Pos.ZERO, decimalFormat);
        assertNotNull(posAsComponents);
        assertFalse(posAsComponents.isEmpty());
        assertEquals(5, posAsComponents.size());

        for (Component posComponent : posAsComponents) {
            String text = PlainTextComponentSerializer.plainText().serialize(posComponent);
            assertTrue(text.contains("0"));
        }
    }
}
