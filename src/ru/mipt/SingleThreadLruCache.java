package ru.mipt;

import java.util.LinkedHashMap;
import java.util.Map;

public class SingleThreadLruCache<K,V> implements LruCache<K,V> {
    private final int cacheSize;
    private final LinkedHashMap<K, V> hashMap;

    private SingleThreadLruCache() {
        this.cacheSize = 0;
        this.hashMap = new LinkedHashMap<>();
    }

    public SingleThreadLruCache(int size) {
        this.cacheSize = size;
        this.hashMap = new LinkedHashMap<K,V>(cacheSize, .75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
                return this.size() > cacheSize;
            }
        };
    }

    @Override
    public V get(K key) {
        return hashMap.getOrDefault(key, null);
    }

    @Override
    public void put(K key, V value) {
        hashMap.put(key, value);
    }
}
