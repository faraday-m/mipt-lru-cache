package ru.mipt;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MultiThreadLruCache<K,V> implements LruCache<K,V> {
    private final int cacheSize;
    private final Map<K, V> hashMap;
    private Lock lock = new ReentrantLock();;

    private MultiThreadLruCache() {
        this.cacheSize = 0;
        this.hashMap = new LinkedHashMap<>();
    }

    public MultiThreadLruCache(int size) {
        this.cacheSize = size;
        this.hashMap = new LinkedHashMap<K,V>(cacheSize, .75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
                while (!lock.tryLock()) { }
                try {
                    boolean result = this.size() > cacheSize;
                    return result;
                } finally {
                    lock.unlock();
                }
            }
        };
    }

    @Override
    public V get(K key) {
        while (!lock.tryLock()) { }
        try {
            V result = hashMap.getOrDefault(key, null);
            return result;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        while (!lock.tryLock()) { }
        try {
            hashMap.put(key, value);
        } finally {
            lock.unlock();
        }
    }
}
