package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

abstract class WeakKeyReferences<K extends @Nullable Object, T> {

    private final Lock instanceLock = new ReentrantLock();

    private final Map<@Nullable K, WeakKeyReference<@Nullable K, T>> referenceMap = new HashMap<>();

    private final ReferenceQueue<T> referenceQueue = new ReferenceQueue<>();

    private final Supplier<T> supplier;

    protected WeakKeyReferences(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    private T createObject(@Nullable K key) {
        final var newObject = supplier.get();
        referenceMap.put(key, new WeakKeyReference<>(key, newObject, referenceQueue));
        return newObject;
    }

    ///
    /// Return a reference for the supplied `key`. There will be at most one reference per key at any given time.
    ///
    /// @param key the key.
    /// @return a reference for the supplied `key`.
    ///
    protected T get(@Nullable K key) {
        instanceLock.lock();
        try {
            processQueue();
            return getInternal(key);
        } finally {
            instanceLock.unlock();
        }
    }

    private T getInternal(@Nullable K key) {
        final var reference = getReference(key);
        if (reference != null) {
            final T object = reference.get();
            if (object != null) {
                return object;
            }
        }
        return createObject(key);
    }

    private @Nullable WeakKeyReference<K, T> getReference(@Nullable K key) {
        return referenceMap.get(key);
    }

    ///
    /// Return the current number of references managed by this instance.
    ///
    /// @return the current number of references managed by this instance.
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
    /// Removes all objects that have been marked as unreachable by the garbage collector.
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
