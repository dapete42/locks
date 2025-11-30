package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.locks.Lock;
import java.util.function.Supplier;

class LocksImpl<K extends @Nullable Object, L extends Lock> extends AbstractLocks<K, L> implements Locks<K, L> {

    LocksImpl(Supplier<L> lockSupplier) {
        super(lockSupplier);
    }

    @Override
    public final L lock(@Nullable K key) {
        final var lock = get(key);
        lock.lock();
        return lock;
    }

}
