package me.johnniang.umami.cache;

import java.time.Duration;
import java.util.Optional;

/**
 * Cache interface.
 *
 * @author johnniang
 */
public interface Cache {

    void put(String key, Object value, Duration timeout);

    <T> Optional<T> get(String key, Class<T> requiredType);

    void revoke(String key);

}
