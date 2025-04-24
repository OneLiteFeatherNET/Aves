package net.theevilreaper.aves.util.collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProbabilityCollectionTest {

    ProbabilityCollection<Integer> collection;

    @BeforeAll
    void init() {
        this.collection = new ProbabilityCollection<>();
    }

    @Order(1)
    @Test
    void testSizeAndIfEmpty() {
        assertSame(0, this.collection.size());
        assertTrue(this.collection.isEmpty());
    }

    @Order(2)
    @Test
    void testContains() {
        this.collection.add(12, 1);
        assertFalse(this.collection.contains(3));
    }

    @Order(3)
    @Test
    void testContainsFail() {
        assertThrowsExactly(IllegalArgumentException.class, () -> this.collection.contains(null), "Cannot check if null object is contained in this collection");
    }

    @Order(4)
    @Test
    void testIterator() {
        assertNotNull(this.collection.iterator());
    }

    @Order(5)
    @Test
    void testAddWithInvalidValues() {
        assertThrowsExactly(IllegalArgumentException.class, () -> this.collection.add(null, 0), "Cannot add null object");
        assertThrowsExactly(IllegalArgumentException.class, () -> this.collection.add(11, 0), "Probability must be greater than 0");
    }

    @Order(6)
    @Test
    void testRemoveWithInvalidValues() {
        assertThrowsExactly(IllegalArgumentException.class, () -> this.collection.remove(null), "Cannot add null object");
    }

    @Order(7)
    @Test
    void testRemove() {
        assertTrue(this.collection.remove(12));
    }

    @Order(8)
    @Test
    void testClear() {
        this.collection.clear();
        assertSame(0, this.collection.size());
    }

    @Order(9)
    @Test
    void testGetObject() {
        assertThrowsExactly(IllegalStateException.class, collection::get, "Cannot get an object out of a empty collection");
    }

    @Order(10)
    @Test
    void testTotalProbability() {
        assertSame(0, this.collection.getTotalProbability());
    }
}