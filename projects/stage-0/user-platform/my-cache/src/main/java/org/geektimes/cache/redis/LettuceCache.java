package org.geektimes.cache.redis;

import io.lettuce.core.api.sync.RedisCommands;
import org.geektimes.cache.AbstractCache;
import org.geektimes.cache.ExpirableEntry;

import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.Configuration;
import java.io.Serializable;
import java.util.Set;

public class LettuceCache<K extends Serializable, V extends Serializable> extends AbstractCache<K, V> {

    private final RedisCommands<K, V> syncCommands;

    public LettuceCache(CacheManager cacheManager, String cacheName, Configuration<K, V>  configuration,
                        RedisCommands<K, V> syncCommands) {
        super(cacheManager, cacheName, configuration);
        this.syncCommands = syncCommands;
    }

    @Override
    protected boolean containsEntry(K key) throws CacheException, ClassCastException {
        return syncCommands.get(key) != null;
    }

    @Override
    protected ExpirableEntry<K, V> getEntry(K key) throws CacheException, ClassCastException {
        return ExpirableEntry.of(key, syncCommands.get(key));
    }

    @Override
    protected void putEntry(ExpirableEntry<K, V> entry) throws CacheException, ClassCastException {
        syncCommands.set(entry.getKey(), entry.getValue());
    }

    @Override
    protected ExpirableEntry<K, V> removeEntry(K key) throws CacheException, ClassCastException {
        ExpirableEntry<K, V> oldEntry = getEntry(key);
        syncCommands.del(key);
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
        syncCommands.shutdown(false);
    }
}
