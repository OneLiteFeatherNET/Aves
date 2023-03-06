package de.icevizion.aves.inventory.pageable;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageActionTest {

    @Test
    void testUpdate() {
        assertEquals("UPDATE", PageAction.UPDATE.name());
    }

    @Test
    void testForward() {
        assertEquals("FORWARD", PageAction.FORWARD.name());
    }

    @Test
    void testBackwards() {
        assertEquals("BACKWARDS", PageAction.BACKWARDS.name());
    }

}