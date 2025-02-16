package net.dapete.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class LocksTest {

    @Test
    void testLocksAreReleasedWhenUnused() throws InterruptedException {
        final var locks = Locks.reentrant(Integer.class);

        var lock = locks.lock(1);
        lock.unlock();

        int size = locks.size();
        assertEquals(1, size);

        /*
         * Wait up to 30 seconds for size to change after dereferencing the lock. There is no way to force the garbage collector to run, System.gc() is just a
         * suggestion, but this seems to work.
         */
        lock = null;
        for (int i = 300; i > 0 && size > 0; i--) {
            System.gc();
            Thread.sleep(100);
            size = locks.size();
        }
        assertEquals(0, size);
    }

    @Test
    void testLocking() throws InterruptedException {
        final var locks = Locks.reentrant(Integer.class);

        var lock = locks.lock(1);

        // lock the lock in another thread and wait until it's running
        final AtomicBoolean threadHasStarted = new AtomicBoolean(false);
        final AtomicBoolean threadHasLocked = new AtomicBoolean(false);
        Runnable runnable = () -> {
            threadHasStarted.set(true);
            final var lock2 = locks.lock(1);
            try {
                threadHasLocked.set(true);
            } finally {
                lock2.unlock();
            }
        };
        new Thread(runnable).start();
        while (!threadHasStarted.get()) {
            Thread.yield();
        }
        TimeUnit.SECONDS.sleep(1);

        assertFalse(threadHasLocked.get());

        lock.unlock();
        TimeUnit.SECONDS.sleep(1);

        assertTrue(threadHasLocked.get());
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
