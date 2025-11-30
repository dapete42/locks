package net.dapete.locks;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class LocksImplTest {

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
                assertSame(lock, lock2);
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
    void lock() {
        final var locks = Locks.reentrant(Integer.class);

        final var lock = locks.lock(1);

        assertTrue(lock.isLocked());

        lock.unlock();
    }

}
