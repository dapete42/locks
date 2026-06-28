package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

abstract class WeakKeyReferences<K extends @Nullable Object, V> {

    private final Lock instanceLock = new ReentrantLock();

    private final Map<@Nullable K, WeakKeyReference<@Nullable K, V>> referenceMap = new HashMap<>();

    private final ReferenceQueue<V> referenceQueue = new ReferenceQueue<>();

    private final Supplier<V> supplier;

    protected WeakKeyReferences(Supplier<V> supplier) {
        this.supplier = supplier;
    }

    private V createValue(@Nullable K key) {
        final var newValue = supplier.get();
        referenceMap.put(key, new WeakKeyReference<>(key, newValue, referenceQueue));
        return newValue;
    }

    ///
    /// Return a value for the supplied `key`. There will be at most one value per key at any given time.
    ///
    /// @param key the key.
    /// @return a value for the supplied `key`.
    ///
    protected V get(@Nullable K key) {
        instanceLock.lock();
        try {
            processQueue();
            return getValue(key);
        } finally {
            instanceLock.unlock();
        }
    }

    private V getValue(@Nullable K key) {
        final var reference = getReference(key);
        if (reference != null) {
            final V value = reference.get();
            if (value != null) {
                return value;
            }
        }
        return createValue(key);
    }

    private @Nullable WeakKeyReference<K, V> getReference(@Nullable K key) {
        return referenceMap.get(key);
    }

    ///
    /// Return the current number of values managed by this instance.
    ///
    /// @return the current number of values managed by this instance.
    ///
    protected int size() {
        instanceLock.lock();
        try {
            processQueue();
            return referenceMap.size();
        } finally {
            instanceLock.unlock();
        }
    }

    ///
    /// Removes all values that have been marked as unreachable by the garbage collector.
    ///
    private void processQueue() {
        Reference<?> reference;
        while ((reference = referenceQueue.poll()) != null) {
            if (reference instanceof WeakKeyReference) {
                final var keyReference = (WeakKeyReference<?, ?>) reference;
                referenceMap.remove(keyReference.getKey());
            }
        }
    }

}
