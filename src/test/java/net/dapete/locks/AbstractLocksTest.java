package net.dapete.locks;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

class AbstractLocksTest {

    private static class TestAbstractLocks extends AbstractLocks<@Nullable Integer, @NonNull ReentrantLock> {

        private TestAbstractLocks() {
            super(ReentrantLock::new);
        }

    }

    private static MethodHandle getLockReferenceMethodHandle;
    private static VarHandle lockReferenceQueueVarHandle;
    private static MethodHandle processQueueMethodHandle;

    @BeforeAll
    static void beforeAll() throws IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        final var lookup = MethodHandles.lookup();
        final var privateLookup = MethodHandles.privateLookupIn(AbstractLocks.class, lookup);

        final var getLockReferenceMethod = AbstractLocks.class.getDeclaredMethod("getLockReference", Object.class);
        getLockReferenceMethod.setAccessible(true);
        getLockReferenceMethodHandle = lookup.unreflect(getLockReferenceMethod);

        lockReferenceQueueVarHandle = privateLookup.findVarHandle(AbstractLocks.class, "lockReferenceQueue", ReferenceQueue.class);

        final var processQueueMethod = AbstractLocks.class.getDeclaredMethod("processQueue");
        processQueueMethod.setAccessible(true);
        processQueueMethodHandle = lookup.unreflect(processQueueMethod);
    }

    private void clearLockReference(AbstractLocks<?, ?> locks, Integer key) throws Throwable {
        final var lockReference = getLockReferenceMethodHandle.invoke(locks, key);
        ((LockReference<?, ?>) lockReference).clear();
    }

    private static ReferenceQueue<ReentrantLock> getLockReferenceQueue(TestAbstractLocks locks) {
        return (ReferenceQueue<ReentrantLock>) lockReferenceQueueVarHandle.get(locks);
    }

    @Test
    void testLocksAreReleasedWhenUnused() {
        final var locks = new TestAbstractLocks();

        locks.get(1);

        assertEquals(1, locks.size());

        /*
         * Wait up to 30 seconds for the size to change after dereferencing the lock. There is no way to force the garbage collector to run, System.gc() is just
         * a suggestion, but this seems to work.
         */
        System.gc();
        await().atMost(30, TimeUnit.SECONDS).until(() -> locks.size() == 0);
    }

    @Test
    void get_null() {
        final var locks = new TestAbstractLocks();

        final var lock = locks.get(null);

        assertNotNull(lock);
    }

    @Test
    void get_createNewLockIfLockReferenceIsNull() throws Throwable {
        final var locks = new TestAbstractLocks();

        // get one lock and then clear the reference to it
        final var lock1 = locks.get(1);
        clearLockReference(locks, 1);

        // this should not be null, but a different lock
        final var lock2 = locks.get(1);

        assertNotNull(lock2);
        assertNotSame(lock1, lock2);
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
    void processQueue_ignoresNonLockReference() throws Throwable {
        final var locks = new TestAbstractLocks();
        final var queue = getLockReferenceQueue(locks);
        final var nonLockReference = new WeakReference<>(new ReentrantLock(), queue);
        nonLockReference.enqueue();

        processQueueMethodHandle.invoke(locks);

        assertNull(queue.poll());
    }

    @Test
    void size() {
        final var locks = new TestAbstractLocks();

        // Initially, the size should be 0
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
