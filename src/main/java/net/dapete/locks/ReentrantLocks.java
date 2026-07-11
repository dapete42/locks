package net.dapete.locks;

import org.apiguardian.api.API;

import java.util.concurrent.locks.ReentrantLock;

import static org.apiguardian.api.API.Status.STABLE;

///
/// Key-based locking using instances of [ReentrantLock].
///
/// Instances can be created using [Locks#reentrant()], [Locks#reentrant(Class)], [Locks#reentrant(boolean)] and [Locks#reentrant(boolean, Class)].
///
/// @param <K> the key type.
///
@API(status = STABLE)
public final class ReentrantLocks<K> extends Locks<K, ReentrantLock> {

    private final boolean fair;

    ReentrantLocks() {
        this(false);
    }

    ReentrantLocks(boolean fair) {
        super(() -> new ReentrantLock(fair));
        this.fair = fair;
    }

    ///
    /// Return `true` if locks returned by this instance have fairness set true.
    ///
    /// @return `true` if locks returned by this instance have fairness set true.
    /// @since 1.3.3
    ///
    @API(status = STABLE, since = "1.3.3")
    public boolean isFair() {
        return fair;
    }

}
