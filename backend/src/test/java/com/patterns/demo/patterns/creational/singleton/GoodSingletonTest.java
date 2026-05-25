package com.patterns.demo.patterns.creational.singleton;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

class GoodSingletonTest {

    @Test
    void returnsSameInstanceOnRepeatedCalls() {
        GoodSingleton.resetForDemo();
        GoodSingleton a = GoodSingleton.getInstance();
        GoodSingleton b = GoodSingleton.getInstance();
        assertThat(a).isSameAs(b);
    }

    @Test
    void createsExactlyOneInstanceUnderConcurrency() throws InterruptedException {
        GoodSingleton.resetForDemo();
        int threads = 16;
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);
        Set<Integer> hashes = Collections.synchronizedSet(new HashSet<>());

        for (int i = 0; i < threads; i++) {
            pool.submit(() -> {
                try {
                    start.await();
                    hashes.add(System.identityHashCode(GoodSingleton.getInstance()));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            });
        }

        start.countDown(); // отпускаем все потоки одновременно
        assertThat(done.await(5, TimeUnit.SECONDS)).isTrue();
        pool.shutdownNow();

        // корректный DCL+volatile singleton: ровно один экземпляр на все потоки
        assertThat(hashes).hasSize(1);
    }
}
