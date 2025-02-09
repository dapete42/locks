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
     * @param lockSupplier Supplier for instances of {@link L} (usually the constructor of a class implementing {@link Lock})
     * @param <K>          type of key
     * @param <L>          type of {@link Lock}
     * @return instance using {@link Lock} implementations created by the specified {@code lockSupplier}
     */
    public static <K, L extends Lock> Locks<K, L> withSupplier(Supplier<L> lockSupplier) {
        return new Locks<>(lockSupplier);
    }

    /**
     * @param <K> type of key
     * @return {@link ReentrantLocks} instance using {@link ReentrantLock}
     */
    public static <K> ReentrantLocks<K> reentrant() {
        return new ReentrantLocks<>();
    }

    /**
     * @param keyClass class of key
     * @param <K>      type of key
     * @return {@link ReentrantLocks} instance using {@link ReentrantLock}
     */
    public static <K> ReentrantLocks<K> reentrant(Class<K> keyClass) {
        return new ReentrantLocks<>();
    }

    /**
     * Return a {@link Lock} (of type {@link L}) already locked using {@link Lock#lock()}.
     *
     * @param key key
     * @return already locked lock
     */
    public L lock(K key) {
        final L lock = get(key);
        lock.lock();
        return lock;
    }

}
