package net.dapete.locks;

import org.jspecify.annotations.Nullable;

import java.util.concurrent.Semaphore;

///
/// Key-based semaphores.
///
/// @param <K> the key type
/// @since 1.6.0
///
public final class Semaphores<K extends @Nullable Object> extends WeakKeyReferences<K, Semaphore> {

    private final boolean fair;

    private Semaphores(boolean fair, int permits) {
        super(() -> new Semaphore(permits, fair));
        this.fair = fair;
    }

    ///
    /// Return a [Semaphores] instance.
    ///
    /// @param permits the number of permits for generated `Semaphore`s.
    /// @param <K>     the key type.
    /// @return a `Semaphores` instance.
    ///
    public static <K> Semaphores<K> instance(int permits) {
        return instance(false, permits);
    }

    ///
    /// Return a [Semaphores] instance.
    ///
    /// @param permits  the number of permits for generated `Semaphore`s.
    /// @param keyClass the class for the key type.
    /// @param <K>      the key type.
    /// @return a `Semaphores` instance.
    ///
    public static <K> Semaphores<K> instance(int permits, @SuppressWarnings("unused") Class<K> keyClass) {
        return instance(permits);
    }

    ///
    /// Return a [Semaphores] instance with the given fairness policy.
    ///
    /// @param fair    `true` if the locks should use a fair ordering policy (see [Semaphore#Semaphore(int, boolean)]).
    /// @param permits the number of permits for generated `Semaphore`s.
    /// @param <K>     the key type.
    /// @return a `Semaphores` instance.
    ///
    public static <K> Semaphores<K> instance(boolean fair, int permits) {
        return new Semaphores<>(fair, permits);
    }

    ///
    /// Return a [Semaphores] instance with the given fairness policy.
    ///
    /// @param fair     `true` if the locks should use a fair ordering policy (see [Semaphore#Semaphore(int, boolean)]).
    /// @param permits  the number of permits for generated `Semaphore`s.
    /// @param keyClass the class for the key type.
    /// @param <K>      the key type.
    /// @return a `Semaphores` instance.
    ///
    public static <K> Semaphores<K> instance(boolean fair, int permits, @SuppressWarnings("unused") Class<K> keyClass) {
        return instance(fair, permits);
    }

    ///
    /// Return a semaphore for `key`. There will be at most one semaphore per key at any given time.
    ///
    /// @param key the key
    /// @return a lock for `key`.
    ///
    @Override
    public Semaphore get(@Nullable K key) {
        return super.get(key);
    }

    ///
    /// Return the current number of semaphores managed by this instance.
    ///
    /// @return the current number of semaphores managed by this instance.
    ///
    @Override
    public int size() {
        return super.size();
    }

    ///
    /// Return `true` if semaphores returned by this instance have fairness set true.
    ///
    /// @return `true` if semaphores returned by this instance have fairness set true.
    ///
    public boolean isFair() {
        return fair;
    }

}
