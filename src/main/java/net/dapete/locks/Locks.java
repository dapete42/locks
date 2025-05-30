package net.dapete.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Key-based locking with implementations of {@link Lock}.
 *
 * @param <K> type of key
 * @param <L> type of {@link Lock}
 */
public class Locks<K, L extends Lock> extends AbstractLocks<K, L> {

    Locks(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    /**
     * Return an instance using {@link Lock} implementations created by the specified {@code lockSupplier}.
     *
     * @param lockSupplier Supplier for instances of {@link L} (usually the constructor of a class implementing {@link Lock})
     * @param <K>          type of key
     * @param <L>          type of {@link Lock}
     * @return instance using {@code Lock} implementations created by the specified {@code lockSupplier}
     */
    public static <K, L extends Lock> Locks<K, L> withSupplier(Supplier<L> lockSupplier) {
        return new Locks<>(lockSupplier);
    }

    /**
     * Return a {@link ReentrantLocks} instance using {@link ReentrantLock}.
     *
     * @param <K> type of key
     * @return {@code ReentrantLocks} instance
     */
    public static <K> ReentrantLocks<K> reentrant() {
        return new ReentrantLocks<>();
    }

    /**
     * Return a {@link ReentrantLocks} instance using {@link ReentrantLock}.
     *
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@code ReentrantLocks} instance
     */
    public static <K> ReentrantLocks<K> reentrant(@SuppressWarnings("unused") Class<K> keyClass) {
        return new ReentrantLocks<>();
    }

    /**
     * Return a {@link ReentrantLocks} instance using {@link ReentrantLock} with the given fairness policy.
     *
     * @param fair {@code true} if the locks should use a fair ordering policy (see {@link ReentrantLock#ReentrantLock(boolean)})
     * @param <K>  type of key
     * @return {@code ReentrantLocks} instance
     * @since 1.2.0
     */
    public static <K> ReentrantLocks<K> reentrant(boolean fair) {
        return new ReentrantLocks<>(fair);
    }

    /**
     * Return a {@link ReentrantLocks} instance using {@link ReentrantLock} with the given fairness policy.
     *
     * @param fair     {@code true} if the locks should use a fair ordering policy (see {@link ReentrantLock#ReentrantLock(boolean)})
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@code ReentrantLocks} instance
     * @since 1.2.0
     */
    public static <K> ReentrantLocks<K> reentrant(boolean fair, @SuppressWarnings("unused") Class<K> keyClass) {
        return new ReentrantLocks<>(fair);
    }

    /**
     * Return a {@code Lock} already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already locked lock
     */
    public L lock(K key) {
        final var lock = get(key);
        lock.lock();
        return lock;
    }

}
