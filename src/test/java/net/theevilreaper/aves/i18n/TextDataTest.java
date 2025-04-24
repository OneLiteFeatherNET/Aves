package net.theevilreaper.aves.i18n;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextDataTest {

    private static final String KEY = "testKey";

    @Test
    void testTitleDataCreationViaComponents() {
        TextData data = TextData.of(KEY, Component.text("Test"));
        assertNotNull(data);
        assertEquals(KEY, data.key());
        assertEquals(1, data.args().length);

        Component firstComponent = data.args()[0];
        assertNotNull(firstComponent);
        assertEquals("Test", PlainTextComponentSerializer.plainText().serialize(firstComponent));
    }

    @Test
    void testTitleDataCreationViaStrings() {
        TextData textData = TextData.of(KEY, "Test");
        assertNotNull(textData);
        assertEquals(KEY, textData.key());
        assertEquals(1, textData.args().length);

        Component firstComponent = textData.args()[0];
        assertNotNull(firstComponent);
        assertEquals("Test", PlainTextComponentSerializer.plainText().serialize(firstComponent));
    }

    @Test
    void testTitleDataCreationWithEmptyContent() {
        TextData textData = TextData.of(KEY, "", "");
        assertNotNull(textData);
        assertEquals(KEY, textData.key());
        assertEquals(0, textData.args().length);
    }

    @Test
    void testEquals() {
        TextData textData = TextData.of(KEY, "");
        TextData anotherData = TextData.of(KEY, Component.text("Test"));
        assertEquals(textData, anotherData);
        assertNotEquals(anotherData, TextData.of("Test", "Test", "Test"));
    }

    @Test
    void testHashCode() {
        assertNotEquals(0, TextData.of(KEY, "").hashCode());
    }
}
