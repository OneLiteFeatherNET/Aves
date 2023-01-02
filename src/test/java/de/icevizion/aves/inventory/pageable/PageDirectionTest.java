package de.icevizion.aves.inventory.pageable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageDirectionTest {

    @Test
    void testUpdate() {
        assertEquals("UPDATE", PageDirection.UPDATE.name());
    }

    @Test
    void testForward() {
        assertEquals("FORWARD", PageDirection.FORWARD.name());
    }

    @Test
    void testBackwards() {
        assertEquals("BACKWARDS", PageDirection.BACKWARDS.name());
    }

}