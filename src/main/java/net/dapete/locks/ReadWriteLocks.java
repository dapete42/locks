package net.dapete.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Key-based locking with implementations of {@link ReadWriteLock}.
 *
 * @param <K> type of key
 * @param <L> type of ReadWriteLock
 */
public class ReadWriteLocks<K, L extends ReadWriteLock> extends AbstractLocks<K, L> {

    ReadWriteLocks(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    /**
     * Return an instance using {@link ReadWriteLock} implementations created by the specified {@code lockSupplier}.
     *
     * @param lockSupplier Supplier for instances of {@link L} (usually the constructor of a class implementing {@link ReadWriteLock})
     * @param <K>          type of key
     * @param <L>          type of {@link Lock}
     * @return instance using {@code ReadWriteLock} implementations created by the specified {@code lockSupplier}
     */
    public static <K, L extends ReadWriteLock> ReadWriteLocks<K, L> withSupplier(Supplier<L> lockSupplier) {
        return new ReadWriteLocks<>(lockSupplier);
    }

    /**
     * Return a {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock}.
     *
     * @param <K> type of key
     * @return {@code ReentrantReadWriteLocks} instance
     */
    public static <K> ReentrantReadWriteLocks<K> reentrant() {
        return new ReentrantReadWriteLocks<>();
    }

    /**
     * Return a {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock}.
     *
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@code ReentrantReadWriteLocks} instance
     */
    public static <K> ReentrantReadWriteLocks<K> reentrant(@SuppressWarnings("unused") Class<K> keyClass) {
        return new ReentrantReadWriteLocks<>();
    }

    /**
     * Return a {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock} with the given fairness policy.
     *
     * @param fair {@code true} if the locks should use a fair ordering policy (see {@link ReentrantReadWriteLock#ReentrantReadWriteLock(boolean)})
     * @param <K>  type of key
     * @return {@code ReentrantReadWriteLocks} instance
     * @since 1.2.0
     */
    public static <K> ReentrantReadWriteLocks<K> reentrant(boolean fair) {
        return new ReentrantReadWriteLocks<>(fair);
    }

    /**
     * Return a {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock} with the given fairness policy.
     *
     * @param fair     {@code true} if the locks should use a fair ordering policy (see {@link ReentrantReadWriteLock#ReentrantReadWriteLock(boolean)})
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@code ReentrantReadWriteLocks} instance
     * @since 1.2.0
     */
    public static <K> ReentrantReadWriteLocks<K> reentrant(boolean fair, @SuppressWarnings("unused") Class<K> keyClass) {
        return new ReentrantReadWriteLocks<>(fair);
    }

    /**
     * Return a {@code ReadWriteLock} with its {@link ReadWriteLock#readLock()} already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already read locked lock
     */
    public L readLock(K key) {
        final var lock = get(key);
        lock.readLock().lock();
        return lock;
    }

    /**
     * Return a {@code ReadWriteLock} with its {@link ReadWriteLock#writeLock()} already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already write locked lock
     */
    public L writeLock(K key) {
        final var lock = get(key);
        lock.writeLock().lock();
        return lock;
    }

}
