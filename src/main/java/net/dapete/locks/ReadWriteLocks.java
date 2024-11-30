package net.dapete.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Key-based locking with implementations of {@link ReadWriteLock}.
 *
 * @param <L> type of ReadWriteLock
 */
public class ReadWriteLocks<L extends ReadWriteLock> extends AbstractLocks<L> {

    ReadWriteLocks(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    /**
     * @param lockSupplier Supplier for instances of {@link L} (usually the constructor of a class implementing {@link ReadWriteLock})
     * @param <L>          type of {@link Lock}
     * @return instance using {@link ReadWriteLock} implementations created by the specified {@code lockSupplier}
     */
    public static <L extends ReadWriteLock> ReadWriteLocks<L> withSupplier(Supplier<L> lockSupplier) {
        return new ReadWriteLocks<>(lockSupplier);
    }

    /**
     * @return {@link ReentrantReadWriteLocks} instance using {@link ReentrantReadWriteLock}
     */
    public static ReentrantReadWriteLocks reentrant() {
        return new ReentrantReadWriteLocks();
    }

    /**
     * Return a {@link ReadWriteLock} (of type {@link L}) where the {@link ReadWriteLock#readLock()} is already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already locked lock
     */
    public L readLock(Object key) {
        final var lock = get(key);
        lock.readLock().lock();
        return lock;
    }

    /**
     * Return a {@link ReadWriteLock} (of type {@link L}) where the {@link ReadWriteLock#writeLock()} is already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already locked lock
     */
    public L writeLock(Object key) {
        final var lock = get(key);
        lock.writeLock().lock();
        return lock;
    }

}
