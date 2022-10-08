package de.icevizion.aves.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static de.icevizion.aves.util.Components.*;
import static org.junit.jupiter.api.Assertions.*;

class ComponentsTest {

    private final Component HEART_STRING = getHealthString(10);

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
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }
}