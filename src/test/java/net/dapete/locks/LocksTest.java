package net.dapete.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class LocksTest {

    @Test
    void testLocksAreReleasedWhenUnused() {
        final var locks = Locks.reentrant(Integer.class);

        locks.lock(1).unlock();

        assertEquals(1, locks.size());

        /*
         * Wait up to 30 seconds for size to change after dereferencing the lock. There is no way to force the garbage collector to run, System.gc() is just a
         * suggestion, but this seems to work.
         */
        System.gc();
        await().atMost(30, TimeUnit.SECONDS).until(() -> locks.size() == 0);
    }

    @Test
    void testLocking() {
        final var locks = Locks.reentrant(Integer.class);

        final AtomicBoolean threadHasStarted = new AtomicBoolean(false);
        final AtomicBoolean threadHasLocked = new AtomicBoolean(false);

        final var lock = locks.lock(1);
        try {

            // lock the lock in another thread and wait until it's running
            Runnable runnable = () -> {
                threadHasStarted.set(true);
                final var lock2 = locks.lock(1);
                assertEquals(lock, lock2);
                try {
                    threadHasLocked.set(true);
                } finally {
                    lock2.unlock();
                }
            };
            new Thread(runnable).start();
            await().atMost(10, TimeUnit.SECONDS).untilTrue(threadHasStarted);
            assertFalse(threadHasLocked.get());

        } finally {
            lock.unlock();
        }

        await().atMost(10, TimeUnit.SECONDS).untilTrue(threadHasLocked);
    }


    @Test
    void reentrant_fair() {
        final var locks = Locks.reentrant(true, Integer.class);

        final var lock = locks.lock(1);
        try {
            assertTrue(lock.isFair());
        } finally {
            lock.unlock();
        }
    }

    @Test
    void get_differentForDifferentKeys() {
        final var locks = Locks.reentrant(Integer.class);

        assertNotEquals(locks.get(1), locks.get(2));
    }

    @Test
    void get_identicalForIdenticalKey() {
        final var locks = Locks.reentrant(Integer.class);

        assertEquals(locks.get(1), locks.get(1));
    }

    @Test
    void lock() {
        final var locks = Locks.reentrant(Integer.class);

        final var lock = locks.lock(1);

        assertTrue(lock.isLocked());

        lock.unlock();
    }

}
