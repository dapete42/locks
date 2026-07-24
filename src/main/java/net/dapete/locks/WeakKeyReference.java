package net.dapete.locks;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

final class WeakKeyReference<K, T> extends WeakReference<T> {

    private final K key;

    WeakKeyReference(K key, T value, ReferenceQueue<? super T> referenceQueue) {
        super(value, referenceQueue);
        this.key = key;
    }

    K getKey() {
        return key;
    }

}
