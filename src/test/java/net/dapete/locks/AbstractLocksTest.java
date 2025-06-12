package net.dapete.locks;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class AbstractLocksTest {

    private static class TestAbstractLocks extends AbstractLocks<Integer, ReentrantLock> {
        private TestAbstractLocks() {
            super(ReentrantLock::new);
        }
    }

    @Test
    void testLocksAreReleasedWhenUnused() {
        final var locks = new TestAbstractLocks();

        locks.get(1);

        assertEquals(1, locks.size());

        /*
         * Wait up to 30 seconds for size to change after dereferencing the lock. There is no way to force the garbage collector to run, System.gc() is just a
         * suggestion, but this seems to work.
         */
        System.gc();
        await().atMost(30, TimeUnit.SECONDS).until(() -> locks.size() == 0);
    }

    @Test
    void get_differentForDifferentKeys() {
        final var locks = new TestAbstractLocks();

        assertNotSame(locks.get(1), locks.get(2));
    }

    @Test
    void get_identicalForIdenticalKey() {
        final var locks = new TestAbstractLocks();

        assertSame(locks.get(1), locks.get(1));
    }

    @Test
    void size() {
        final var locks = new TestAbstractLocks();

        // Initially the size should be 0
        assertEquals(0, locks.size(), "Initial size should be 0");

        // Add some locks
        final var lockList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lockList.add(locks.get(i));
        }

        // Size should reflect the number of locks
        assertEquals(5, locks.size(), "Size should match the number of locks");

        // Clear some references and force GC
        lockList.subList(0, 3).clear();
        System.gc();

        // Wait for size to decrease
        await().atMost(30, TimeUnit.SECONDS).until(() -> locks.size() == 2);

        // Clear remaining references
        lockList.clear();
        System.gc();

        // Wait for size to reach 0
        await().atMost(30, TimeUnit.SECONDS).until(() -> locks.size() == 0);
    }

}
