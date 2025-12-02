import org.jspecify.annotations.NullMarked;

///
/// This module contains classes for key-based locking, as in a way to create instances of [Lock][java.util.concurrent.locks.Lock] or
/// [ReadWriteLock][java.util.concurrent.locks.ReadWriteLock] which are identified by a key.
///
/// @see net.dapete.locks documentation for package net.dapete.locks
/// @since 1.1.0
///
@NullMarked
module net.dapete.locks {
    requires org.jspecify;
    exports net.dapete.locks;
}
