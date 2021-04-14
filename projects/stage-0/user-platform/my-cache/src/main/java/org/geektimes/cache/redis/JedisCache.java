package org.geektimes.cache.redis;

import io.lettuce.core.codec.RedisCodec;
import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.ExpirableEntry;
import redis.clients.jedis.Jedis;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Set;

public class JedisCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final Jedis jedis;

    private final RedisCodec<K, V> codec;

    public JedisCache(CacheManager cacheManager, String cacheName,
                      Configuration<K, V> configuration, Jedis jedis,
                      RedisCodec<K, V> codec) {
        super(cacheManager, cacheName, configuration);
        this.jedis = jedis;
        this.codec = codec;
    }

    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serializeKey(key);
        return jedis.exists(keyBytes);
    }

    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serializeKey(key);
        return getEntry(keyBytes);
    }

    protected ExpirableEntry<K, V> getEntry(byte[] keyBytes) throws CacheException, ClassCastException {
        byte[] valueBytes = jedis.get(keyBytes);
        return ExpirableEntry.of(deserializeKey(keyBytes), deserializeValue(valueBytes));
    }

    @Override
    protected void putEntry(ExpirableEntry<K, V> entry) throws CacheException, ClassCastException {
        byte[] keyBytes = serializeKey(entry.getKey());
        byte[] valueBytes = serializeValue(entry.getValue());
        jedis.set(keyBytes, valueBytes);
    }

    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        byte[] keyBytes = serializeKey(key);
        ExpirableEntry<K, V> oldEntry = getEntry(keyBytes);
        jedis.del(keyBytes);
        return oldEntry;
    }

    @Override
    protected void clearEntries() throws CacheException {
        // TODO
    }


    @Override
    protected Set<K> keySet() {
        // TODO
        return null;
    }

    @Override
    protected void doClose() {
        this.jedis.close();
    }

    // encode and decode

    private byte[] serializeKey(K key) {
        return codec.encodeKey(key).array();
    }

    private byte[] serializeValue(V value) {
        return codec.encodeValue(value).array();
    }

    private K deserializeKey(byte[] bytes) {
        return codec.decodeKey(ByteBuffer.wrap(bytes));
    }

    private V deserializeValue(byte[] bytes) {
        return codec.decodeValue(ByteBuffer.wrap(bytes));
    }

}
