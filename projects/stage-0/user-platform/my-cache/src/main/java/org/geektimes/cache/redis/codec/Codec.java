package org.geektimes.cache.redis.codec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public interface Codec<T> {
    default String _decode(ByteBuffer bytes) {
        return StandardCharsets.UTF_8.decode(bytes).toString();
    }

    default ByteBuffer _encode(String value) {
        return ByteBuffer.wrap(value.getBytes(StandardCharsets.UTF_8));
    }

    T decode(ByteBuffer buffer);

    ByteBuffer encode(T value);
}
