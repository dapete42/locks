package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

///
/// Key-based locking with implementations of [ReadWriteLock].
///
/// @param <K> the key type
/// @param <L> the `ReadWriteLock` type
///
public interface ReadWriteLocks<K extends @Nullable Object, L extends ReadWriteLock> {

    ///
    /// Return an instance using [ReadWriteLock] implementations created by the specified `lockSupplier`.
    ///
    /// @param lockSupplier a `Supplier` for instances of [L] (usually the constructor of a class implementing `ReadWriteLock`)
    /// @param <K>          the key type
    /// @param <L>          the `ReadWriteLock` type
    /// @return an instance using `ReadWriteLock` implementations created by the specified `lockSupplier`
    ///
    static <K, L extends ReadWriteLock> ReadWriteLocks<K, L> withSupplier(Supplier<L> lockSupplier) {
        return new ReadWriteLocksImpl<>(lockSupplier);
    }

    ///
    /// Return a [ReentrantReadWriteLocks] instance using [ReentrantReadWriteLock].
    ///
    /// @param <K> the key type.
    /// @return a `ReentrantReadWriteLocks` instance.
    ///
    static <K> ReentrantReadWriteLocks<K> reentrant() {
        return new ReentrantReadWriteLocks<>();
    }

    ///
    /// Return a [ReentrantReadWriteLocks] instance using [ReentrantReadWriteLock].
    ///
    /// @param keyClass the key type class.
    /// @param <K>      the key type.
    /// @return a `ReentrantReadWriteLocks` instance.
    ///
    static <K> ReentrantReadWriteLocks<K> reentrant(@SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant();
    }

    ///
    /// Return a [ReentrantReadWriteLocks] instance using [ReentrantReadWriteLock] with the given fairness policy.
    ///
    /// @param fair `true` if the locks should use a fair ordering policy (see [ReentrantReadWriteLock#ReentrantReadWriteLock(boolean)]).
    /// @param <K>  the key type.
    /// @return a `ReentrantReadWriteLocks` instance.
    /// @since 1.2.0
    ///
    static <K> ReentrantReadWriteLocks<K> reentrant(boolean fair) {
        return new ReentrantReadWriteLocks<>(fair);
    }

    ///
    /// Return a [ReentrantReadWriteLocks] instance using [ReentrantReadWriteLock] with the given fairness policy.
    ///
    /// @param fair     `true` if the locks should use a fair ordering policy (see [ReentrantReadWriteLock#ReentrantReadWriteLock(boolean)]).
    /// @param keyClass the key type class.
    /// @param <K>      the key type.
    /// @return a `ReentrantReadWriteLocks` instance.
    /// @since 1.2.0
    ///
    static <K> ReentrantReadWriteLocks<K> reentrant(boolean fair, @SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant(fair);
    }

    ///
    /// Return a lock for `key`. There will be at most one lock per key at any given time.
    ///
    /// @param key the key
    /// @return lock
    ///
    L get(@Nullable K key);

    ///
    /// Return a `ReadWriteLock` for `key` with its [readLock][ReadWriteLock#readLock()] already locked using [Lock#lock()].
    ///
    /// @param key the key
    /// @return a `ReadWriteLock` already read locked.
    ///
    L readLock(@Nullable K key);

    ///
    /// Return a [ReadWriteLock] for `key` with its [writeLock][ReadWriteLock#writeLock()] already locked using [Lock#lock()].
    ///
    /// @param key the key
    /// @return a `ReadWriteLock` already write locked.
    ///
    L writeLock(@Nullable K key);

    ///
    /// Return the current number of locks managed by this instance.
    ///
    /// @return the current number of locks managed by this instance.
    ///
    int size();

}
