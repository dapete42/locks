package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

///
/// Key-based locking with implementations of [Lock].
///
/// @param <K> the key type..
/// @param <L> the [Lock] type.
///
public class Locks<K extends @Nullable Object, L extends Lock> extends WeakKeyReferences<K, L> {

    Locks(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    ///
    /// Return an instance using [Lock] instances created by `lockSupplier`.
    ///
    /// @param lockSupplier a `Supplier` for instances of [L] (usually the constructor of a class implementing `Lock`).
    /// @param <K>          the key type.
    /// @param <L>          the `Lock` type.
    /// @return an instance using `Lock` instances created by `lockSupplier`
    ///
    public static <K, L extends Lock> Locks<K, L> withSupplier(Supplier<L> lockSupplier) {
        return new Locks<>(lockSupplier);
    }

    ///
    /// Return a [ReentrantLocks] instance using [ReentrantLock].
    ///
    /// @param <K> the key type.
    /// @return a `ReentrantLocks` instance.
    ///
    public static <K> ReentrantLocks<K> reentrant() {
        return new ReentrantLocks<>();
    }

    ///
    /// Return a [ReentrantLocks] instance using [ReentrantLock].
    ///
    /// @param keyClass the class for the key type.
    /// @param <K>      the key type.
    /// @return a `ReentrantLocks` instance.
    ///
    public static <K> ReentrantLocks<K> reentrant(@SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant();
    }

    ///
    /// Return a [ReentrantLocks] instance using [ReentrantLock] with the given fairness policy.
    ///
    /// @param fair `true` if the locks should use a fair ordering policy (see [ReentrantLock#ReentrantLock(boolean)]).
    /// @param <K>  the key type.
    /// @return a `ReentrantLocks` instance.
    /// @since 1.2.0
    ///
    public static <K> ReentrantLocks<K> reentrant(boolean fair) {
        return new ReentrantLocks<>(fair);
    }

    ///
    /// Return a [ReentrantLocks] instance using [ReentrantLock] with the given fairness policy.
    ///
    /// @param fair     `true` if the locks should use a fair ordering policy (see [ReentrantLock#(ReentrantLock(boolean)]).
    /// @param keyClass the class for the key type.
    /// @param <K>      the key type.
    /// @return a `ReentrantLocks` instance
    /// @since 1.2.0
    ///
    public static <K> ReentrantLocks<K> reentrant(boolean fair, @SuppressWarnings("unused") Class<K> keyClass) {
        return reentrant(fair);
    }

    ///
    /// Return a lock for `key`. There will be at most one lock per key at any given time.
    ///
    /// @param key the key
    /// @return a lock for `key`.
    ///
    @Override
    public L get(@Nullable K key) {
        return super.get(key);
    }

    ///
    /// Return a lock for `key` already locked using [Lock#lock()].
    ///
    /// @param key the key
    /// @return a lock for `key` already locked.
    ///
    public final L lock(@Nullable K key) {
        final var lock = get(key);
        lock.lock();
        return lock;
    }

    ///
    /// Return the current number of locks managed by this instance.
    ///
    /// @return the current number of locks managed by this instance.
    ///
    @Override
    public int size() {
        return super.size();
    }

}
