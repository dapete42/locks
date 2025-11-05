package net.dapete.locks;

import java.util.concurrent.locks.ReentrantReadWriteLock;

///
/// Key-based locking using instances of [ReentrantReadWriteLock].
///
/// Instances can be created using [ReadWriteLocks#reentrant()], [ReadWriteLocks#reentrant(Class)], [ReadWriteLocks#reentrant(boolean)] and
/// [ReadWriteLocks#reentrant(boolean, Class)].
///
/// @param <K> type of key
///
public final class ReentrantReadWriteLocks<K> extends ReadWriteLocksImpl<K, ReentrantReadWriteLock> {

    ReentrantReadWriteLocks() {
        super(ReentrantReadWriteLock::new);
    }

    ReentrantReadWriteLocks(boolean fair) {
        super(() -> new ReentrantReadWriteLock(fair));
    }

}
