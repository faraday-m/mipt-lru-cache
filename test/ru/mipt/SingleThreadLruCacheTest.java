package ru.mipt;

import org.junit.Assert;
import org.junit.Test;

public class SingleThreadLruCacheTest {
    private LruCache<Integer, Integer> cache;

    @Test
    public void whenLessKeysThanCapacity_thenCanGetAll() {
        cache = new SingleThreadLruCache<>(4);
        cache.put(1, 1);
        cache.put(2, 4);
        cache.put(2, 8);
        cache.put(2, 16);
        cache.put(2, 32);
        Assert.assertEquals(Integer.valueOf(1), cache.get(1));
        Assert.assertEquals(Integer.valueOf(32), cache.get(2));
    }

    @Test
    public void whenMoreKeysThanCapacity_thenLostFirst() {
        cache = new SingleThreadLruCache<>(4);
        cache.put(1, 1);
        cache.put(2, 4);
        cache.put(3, 9);
        cache.put(4, 16);
        cache.put(2, 6);
        cache.put(5, 25);
        Assert.assertNull(cache.get(1));
        Assert.assertEquals(Integer.valueOf(6), cache.get(2));
        Assert.assertEquals(Integer.valueOf(9), cache.get(3));
        Assert.assertEquals(Integer.valueOf(16), cache.get(4));
        Assert.assertEquals(Integer.valueOf(25), cache.get(5));
    }
}
