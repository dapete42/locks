package net.dapete.locks;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Key-based locking with {@link ReentrantReadWriteLock}s.
 * <p>
 * Instances can be created using {@link ReadWriteLocks#reentrant()} and {@link ReadWriteLocks#reentrant(Class)}.
 *
 * @param <K> type of key
 */
public final class ReentrantReadWriteLocks<K> extends ReadWriteLocksImpl<K, ReentrantReadWriteLock> {

    ReentrantReadWriteLocks() {
        super(ReentrantReadWriteLock::new);
    }

    ReentrantReadWriteLocks(boolean fair) {
        super(() -> new ReentrantReadWriteLock(fair));
    }

}
