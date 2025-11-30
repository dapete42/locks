package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

///
/// Key-based locking using instances of [ReentrantLock].
///
/// Instances can be created using [Locks#reentrant()], [Locks#reentrant(Class)], [Locks#reentrant(boolean)] and [Locks#reentrant(boolean,Class)].
///
/// @param <K> type of key
///
public final class ReentrantLocks<K extends @Nullable Object> extends LocksImpl<K, ReentrantLock> {

    ReentrantLocks() {
        super(ReentrantLock::new);
    }

    ReentrantLocks(boolean fair) {
        super(() -> new ReentrantLock(fair));
    }

}
