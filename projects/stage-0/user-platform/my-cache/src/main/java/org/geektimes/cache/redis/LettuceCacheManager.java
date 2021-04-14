package org.geektimes.cache.redis;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.geektimes.cache.AbstractCacheManager;
import org.geektimes.cache.redis.codec.MyRedisCodec;

import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

public class LettuceCacheManager extends AbstractCacheManager {
    RedisClient redisClient;
    StatefulRedisConnection connection;

    public LettuceCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);
        redisClient = RedisClient.create(uri.toString());
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> Cache<K, V> doCreateCache(String cacheName, C configuration) {
        Class<K> keyType = configuration.getKeyType();
        Class<V> valueType = configuration.getValueType();
        connection = redisClient.connect(new MyRedisCodec(keyType, valueType));
        RedisCommands<K, V> syncCommands = connection.sync();
        return new LettuceCache(this, cacheName, configuration, syncCommands);
    }

    @Override
    protected void doClose() {
        connection.close();
        redisClient.shutdown();
    }
}
