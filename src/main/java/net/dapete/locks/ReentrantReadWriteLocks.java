package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.locks.ReentrantReadWriteLock;

///
/// Key-based locking using instances of [ReentrantReadWriteLock].
///
/// Instances can be created using [ReadWriteLocks#reentrant()], [ReadWriteLocks#reentrant(Class)], [ReadWriteLocks#reentrant(boolean)] and
/// [ReadWriteLocks#reentrant(boolean, Class)].
///
/// @param <K> the key type.
///
public final class ReentrantReadWriteLocks<K extends @Nullable Object> extends ReadWriteLocksImpl<K, ReentrantReadWriteLock> {

    private final boolean fair;

    ReentrantReadWriteLocks() {
        this(false);
    }

    ReentrantReadWriteLocks(boolean fair) {
        super(() -> new ReentrantReadWriteLock(fair));
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
