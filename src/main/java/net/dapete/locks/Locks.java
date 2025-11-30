package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

///
/// Key-based locking with implementations of [Lock].
///
/// @param <K> type of key
/// @param <L> type of [Lock]
///
public interface Locks<K extends @Nullable Object, L extends Lock> {

    ///
    /// Return an instance using [Lock] implementations created by the specified `lockSupplier`.
    ///
    /// @param lockSupplier Supplier for instances of [L] (usually the constructor of a class implementing [Lock])
    /// @param <K>          type of key
    /// @param <L>          type of [Lock]
    /// @return instance using [Lock] implementations created by the specified `lockSupplier`
    ///
    static <K, L extends Lock> Locks<K, L> withSupplier(Supplier<L> lockSupplier) {
        return new LocksImpl<>(lockSupplier);
    }

    ///
    /// Return a [ReentrantLocks] instance using [ReentrantLock].
    ///
    /// @param <K> type of key
    /// @return [ReentrantLocks] instance
    ///
    static <K> ReentrantLocks<K> reentrant() {
        return new ReentrantLocks<>();
    }

    ///
    /// Return a [ReentrantLocks] instance using [ReentrantLock].
    ///
    /// @param keyClass class of key
    /// @param <K>      type of key
    /// @return [ReentrantLocks] instance
    ///
    static <K> ReentrantLocks<K> reentrant(@SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant();
    }

    ///
    /// Return a [ReentrantLocks] instance using [ReentrantLock] with the given fairness policy.
    ///
    /// @param fair `true` if the locks should use a fair ordering policy (see [ReentrantLock#ReentrantLock(boolean)])
    /// @param <K>  type of key
    /// @return [ReentrantLocks] instance
    /// @since 1.2.0
    ///
    static <K> ReentrantLocks<K> reentrant(boolean fair) {
        return new ReentrantLocks<>(fair);
    }

    ///
    /// Return a [ReentrantLocks] instance using [ReentrantLock] with the given fairness policy.
    ///
    /// @param fair     `true` if the locks should use a fair ordering policy (see [ReentrantLock#ReentrantLock(boolean)])
    /// @param keyClass class of key
    /// @param <K>      type of key
    /// @return [ReentrantLocks] instance
    /// @since 1.2.0
    ///
    static <K> ReentrantLocks<K> reentrant(boolean fair, @SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant(fair);
    }

    ///
    /// Returns a lock for the supplied key. There will be at most one lock per key at any given time.
    ///
    /// @param key key
    /// @return lock
    ///
    L get(@Nullable K key);

    ///
    /// Return a lock already locked using [Lock#lock()].
    ///
    /// @param key key
    /// @return already locked lock
    ///
    L lock(@Nullable K key);

    ///
    /// Returns the current number of locks managed by this instance.
    ///
    /// @return number of locks
    ///
    int size();

}
