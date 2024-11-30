package net.dapete.locks;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLocks extends Locks<ReentrantLock> {

    public ReentrantLocks() {
        super(ReentrantLock::new);
    }

}
