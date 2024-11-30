package net.dapete.locks;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Key-based locking with implementations of {@link Lock}.
 *
 * @param <L> type of {@link Lock}
 */
public class Locks<L extends Lock> extends AbstractLocks<L> {

    Locks(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    /**
     * @param lockSupplier Supplier for instances of {@link L} (usually the constructor of a class implementing {@link Lock})
     * @param <L>          type of {@link Lock}
     * @return instance using {@link Lock} implementations created by the specified {@code lockSupplier}
     */
    public static <L extends Lock> Locks<L> withSupplier(Supplier<L> lockSupplier) {
        return new Locks<>(lockSupplier);
    }

    /**
     * @return {@link ReentrantLocks} instance using {@link ReentrantLock}
     */
    public static ReentrantLocks reentrant() {
        return new ReentrantLocks();
    }

    /**
     * Return a {@link Lock} (of type {@link L}) already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already locked lock
     */
    public L lock(Object key) {
        final var lock = get(key);
        lock.lock();
        return lock;
    }

}
