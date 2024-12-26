package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.coordinate.Pos;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static de.icevizion.aves.util.Components.*;
import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacyAmpersand;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for the Components utility class")
class ComponentsTest {

    private static final String TEST_BAR = "&b|&9|||||||||";
    private static final Component HEART_STRING = getHealthString(10);

    private static final Pos TEST_POS = new Pos(1, 2, 3, 40, -50);

    private static final Stream<Arguments> CONVERT_ARGUMENTS = Stream.of(
            Arguments.of(
                    legacyAmpersand().serialize(Components.convertPoint(Pos.ZERO)),
                    "[x: 0, y: 0, z: 0, yaw: 0.0, pitch: 0.0]"
            ),
            Arguments.of(
                    legacyAmpersand().serialize(Components.convertPoint(TEST_POS)),
                    "[x: 1, y: 2, z: 3, yaw: 40.0, pitch: -50.0]"
            ),
            Arguments.of(
                    legacyAmpersand().serialize(Components.convertPoint(TEST_POS,
                            JoinConfiguration.builder().separator(Component.text("; ")).lastSeparator(Component.text("; ")).build())),
                    "x: 1; y: 2; z: 3; yaw: 40.0; pitch: -50.0"
            ),
            Arguments.of(
                    legacyAmpersand().serialize(Components.convertPoint(TEST_POS,
                            JoinConfiguration.builder()
                                    .separator(Component.text("; "))
                                    .lastSeparator(Component.text("; "))
                                    .prefix(Component.text("+"))
                                    .suffix(Component.text("+"))
                    )),
                    "+x: 1; y: 2; z: 3; yaw: 40.0; pitch: -50.0+"
            )
    );

    private static @NotNull Stream<Arguments> getConvertArguments() {
        return CONVERT_ARGUMENTS;
    }

    @ParameterizedTest
    @MethodSource("getConvertArguments")
    void testConverterFunction(@NotNull String argument, @NotNull String expected) {
        assertEquals(expected, argument);
    }

    @Test
    void testProgressBar() {
        Component progressBar = Components.getProgressBar(1, 10, 10, "|", NamedTextColor.AQUA, NamedTextColor.BLUE);
        assertEquals(TEST_BAR, legacyAmpersand().serialize(progressBar));
    }

    @Test
    void testGetHealthString() {
        var component = getHealthString(20D);
        assertNotEquals(convertToString(HEART_STRING), convertToString(component));
    }

    @Test
    void testGetHealthStringWithVariables() {
        var component = getHealthString(10, NamedTextColor.AQUA, NamedTextColor.BLUE);
        assertNotEquals(convertToString(HEART_STRING), convertToString(component));
    }

    private @NotNull String convertToString(@NotNull Component component) {
        return legacyAmpersand().serialize(component);
    }
}