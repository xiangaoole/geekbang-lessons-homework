package org.geektimes.cache.redis.codec;

import io.lettuce.core.codec.RedisCodec;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class MyRedisCodec<K extends Serializable, V extends Serializable> implements RedisCodec<K, V> {

    private final Codec<K> keyCodec;

    private final Codec<V> valueCodec;

    public MyRedisCodec(Class<?> keyClazz, Class<?> valueClazz) {
        keyCodec = DefaultCodecs.resolveCodec(keyClazz);
        valueCodec = DefaultCodecs.resolveCodec(valueClazz);
    }

    @Override
    public K decodeKey(ByteBuffer bytes) {
        return keyCodec.decode(bytes);
    }

    @Override
    public V decodeValue(ByteBuffer bytes) {
        return valueCodec.decode(bytes);
    }

    @Override
    public ByteBuffer encodeKey(K key) {

        return keyCodec.encode(key);
    }

    @Override
    public ByteBuffer encodeValue(V value) {
        return valueCodec.encode(value);
    }
}
