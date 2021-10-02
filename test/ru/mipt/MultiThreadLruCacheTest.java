package ru.mipt;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.fail;

public class MultiThreadLruCacheTest {
    private static final int SIZE = 10;
    private static LruCache<Integer, Integer> cache;
    private static volatile boolean execute = true;
    public static class CacheThread implements Runnable {
        private final Random random = new Random();
        private int cnt = 0;
        @Override
        public void run() {
            long id = Thread.currentThread().getId();
            try {
                while (execute) {
                    int rnd = random.nextInt(SIZE * 2);
                    if (random.nextInt() % 2 == 0) {
                        cache.put(rnd, rnd);
                        if (cnt % 1000 == 0) {
                            System.out.printf("%d: put %d\n", id, rnd);
                        }
                    } else {
                        Integer result = cache.get(rnd);
                        if (cnt % 1000 == 0) {
                            System.out.printf("%d: get %d from %d\n", id, result, rnd);
                        }
                    }
                    cnt++;
                }
            } finally {
                System.out.println(id + ": done");
            }
        }
    }

    @Test
    public void checkNoDeadlocks() {
        cache = new MultiThreadLruCache<>(SIZE);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(new CacheThread()));
        }
        for (Thread t : threads) {
            t.start();
        }
        try {
            Thread.sleep(1000);
            execute = false;
        } catch (Exception e) {
            fail();
        }
    }

}
