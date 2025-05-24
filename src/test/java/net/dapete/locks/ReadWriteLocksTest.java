package net.dapete.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class ReadWriteLocksTest {

    @Test
    void testReadLocksAreReleasedWhenUnused() {
        final var readWriteLocks = ReadWriteLocks.reentrant(Integer.class);

        readWriteLocks.readLock(1).readLock().unlock();

        assertEquals(1, readWriteLocks.size());

        /*
         * Wait up to 30 seconds for size to change after dereferencing the lock. There is no way to force the garbage collector to run, System.gc() is just a
         * suggestion, but this seems to work.
         */
        System.gc();
        await().atMost(30, TimeUnit.SECONDS).until(() -> readWriteLocks.size() == 0);
    }

    @Test
    void testLocking() {
        final var readWriteLocks = ReadWriteLocks.reentrant(Integer.class);

        final AtomicBoolean threadHasStarted = new AtomicBoolean(false);
        final AtomicBoolean threadHasLocked = new AtomicBoolean(false);

        final var readWriteLock = readWriteLocks.readLock(1);
        try {

            Runnable runnable = () -> {
                threadHasStarted.set(true);
                final var readWriteLock2 = readWriteLocks.writeLock(1);
                assertEquals(readWriteLock, readWriteLock2);
                try {
                    threadHasLocked.set(true);
                } finally {
                    readWriteLock2.writeLock().unlock();
                }
            };
            new Thread(runnable).start();
            await().atMost(10, TimeUnit.SECONDS).untilTrue(threadHasStarted);
            assertFalse(threadHasLocked.get());

        } finally {
            readWriteLock.readLock().unlock();
        }

        await().atMost(10, TimeUnit.SECONDS).untilTrue(threadHasLocked);
    }

    @Test
    void reentrant_fair() {
        final var locks = ReadWriteLocks.reentrant(true, Integer.class);

        final var lock = locks.readLock(1);
        try {
            assertTrue(lock.isFair());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Test
    void readLock() {
        final var locks = ReadWriteLocks.reentrant(Integer.class);

        final var lock = locks.readLock(1);

        assertEquals(1, lock.getReadLockCount());

        lock.readLock().unlock();
    }

    @Test
    void writeLock() {
        final var locks = ReadWriteLocks.reentrant(Integer.class);

        final var lock = locks.writeLock(1);

        assertTrue(lock.isWriteLocked());

        lock.writeLock().unlock();
    }

}
