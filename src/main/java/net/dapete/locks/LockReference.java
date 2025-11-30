package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class LockReference<K extends @Nullable Object, L> extends WeakReference<L> {

    private final @Nullable K key;

    LockReference(@Nullable K key, L value, ReferenceQueue<? super L> referenceQueue) {
        super(value, referenceQueue);
        this.key = key;
    }

    @Nullable K getKey() {
        return key;
    }

}
