package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.locks.ReentrantLock;

///
/// Key-based locking using instances of [ReentrantLock].
///
/// Instances can be created using [Locks#reentrant()], [Locks#reentrant(Class)], [Locks#reentrant(boolean)] and [Locks#reentrant(boolean, Class)].
///
/// @param <K> the key type.
///
public final class ReentrantLocks<K extends @Nullable Object> extends LocksImpl<K, ReentrantLock> {

    private final boolean fair;

    ReentrantLocks() {
        this(false);
    }

    ReentrantLocks(boolean fair) {
        super(() -> new ReentrantLock(fair));
        this.fair = fair;
    }

    ///
    /// Return `true` if locks returned by this instance have fairness set true.
    ///
    /// @return `true` if locks returned by this instance have fairness set true.
    /// @since 1.3.3
    ///
    public boolean isFair() {
        return fair;
    }

}
