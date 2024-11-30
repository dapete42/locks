package net.dapete.locks;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLocks extends ReadWriteLocks<ReentrantReadWriteLock> {

    public ReentrantReadWriteLocks() {
        super(ReentrantReadWriteLock::new);
    }

}
