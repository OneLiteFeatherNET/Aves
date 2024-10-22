package de.icevizion.aves.inventory.pageable;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TitleDataTest {

    @Test
    void testTitleDataBuilder() {
        TitleData.Builder builder = TitleData.builder();

        assertNotNull(builder);

        builder.showPageNumbers(true).pageMapper((integer, integer2) -> {
            throw new RuntimeException("Works");
        }).title(Component.text("Test"));

        TitleData data = builder.build();

        assertNotNull(data);
        assertTrue(data.showPageNumbers());
        assertNotNull(data.pageMapper());

        assertThrowsExactly(
                RuntimeException.class,
                () -> data.pageMapper().apply(1, 1),
                "Works"
        );

        assertEquals("Test", PlainTextComponentSerializer.plainText().serialize(data.title()));
    }

}