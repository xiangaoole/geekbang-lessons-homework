package org.geektimes.cache.redis;

import io.lettuce.core.codec.RedisCodec;
import org.geektimes.cache.AbstractCacheManager;
import org.geektimes.cache.redis.codec.MyRedisCodec;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.cache.Cache;
import javax.cache.configuration.Configuration;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

/**
 * {@link javax.cache.CacheManager} based on Jedis
 */
public class JedisCacheManager extends AbstractCacheManager {

    private final JedisPool jedisPool;

    public JedisCacheManager(CachingProvider cachingProvider, URI uri, ClassLoader classLoader, Properties properties) {
        super(cachingProvider, uri, classLoader, properties);
        this.jedisPool = new JedisPool(uri);
    }

    @Override
    protected <K, V, C extends Configuration<K, V>> Cache doCreateCache(String cacheName, C configuration) {
        Jedis jedis = jedisPool.getResource();
        Class<K> keyType = configuration.getKeyType();
        Class<V> valueType = configuration.getValueType();
        RedisCodec codec = new MyRedisCodec(keyType, valueType);
        return new JedisCache(this, cacheName, configuration, jedis, codec);
    }

    @Override
    protected void doClose() {
        jedisPool.close();
    }
}
