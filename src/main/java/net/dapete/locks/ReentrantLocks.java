package net.dapete.locks;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Key-based locking with {@link ReentrantLock}s.
 * <p>
 * Instances can be created using {@link Locks#reentrant()} and {@link Locks#reentrant(Class)}.
 *
 * @param <K> type of key
 */
public class ReentrantLocks<K> extends Locks<K, ReentrantLock> {

    ReentrantLocks() {
        super(ReentrantLock::new);
    }

    ReentrantLocks(boolean fair) {
        super(() -> new ReentrantLock(fair));
    }

}
