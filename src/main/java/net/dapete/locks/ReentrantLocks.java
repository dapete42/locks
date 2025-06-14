package net.dapete.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Key-based locking with {@link ReentrantLock}s.
 * <p>
 * Instances can be created using {@link Locks#reentrant()} and {@link Locks#reentrant(Class)}.
 *
 * @param <K> type of key
 */
 public final class ReentrantLocks<K> extends LocksImpl<K, ReentrantLock> {

    ReentrantLocks() {
        super(ReentrantLock::new);
    }

    ReentrantLocks(boolean fair) {
        super(() -> new ReentrantLock(fair));
    }

}
