package me.johnniang.umami.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCacheTest {

    InMemoryCache cache;

    @BeforeEach
    void setUp() {
        cache = new InMemoryCache();
    }

    @Test
    void put() throws InterruptedException {
        cache.put("test", "value", Duration.ZERO);
        TimeUnit.MILLISECONDS.sleep(1100);
        assertTrue(cache.get("test", String.class).isEmpty());

        cache.put("test", "value", Duration.ofSeconds(1));
        assertEquals("value", cache.get("test", String.class).get());

        cache.revoke("test");
        assertTrue(cache.get("test", String.class).isEmpty());
    }

}