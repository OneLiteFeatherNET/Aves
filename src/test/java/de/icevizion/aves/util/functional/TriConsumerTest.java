package de.icevizion.aves.util.functional;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author theEvilReaper
 * @version 1.0.0
 * @since 1.0.0
 **/
class TriConsumerTest {

    @Test
    void testTriConsumer() {
        var consumer = new TriConsumer<Integer, Integer, Integer>() {
            @Override
            public void accept(Integer integer, Integer integer2, Integer integer3) {
                assertEquals( 12, integer + integer2 + integer3);
            }
        };

        consumer.accept(12, 0 ,0);
    }

}