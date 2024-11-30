package net.dapete.locks;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

class LockReference<L> extends WeakReference<L> {

    private final Object key;

    LockReference(Object key, L value, ReferenceQueue<? super L> referenceQueue) {
        super(value, referenceQueue);
        this.key = key;
    }

    Object getKey() {
        return key;
    }

}
