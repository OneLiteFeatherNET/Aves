package net.theevilreaper.aves.util;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class FuturesTest {

    @Test
    void testThenRunOrTimeout() {
        var result = Futures.thenRunOrTimeout(new CompletableFuture<>(), Duration.ofMillis(19), t -> {});
        result.join();
        assertTrue(result.isDone());
    }
}