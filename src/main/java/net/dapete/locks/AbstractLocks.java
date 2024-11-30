package net.dapete.locks;

import java.lang.ref.ReferenceQueue;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Abstract base implementation of key-based locking.
 *
 * @param <L> type of lock
 */
abstract class AbstractLocks<L> {

    private final Map<Object, LockReference<L>> lockReferenceMap = new ConcurrentHashMap<>();

    private final ReferenceQueue<L> lockReferenceQueue = new ReferenceQueue<>();

    private final Lock lockReferenceQueueLock = new ReentrantLock();

    private final Supplier<L> lockSupplier;

    protected AbstractLocks(Supplier<L> lockSupplier) {
        this.lockSupplier = lockSupplier;
    }

    /**
     * Returns a new lock for the supplied key.
     *
     * @param key key
     * @return lock
     */
    public L get(Object key) {
        processQueue();
        return lockReferenceMap.computeIfAbsent(key, this::newLockReference).get();
    }

    /**
     * Returns the current number of locks managed by this instance.
     *
     * @return number of locks
     */
    public int size() {
        processQueue();
        return lockReferenceMap.size();
    }

    private LockReference<L> newLockReference(Object key) {
        final var lock = lockSupplier.get();
        return new LockReference<>(key, lock, lockReferenceQueue);
    }

    /**
     * Removes all locks that have been marked as unreachable by the garbage collector.
     */
    private void processQueue() {
        lockReferenceQueueLock.lock();
        try {
            LockReference<?> lockReference;
            while ((lockReference = (LockReference<?>) lockReferenceQueue.poll()) != null) {
                lockReferenceMap.remove(lockReference.getKey());
            }
        } finally {
            lockReferenceQueueLock.unlock();
        }
    }

}