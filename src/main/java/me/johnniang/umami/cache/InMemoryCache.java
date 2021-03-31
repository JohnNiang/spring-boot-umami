package me.johnniang.umami.cache;

import org.springframework.util.Assert;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory cache.
 *
 * @author johnniang
 */
public class InMemoryCache implements Cache {

    private final Map<String, ExpiredWrapper> cache;

    public InMemoryCache() {
        cache = new ConcurrentHashMap<>();
    }

    @Override
    public void put(String key, Object value, Duration timeout) {
        Assert.notNull(key, "Cache key must not be null");
        Assert.notNull(value, "Cache value must not be null");
        if (timeout != null && timeout.isZero()) {
            // do nothing
            return;
        }
        if (timeout == null || timeout.isNegative()) {
            // default timeout is 356 days
            timeout = Duration.ofDays(365);
        }
        cache.put(key, new ExpiredWrapper(value, timeout));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key, Class<T> requiredType) {
        ExpiredWrapper expiredWrapper = cache.get(key);
        if (expiredWrapper == null) {
            return Optional.empty();
        }
        if (expiredWrapper.isExpired()) {
            // remove it
            cache.remove(key);
            return Optional.empty();
        }
        return Optional.of((T) expiredWrapper.getValue());
    }

    @Override
    public void revoke(String key) {
        cache.remove(key);
    }

    static class ExpiredWrapper {

        private final Object value;

        private final Instant expiryTime;

        ExpiredWrapper(Object value, Duration timeout) {
            this.value = value;
            expiryTime = Instant.now().plus(timeout);
        }

        Object getValue() {
            return value;
        }

        public boolean isExpired() {
            return expiryTime.isBefore(Instant.now());
        }
    }
}
