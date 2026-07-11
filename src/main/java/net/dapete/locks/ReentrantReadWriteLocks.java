package net.dapete.locks;

import org.apiguardian.api.API;

import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.apiguardian.api.API.Status.STABLE;

///
/// Key-based locking using instances of [ReentrantReadWriteLock].
///
/// Instances can be created using [ReadWriteLocks#reentrant()], [ReadWriteLocks#reentrant(Class)], [ReadWriteLocks#reentrant(boolean)] and
/// [ReadWriteLocks#reentrant(boolean, Class)].
///
/// @param <K> the key type.
///
@API(status = STABLE)
public final class ReentrantReadWriteLocks<K> extends ReadWriteLocks<K, ReentrantReadWriteLock> {

    private final boolean fair;

    ReentrantReadWriteLocks() {
        this(false);
    }

    ReentrantReadWriteLocks(boolean fair) {
        super(() -> new ReentrantReadWriteLock(fair));
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
