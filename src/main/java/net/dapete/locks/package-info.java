///
/// This package contains classes for key-based locking, as in a way to create instances of [Lock][java.util.concurrent.locks.Lock] or
/// [ReadWriteLock][java.util.concurrent.locks.ReadWriteLock] which are identified by a key. These locks are guaranteed to by different for each key and will be
/// kept as long as they are referenced.
///
/// <table class="striped">
///     <caption>Obtaining instances</caption>
///     <thead>
///         <tr>
///             <th>Lock type</th>
///             <th>Method(s)</th>
///             <th>Return type</th>
///         </tr>
///     </thead>
///     <tbody>
///         <tr>
///             <td>{@link java.util.concurrent.locks.ReentrantLock ReentrantLock}</td>
///             <td>{@link net.dapete.locks.Locks#reentrant() Locks.reentrant()}<br>
///                 {@link net.dapete.locks.Locks#reentrant(Class)}<br>
///                 {@link net.dapete.locks.Locks#reentrant(boolean)}<br>
///                 {@link net.dapete.locks.Locks#reentrant(boolean, Class)}</td>
///             <td>{@link net.dapete.locks.ReentrantLocks}</td>
///         </tr>
///         <tr>
///             <td>any implementation of {@link java.util.concurrent.locks.Lock Lock}</td>
///             <td>{@link net.dapete.locks.Locks#withSupplier(java.util.function.Supplier) Locks.withSupplier(Supplier)}</td>
///             <td>{@link net.dapete.locks.Locks}</td>
///         </tr>
///         <tr>
///             <td>{@link java.util.concurrent.locks.ReentrantReadWriteLock ReentrantReadWriteLock}</td>
///             <td>{@link net.dapete.locks.ReadWriteLocks#reentrant()}<br>
///                 {@link net.dapete.locks.ReadWriteLocks#reentrant(Class)}<br>
///                 {@link net.dapete.locks.ReadWriteLocks#reentrant(boolean)}<br>
///                 {@link net.dapete.locks.ReadWriteLocks#reentrant(boolean, Class)}</td>
///             <td>{@link net.dapete.locks.ReentrantReadWriteLocks}</td>
///         </tr>
///         <tr>
///             <td>any implementation of {@link java.util.concurrent.locks.ReadWriteLock ReadWriteLock}</td>
///             <td>{@link net.dapete.locks.ReadWriteLocks#withSupplier(java.util.function.Supplier) ReadWriteLocks.withSupplier(Supplier)}</td>
///             <td>{@link net.dapete.locks.ReadWriteLocks}</td>
///         </tr>
///     </tbody>
/// </table>
///
/// - All implementations use generics for the key type as well as the lock type, if this is not fixed (as with [net.dapete.locks.ReentrantLocks] and
///   [net.dapete.locks.ReentrantReadWriteLocks]).
///
/// - The [Class][java.lang.Class] parameter on the `reentrant(Class)` and `reentrant(boolean, Class)` methods is the `Class` of the key type. This is a
///   shortcut for readability if the compiler does not automatically detect it, for example:
///
///     ```
///     Locks.reentrant(String.class)
///     ```
///
/// - The `boolean` parameter on the `reentrant(boolean)` and `reentrant(boolean, Class)` sets the fairness policy of the reentrant lock instances used. See
///   also the constructors [ReentrantLock(boolean)][java.util.concurrent.locks.ReentrantLock#ReentrantLock(boolean)] and
///   [ReentrantReadWriteLock(boolean)][java.util.concurrent.locks.ReentrantReadWriteLock#ReentrantReadWriteLock(boolean)].
///
/// - The `withSupplier(Supplier)` methods allow for any implementation of [Lock][java.util.concurrent.locks.Lock] or
///   [ReadWriteLock][java.util.concurrent.locks.ReadWriteLock] to be used. You can use the constructor as the `Supplier`, for example:
///
///    ```
///    Locks.withSupplier(SuperEpicLock::new)
///    ```
///
/// # Examples
///
/// Following the same pattern as described in the JDK documentation for [Lock][java.util.concurrent.locks.Lock], using [net.dapete.locks.Locks] should look
/// similar to this:
///
/// ```
/// public class LocksExample {
///
///     private final ReentrantLocks<String> locks = Locks.reentrant();
///
///     public void doSomething(String url) {
///         final var lock = locks.lock(url);
///         try {
///             // do something with the URL
///         } finally {
///             lock.unlock();
///         }
///     }
///
/// }
/// ```
///
/// It is important to keep the lock in a local variable while it is being used. It is stored in a [WeakReference][java.lang.ref.WeakReference], so it could be
/// removed by the garbage collector at any time while it is not referenced.
///
/// An alternative way which may be useful if the lock is not always used or used multiple times would be to split the `final var lock = …` line
/// in two:
///
/// ```
/// final var lock = locks.get(url);
/// lock.lock();
/// ```
///
/// For [net.dapete.locks.ReadWriteLocks] it is similar to the first example:
///
/// ```
/// public class ReadWriteLocksExample {
///
///     private final ReentrantReadWriteLocks<String> locks = ReadWriteLocks.reentrant();
///
///     public void doSomethingRead(String url) {
///         final var lock = locks.readLock(url);
///         try {
///             // do something with the URL
///         } finally {
///             lock.readLock().unlock();
///         }
///     }
///
///     public void doSomethingWrite(String url) {
///         final var lock = locks.writeLock(url);
///         try {
///             // do something with the URL
///         } finally {
///             lock.writeLock().unlock();
///         }
///     }
///
/// }
/// ```
///
/// Again, the `final var lock = …` lines could be split, which may be useful if both read and write locks are used in the method.
///
package net.dapete.locks;
