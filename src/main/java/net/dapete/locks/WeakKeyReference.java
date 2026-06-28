package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class WeakKeyReference<K extends @Nullable Object, T> extends WeakReference<T> {

    private final @Nullable K key;

    WeakKeyReference(@Nullable K key, T value, ReferenceQueue<? super T> referenceQueue) {
        super(value, referenceQueue);
        this.key = key;
    }

    @Nullable K getKey() {
        return key;
    }

}
