package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Abstract base implementation of key-based locking.
 *
 * @param <K> type of key
 * @param <L> type of lock
 */
abstract class AbstractLocks<K, L> {

    private final Lock instanceLock = new ReentrantLock();

    private final Map<Object, LockReference<K, L>> lockReferenceMap = new HashMap<>();

    private final ReferenceQueue<L> lockReferenceQueue = new ReferenceQueue<>();

    private final Supplier<L> lockSupplier;

    protected AbstractLocks(Supplier<L> lockSupplier) {
        this.lockSupplier = lockSupplier;
    }

    private L createLock(K key) {
        final var newLock = lockSupplier.get();
        lockReferenceMap.put(key, new LockReference<>(key, newLock, lockReferenceQueue));
        return newLock;
    }

    /**
     * Returns a lock for the supplied key. There will be at most one lock per key at any given time.
     *
     * @param key key
     * @return lock
     */
    public L get(K key) {
        processQueue();
        instanceLock.lock();
        try {
            final var lockReference = getLockReference(key);
            if (lockReference != null) {
                final L lock = lockReference.get();
                if (lock != null) {
                    return lock;
                }
            }
            return createLock(key);
        } finally {
            instanceLock.unlock();
        }
    }

    // package-private to allow accessing this in tests
    @Nullable
    LockReference<K, L> getLockReference(K key) {
        return lockReferenceMap.get(key);
    }

    /**
     * Returns the current number of locks managed by this instance.
     *
     * @return number of locks
     */
    public int size() {
        processQueue();
        instanceLock.lock();
        try {
            return lockReferenceMap.size();
        } finally {
            instanceLock.unlock();
        }
    }

    /**
     * Removes all locks that have been marked as unreachable by the garbage collector.
     */
    private void processQueue() {
        instanceLock.lock();
        try {
            LockReference<?, ?> lockReference;
            while ((lockReference = (LockReference<?, ?>) lockReferenceQueue.poll()) != null) {
                lockReferenceMap.remove(lockReference.getKey());
            }
        } finally {
            instanceLock.unlock();
        }
    }

}
