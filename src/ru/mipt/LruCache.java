package ru.mipt;

public interface LruCache<K,V> {
    V get(K key);
    void put(K key, V value);
}
