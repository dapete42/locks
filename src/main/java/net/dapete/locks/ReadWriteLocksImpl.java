package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

class ReadWriteLocksImpl<K extends @Nullable Object, L extends ReadWriteLock> extends AbstractLocks<K, L> implements ReadWriteLocks<K, L> {

    ReadWriteLocksImpl(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    @Override
    public final L readLock(@Nullable K key) {
        final var lock = get(key);
        lock.readLock().lock();
        return lock;
    }

    @Override
    public final L writeLock(@Nullable K key) {
        final var lock = get(key);
        lock.writeLock().lock();
        return lock;
    }

}
